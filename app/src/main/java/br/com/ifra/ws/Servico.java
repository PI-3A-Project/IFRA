package br.com.ifra.ws;

import java.io.IOException;

import br.com.ifra.arquitetura.BeanAbstrato;
import br.com.ifra.arquitetura.RetornoDTO;
import br.com.ifra.arquitetura.enums.ErroEnum;
import br.com.ifra.arquitetura.enums.Filtro;
import br.com.ifra.arquitetura.enums.Metodo;
import br.com.ifra.ws.bean.erro.ErroBean;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Servico {
    String baseUrl = "https://www.googleapis.com/books/v1/";

    OkHttpClient client = new OkHttpClient();

    public RetornoDTO<String> buscar(Metodo metodo, String json, Filtro filtro) {
        RetornoDTO<String> retorno = new RetornoDTO<>();
        StringBuilder url = new StringBuilder(baseUrl);
        url.append("/volumes?q={");
        url.append(filtro);
        url.append("}");

        MediaType JSON = MediaType.get("application/json");

        RequestBody body = RequestBody.create(json, JSON);
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
                retorno.setErro(new ErroBean.Builder()
                        .tipoErro(ErroEnum.RESPONSE)
                        .mensagem("Erro na requisição")
                        .erro(new ErroBean.Builder()
                                .mensagem(response.body().string())
                                .build())
                        .build());
            }
        } catch (IOException e) {
            retorno.setErro(new ErroBean.Builder()
                    .tipoErro(ErroEnum.HTTP)
                    .mensagem("Erro ao tentar conectar na API")
                    .build());
        }
        return retorno;
    }
}
