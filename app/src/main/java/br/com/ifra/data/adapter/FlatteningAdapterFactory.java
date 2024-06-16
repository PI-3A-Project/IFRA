package br.com.ifra.data.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FlatteningAdapterFactory implements TypeAdapterFactory {
    // Called by Gson
    public FlatteningAdapterFactory() {
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        TypeAdapter<JsonObject> jsonObjectAdapter = gson.getAdapter(JsonObject.class);
        TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(this, type);

        return new TypeAdapter<T>() {
            private final char separator = '.';

            private void flattenInto(String name, JsonObject toFlatten, JsonObject destination) {
                for (Map.Entry<String, JsonElement> entry : toFlatten.entrySet()) {
                    String flattenedName = name + separator + entry.getKey();
                    if (destination.has(flattenedName)) {
                        throw new IllegalArgumentException("Duplicate name: " + flattenedName);
                    }
                    destination.add(flattenedName, entry.getValue());
                }
            }

            @Override
            public T read(JsonReader in) throws IOException {
                if (in.peek() == JsonToken.NULL) {
                    in.skipValue();
                    return null;
                }

                /*
                 * Flattens nested JsonObject values, e.g.:
                 * {
                 * "a": 1,
                 * "b": {
                 * "x": true,
                 * "y": 2
                 * }
                 * }
                 * Becomes
                 * {
                 * "a": 1,
                 * "b.x": true,
                 * "b.y": 2
                 * }
                 */

                JsonObject jsonObject = jsonObjectAdapter.read(in);
                JsonObject flattened = new JsonObject();

                for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                    String name = entry.getKey();
                    JsonElement value = entry.getValue();

                    // Flatten the value
                    if (value instanceof JsonObject) {
                        flattenInto(name, (JsonObject) value, flattened);
                    }

                    // But also add the non-flattened value in case this entry should not actually be flattened
                    // The delegate adapter will then ignore either the flattened or the non-flattened entries
                    if (flattened.has(name)) {
                        throw new IllegalArgumentException("Duplicate name: " + name);
                    }
                    flattened.add(name, value);
                }

                // Now read the flattened JsonObject using the delegate adapter
                return delegateAdapter.fromJsonTree(flattened);
            }

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                if (value == null) {
                    out.nullValue();
                    return;
                }

                /*
                 * Expands the flattened JsonObject, e.g.:
                 * {
                 * "a": 1,
                 * "b.x": true,
                 * "b.y": 2
                 * }
                 * Becomes
                 * {
                 * "a": 1,
                 * "b": {
                 * "x": true,
                 * "y": 2
                 * }
                 * }
                 */

                JsonObject flattened = (JsonObject) delegateAdapter.toJsonTree(value);
                JsonObject expanded = new JsonObject();
                Map<String, JsonElement> expandedAsMap = jsonObjectToMap(expanded);

                for (Map.Entry<String, JsonElement> entry : flattened.entrySet()) {
                    String name = entry.getKey();
                    JsonElement entryValue = entry.getValue();

                    // Expand the flattened entry
                    int separatorIndex = name.indexOf(separator);
                    if (separatorIndex != -1) {
                        String namePrefix = name.substring(0, separatorIndex);
                        String nameSuffix = name.substring(separatorIndex + 1);
                        JsonObject nestedObject = (JsonObject) expandedAsMap.computeIfAbsent(namePrefix, k -> new JsonObject());

                        if (nestedObject.has(nameSuffix)) {
                            throw new IllegalArgumentException("Duplicate name: " + nameSuffix);
                        }
                        nestedObject.add(nameSuffix, entryValue);
                    } else {
                        if (expanded.has(name)) {
                            throw new IllegalArgumentException("Duplicate name: " + name);
                        }
                        expanded.add(name, entryValue);
                    }
                }

                // Finally write the expanded JsonObject to the actual writer
                jsonObjectAdapter.write(out, expanded);
            }
        };
    }

    public static Map<String, JsonElement> jsonObjectToMap(JsonObject jsonObject) {
        Map<String, JsonElement> map = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
        for (Map.Entry<String, JsonElement> entry : entrySet) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }
}

