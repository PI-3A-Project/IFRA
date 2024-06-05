package br.com.ifra.service;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import br.com.ifra.enums.Metodo;

public class HttpService extends AsyncTask<String, Void, String> {


    private final Metodo metodo;
    private final String url;
    private final String body;
    private final List<String[]> headers;
    HttpService(String url, String body, List<String[]> headers, Metodo metodo) {
        this.url =url;
        this.body = body;
        this.headers = headers;
        this.metodo = metodo;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            // Cria a URL da requisição
            URL urlRequest = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) urlRequest.openConnection();
            connection.setRequestMethod(metodo.name());

            //Define os headers caso tenha
            if(headers != null){
                for (String[] header: headers) {
                    connection.setRequestProperty(header[0], header[1]);
                }
            }

            //Define o body caso tenha
            if (body != null && !body.isEmpty()) {
                // Permite a escrita do body
                connection.setDoOutput(true);

                // Escrever o body no OutputStream
                OutputStream os = connection.getOutputStream();
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
                os.close(); // Fecha o OutputStream
            }

            // Configura a requisição
            connection.setConnectTimeout(5000);

            // Faz a conexão
            connection.connect();

            // Lê a resposta da requisição
            StringBuilder resposta = new StringBuilder();
            Scanner scanner = new Scanner(urlRequest.openStream());
            while (scanner.hasNext()) {
                resposta.append(scanner.next());
            }
            scanner.close();

            return resposta.toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
