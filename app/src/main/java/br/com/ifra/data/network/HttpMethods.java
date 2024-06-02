package br.com.ifra.data.network;

import android.os.AsyncTask;
import android.util.Log;

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

public class HttpMethods extends AsyncTask<String, Void, String> {
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
    public static String executar(String json, String url, Metodo metodo, List<String[]> headers)  throws IOException, HttpMethodException {
        OkHttpClient client = new OkHttpClient();


        RequestBody body = null;
        if(json != null && !json.isEmpty()) {
            MediaType JSON = MediaType.get("application/json");
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
        } catch (Exception e) {
            String mensagem = e.getMessage();
            Log.d("Erro HTTP", e.getMessage());
            e.printStackTrace();
            throw new IOException(MessageUtil.getMessage(ErroEnum.API.getValue()));
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
