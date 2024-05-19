package br.com.ifra.arquitetura;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class GsonDeserializer<T> implements JsonDeserializer<T> {

    @Override
    public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        T object = new Gson().fromJson(json, typeOfT);

        alternateSerizalized(json, object, typeOfT);

        return object;
    }

    private void alternateSerizalized(JsonElement json, T object, Type typeOfT) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        // Obtém todos os campos da classe
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            SerializedName annotation = field.getAnnotation(SerializedName.class);
            if (annotation != null) {
                // Verifica se há caminhos alternativos
                String[] alternates = annotation.alternate();
                String fieldValue = null;

                if(alternates.length > 0) {
                    for (String alternate : alternates) {
                        String[] caminho = alternate.split("/.");
                        for (String obj: caminho) {
                            fieldValue = jsonObject.get(obj).getAsString();
                        }
                        if (jsonObject.has(alternate)) {
                            fieldValue = jsonObject.get(alternate).getAsString();
                            break;
                        }
                    }
                    if (fieldValue != null) {
                        try {
                            field.setAccessible(true);
                            field.set(object, fieldValue);
                        } catch (IllegalAccessException e) {
                            throw new JsonParseException("Erro na transferência do json");
                        }
                    }
                }

            }
        }
    }

}