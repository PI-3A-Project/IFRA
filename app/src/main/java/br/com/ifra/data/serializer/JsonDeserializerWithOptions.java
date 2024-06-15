package br.com.ifra.data.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import br.com.ifra.data.annotations.Converter;
import br.com.ifra.data.annotations.Interno;
import br.com.ifra.data.annotations.Obrigatorio;

public class JsonDeserializerWithOptions<T> implements JsonDeserializer<T> {
    /**
     * To mark required fields of the model:
     * json parsing will be failed if these fields won't be provided.
     */

    /**
     * Called when the model is being parsed.
     *
     * @param je
     *            Source json string.
     * @param type
     *            Object's model.
     * @param jdc
     *            Unused in this case.
     *
     * @return Parsed object.
     *
     * @throws JsonParseException
     *             When parsing is impossible.
     */
    @Override
    public T deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException {
        // Parsing object as usual.
        T pojo = new Gson().fromJson(je, type);

        StringBuilder erros = new StringBuilder();
        // Getting all fields of the class and checking if all required ones were provided.
        checkRequiredFields(pojo.getClass().getDeclaredFields(), pojo, erros);

        checkInerObjects(pojo.getClass().getDeclaredFields(), pojo, je, erros);

        // Checking if all required fields of parent classes were provided.
        checkSuperClasses(pojo, erros);

        converterDados(pojo.getClass().getDeclaredFields(), pojo, erros);

        if (erros.length() > 0) {
            throw new JsonParseException(erros.toString());
        }
        // All checks are ok.
        return pojo;
    }

    private void converterDadosSuperClasses(Object pojo, StringBuilder erros) {
        Class<?> superclass = pojo.getClass();
        while ((superclass = superclass.getSuperclass()) != null) {
            converterDados(superclass.getDeclaredFields(), pojo, erros);
        }
    }

    private void converterDados(Field[] fields, Object pojo, StringBuilder erros) {

        if (pojo instanceof List) {
            final List<?> pojoList = (List<?>) pojo;
            for (final Object pojoListPojo : pojoList) {
                converterDados(pojoListPojo.getClass().getDeclaredFields(), pojoListPojo, erros);
                converterDadosSuperClasses(pojoListPojo, erros);
            }
        }

        Converter convert = null;
        if (pojo.getClass().getAnnotation(Converter.class) != null) {
            convert = pojo.getClass().getAnnotation(Converter.class);
        }
        for (Field field : fields) {

            if (field instanceof Object) {
                field.setAccessible(true);
                try {
                    Object subObj = field.get(pojo);
                    if (subObj != null && subObj.getClass().getAnnotation(Converter.class) != null) {
                        converterDados(subObj.getClass().getDeclaredFields(), subObj, erros);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                }
            }

            if (field.getAnnotation(Converter.class) != null) {
                Converter convertCustom = field.getAnnotation(Converter.class);

                try {
                    // Converter data para um padrão do banco, necessário colocar a annotation no field ou na classe, para que saiba qual o
                    // pattern do banco
                    if (convertCustom.dataPattern() == null || convertCustom.dataPattern().isEmpty()) {
                        convertCustom = convert;
                    }
                    if (convert.dataPattern() != null && !convert.dataPattern().isEmpty()) {

                        if (convertCustom.dataPattern() != null && !convertCustom.dataPattern().isEmpty()) {
                            SimpleDateFormat format = new SimpleDateFormat(convertCustom.dataPatternDefault());
                            SimpleDateFormat newFormat = new SimpleDateFormat(convertCustom.dataPattern());

                            field.setAccessible(true);

                            Object dataObj = field.get(pojo);
                            if (dataObj != null) {
                                Date date = format.parse((String) dataObj);
                                String dataReturn = newFormat.format(date);

                                field.set(pojo, dataReturn);
                            }
                        }
                    }
                } catch (IllegalArgumentException | IllegalAccessException | ParseException e) {
                    erros.append("Converter data " + field.getName() + " pattern esperado: " + convertCustom.dataPatternDefault());
                }
            }
        }
    }

    /**
     * Verifica se existem objetos que não possuem associação no Json, normalmente ele só ignoraria,
     * porém dessa forma ele instancia esse objeto para buscar as informações dentro daquele objeto
     *
     * @param fields
     * @param pojo
     * @param je
     */
    private void checkInerObjects(Field[] fields, Object pojo, JsonElement je, StringBuilder erros) {
        for (Field field : fields) {
            if (field.getAnnotation(Interno.class) != null) {
                Object object = null;
                try {
                    object = field.getType().getDeclaredConstructor().newInstance();

                    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(object.getClass(), new JsonDeserializerWithOptions<>()).create();
                    object = gson.fromJson(je, object.getClass());
                    field.setAccessible(true);
                    field.set(pojo, object);

                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
                    erros.append("Objeto " + field.getName() + " não intanciado");
                }
            }
        }
    }

    /**
     * Checks whether all required fields were provided in the class.
     *
     * @param fields
     *            Fields to be checked.
     * @param pojo
     *            Instance to check fields in.
     *
     */
    private void checkRequiredFields(Field[] fields, Object pojo, StringBuilder erros) {
        // Checking nested list items too.
        if (pojo instanceof List) {
            final List pojoList = (List) pojo;
            for (final Object pojoListPojo : pojoList) {
                checkRequiredFields(pojoListPojo.getClass().getDeclaredFields(), pojoListPojo, erros);
                checkSuperClasses(pojoListPojo, erros);
            }
        }

        for (Field f : fields) {
            // If some field has required annotation.
            if (f.getAnnotation(Obrigatorio.class) != null) {
                try {
                    // Trying to read this field's value and check that it truly has value.
                    f.setAccessible(true);
                    Object fieldObject = f.get(pojo);
                    if (fieldObject == null) {
                        // Required value is null - throwing error.
                        erros.append("Campo obrigatório: " + String.format("%1$s -> %2$s", pojo.getClass().getSimpleName(), f.getName()) + "\n");
                    } else {
                        checkRequiredFields(fieldObject.getClass().getDeclaredFields(), fieldObject, erros);
                        checkSuperClasses(fieldObject, erros);
                    }
                }

                // Exceptions while reflection.
                catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new JsonParseException(e);
                }
            }
        }
    }

    /**
     * Checks whether all super classes have all required fields.
     *
     * @param pojo
     *            Object to check required fields in its superclasses.
     *
     * @throws JsonParseException
     *             When some required field was not met.
     */
    private void checkSuperClasses(Object pojo, StringBuilder erros) throws JsonParseException {
        Class<?> superclass = pojo.getClass();
        while ((superclass = superclass.getSuperclass()) != null) {
            checkRequiredFields(superclass.getDeclaredFields(), pojo, erros);
        }
    }
}
