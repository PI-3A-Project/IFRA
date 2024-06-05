package br.com.ifra.service;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import br.com.ifra.data.model.dto.ReturnDTO;
import br.com.ifra.data.model.dto.erro.ErrorDTO;
import br.com.ifra.data.model.dto.volume.ListVolumeDTO;
import br.com.ifra.data.serializer.GsonCustomDeserializer;
import br.com.ifra.enums.ErroEnum;
import br.com.ifra.enums.Metodo;
import br.com.ifra.exceptions.HttpMethodException;
import br.com.ifra.utils.MessageUtil;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Service extends AsyncTask<String, Void, String> {

    private static Locale locale;
    private static final String baseUrl = "https://www.googleapis.com/books/v1/";
    public static ReturnDTO<ListVolumeDTO> buscarVolume(String search) {
        ReturnDTO<ListVolumeDTO> retorno = new ReturnDTO<>();
        String url = baseUrl + "volumes";
        String filter = "?q={"+ search +"}";
        url += filter;
        try {
            String response = new HttpService(url, null, null, Metodo.GET).execute().get();;

            /*Ignorar esse GsonCustomDeserializer, utilizar a transformação do gson normal, para isso é necessário alterar o DTO,
                as annotations não são necessárias e a estrutura do DTO tem que ficar igual a do json, não é necessário ter todos os campos,
                só os que precisamos*/
            //Gson gson = new GsonBuilder().registerTypeAdapter(ListVolumeDTO.class, new GsonCustomDeserializer<>()).create();
            Gson gson = new Gson();
            ListVolumeDTO listaVolume = gson.fromJson(response, ListVolumeDTO.class);
            retorno.setRetorno(listaVolume);
            retorno.setSucesso(true);
        } catch (ExecutionException e) {
            String erro = e.getMessage();
            return null;
        } catch (InterruptedException e) {
            String erro = e.getMessage();
            return null;
        } catch (Exception e) {
            String erro = e.getMessage();
            return null;
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
