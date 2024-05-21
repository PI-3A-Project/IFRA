package br.com.ifra.data.network;

import java.io.IOException;
import java.util.List;

import br.com.ifra.enums.ErroEnum;
import br.com.ifra.enums.Metodo;
import br.com.ifra.exceptions.HttpMethodException;
import br.com.ifra.utils.MessageUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpMethods {
    /**
     *
     * @param json
     * @param url
     * @param metodo
     * @param headers
     * @return
     * @throws IOException
     * @throws HttpMethodException
     */
    public static String executar(String json, String url, Metodo metodo, List<String[]> headers) throws IOException, HttpMethodException {
        OkHttpClient client = new OkHttpClient();

        MediaType JSON = MediaType.get("application/json");
        RequestBody body = null;

        if(json != null && !json.isEmpty()) {
            RequestBody.create(json, JSON);
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(url).method(metodo.name(), body);

        if (headers != null) {
            for (String[] header : headers) {
                if (header.length == 2) {
                    requestBuilder.addHeader(header[0], header[1]);
                }
            }
        }

        Request request = new okhttp3.Request.Builder()
                .url(url)
                .method(metodo.name(), body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if(response.body() != null) {
                    return response.body().string();
                } else {
                    throw new HttpMethodException(ErroEnum.NO_BODY);
                }
            } else {
                throw new HttpMethodException(ErroEnum.RESPONSE);
            }
        } catch (IOException e) {
            throw new IOException(MessageUtil.getMessage(ErroEnum.API.getValue()));
        }
    }
}
