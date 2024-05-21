package br.com.ifra.data.serializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class GsonCustomDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T object = context.deserialize(json, typeOfT);

        alternateSerizalized(json, object, typeOfT);

        return object;
    }

    private void alternateSerizalized(JsonElement json, T object, Type typeOfT) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Obtém todos os campos da classe
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Verifica se o campo possui uma annotation SerializedName com um alternate
            SerializedName annotation = field.getAnnotation(SerializedName.class);
            if (annotation != null && annotation.alternate() != null) {
                // Verifica se há caminhos alternativos
                String[] alternates = annotation.alternate();
                String fieldValue = null;

                // cada anotation possui várias possibilidade então percorro todos os anotation em busca do que condiz com o json
                for (String alternate : alternates) {
                    // pega os caminho completo do alternate
                    String[] caminho = alternate.split("/.");

                    // alternate com campo único já é transferido pelo gson
                    if(caminho.length > 1) {
                        // entra nos objetos até encontrar o campo especificado
                        for (String obj: caminho) {
                            if(jsonObject.has(obj)) {
                                fieldValue = jsonObject.get(obj).getAsString();
                            } else {
                                // caso não chegue no ultimo e não encontrou o objeto então não existe o objeto esperado
                                fieldValue = null;
                                break;
                            }
                        }
                        // seta o valor pego no json no objeto
                        if (fieldValue != null) {
                            try {
                                field.setAccessible(true);
                                field.set(object, fieldValue);
                            } catch (IllegalAccessException e) {
                                throw new JsonParseException("Erro na transferência do json, parametro: " + field.getName());
                            }
                        }
                    }
                }
            }
        }
    }

}