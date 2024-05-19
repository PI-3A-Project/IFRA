package br.com.ifra.servico;

import java.io.IOException;
import java.util.List;

import br.com.ifra.model.response.dto.RetornoDTO;
import br.com.ifra.enums.ErroEnum;
import br.com.ifra.enums.Filtro;
import br.com.ifra.enums.Metodo;
import br.com.ifra.model.erro.Error;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Servico {
    private static String  baseUrl = "https://www.googleapis.com/books/v1/";



    public static RetornoDTO<String> buscarVolume(List<Filtro> filtro, Metodo metodo) {
        OkHttpClient client = new OkHttpClient();
        RetornoDTO<String> retorno = new RetornoDTO<>();
        StringBuilder url = new StringBuilder(baseUrl);

        url.append("/volumes?q={");
        url.append(filtro);
        url.append("}");

        MediaType JSON = MediaType.get("application/json");

        RequestBody body = RequestBody.create("", JSON);
        Request request = new Request.Builder()
                .url(url.toString())
                .method(metodo.name(), body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if(response.isSuccessful()) {
                retorno.setRetorno(response.body().string());
                retorno.setSucesso(true);
            } else {
                assert response.body() != null;
                retorno.setErro(new Error.Builder()
                        .tipoErro(ErroEnum.RESPONSE)
                        .mensagem("Erro na requisição")
                        .erro(new Error.Builder()
                                .mensagem(response.body().string())
                                .build())
                        .build());
            }
        } catch (IOException e) {
            retorno.setErro(new Error.Builder()
                    .tipoErro(ErroEnum.HTTP)
                    .mensagem("Erro ao tentar conectar na API")
                    .build());
        }
        return retorno;
    }
}
