package br.pucminas.webservice;

import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    // DECLARAÇÃO DOS OBJETOS VISUAIS
    private EditText edtCep;
    private TextInputEditText edtLogradouro;
    private TextInputEditText edtComplemento;
    private TextInputEditText edtBairro;
    private TextInputEditText edtCidade;
    private TextInputEditText edtUF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // REFERÊNCIA AOS OBJETOS VISUAIS USANDO A CLASSE R
        edtCep = findViewById(R.id.edtCepId);
        edtLogradouro = findViewById(R.id.edtLogradouroId);
        edtComplemento = findViewById(R.id.edtComplementoId);
        edtBairro = findViewById(R.id.edtBairroId);
        edtCidade = findViewById(R.id.edtCidadeId);
        edtUF = findViewById(R.id.edtUFId);
    }

    // EXIBE UMA MENSAGEM TOAST PARA O USUÁRIO
    public void print(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    // PROCEDIMENTO PARA EXECUTAR O ONCLICK DO BOTÃO
    public void onClickPesquisar(View view){

        String cep = edtCep.getText().toString();

        if(cep == null || cep.equals("")){
            print("Obrigatório informar o CEP!");
        }else {

            WebServiceEndereco webServiceEndereco = new WebServiceEndereco();
            webServiceEndereco.execute(cep);
        }
    }

    // CLASSE PARA EXECUTA AsyncTask
    public class WebServiceEndereco extends AsyncTask<String, Void, String> {

        // MÉTODO QUE FAZ A REQUISIÇÃO HTTP
        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL("https://viacep.com.br/ws/" + strings[0] + "/json/");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String linha;
                StringBuffer buffer = new StringBuffer();
                while((linha = reader.readLine()) != null) {
                    buffer.append(linha);
                    buffer.append("\n");
                }

                return buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return null;
        }

        // MÉTODO QUE CONFIGURA A RESPOSTA DO MÉTODO HTTP
        @Override
        protected void onPostExecute(String s) {

            if(s == null)
                print("Não foi possível recuperar os dados...");
            else {
                try {

                    JSONObject json = new JSONObject(s);

                    edtLogradouro.setText(json.getString("logradouro"));
                    edtComplemento.setText(json.getString("complemento"));
                    edtBairro.setText(json.getString("bairro"));
                    edtCidade.setText(json.getString("localidade"));
                    edtUF.setText(json.getString("uf"));

                    print("Endereço recuperado com sucesso!");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class WebAPI extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
