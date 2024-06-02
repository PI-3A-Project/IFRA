package br.com.ifra.service;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.ifra.data.model.dto.ReturnDTO;
import br.com.ifra.data.model.dto.erro.ErrorDTO;
import br.com.ifra.data.model.dto.volume.ListVolumeDTO;
import br.com.ifra.data.network.HttpMethods;
import br.com.ifra.data.serializer.GsonCustomDeserializer;
import br.com.ifra.enums.ErroEnum;
import br.com.ifra.enums.Metodo;
import br.com.ifra.exceptions.HttpMethodException;
import br.com.ifra.utils.MessageUtil;

public class Service extends AsyncTask<String, Void, String> {

    private static Locale locale;
    private static final String baseUrl = "https://www.googleapis.com/books/v1/";
    public static ReturnDTO<ListVolumeDTO> buscarVolume(String search) {
        ReturnDTO<ListVolumeDTO> retorno = new ReturnDTO<>();
        String url = baseUrl + "volumes";
        String filter = "?q={"+ search +"}";
        url += filter;
        try {
            String response = HttpMethods.executar(null, url, Metodo.GET, criarHeader());

            Gson gson = new GsonBuilder().registerTypeAdapter(ListVolumeDTO.class, new GsonCustomDeserializer<>()).create();

            ListVolumeDTO listaVolume = gson.fromJson(response, ListVolumeDTO.class);
            retorno.setRetorno(listaVolume);
            retorno.setSucesso(true);
        } catch(IOException e) {
            retorno.setErro(
                    new ErrorDTO.Builder()
                    .tipoErro(ErroEnum.API)
                    .mensagem(MessageUtil.getMessage(ErroEnum.API.getValue()))
                    .build());
        } catch(HttpMethodException e) {
            retorno.setErro(
                    new ErrorDTO.Builder().tipoErro(e.getTipoErro())
                    .mensagem(e.getMessage())
                    .build());
        }

        return retorno;
    }

    public static List<String[]> criarHeader() {
        List<String[]> header = new ArrayList<>();
        //header.add(new String[]{"", ""});
        return header;
    }

    @Override
    protected String doInBackground(String... strings) {
        return null;
    }
}
