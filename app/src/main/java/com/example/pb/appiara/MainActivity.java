package com.example.pb.appiara;

import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.MainThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pb.appiara.entity.Medicao;
import com.example.pb.appiara.util.MedicaoUtil;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter BTAdapter; // declara o adaptador BT
    public static int REQUISITA_BLUETOOTH = 1; // define o parâmetro de busco, o número é aleatório
    List<String> listaEnderecosBT; // declara uma lista de endereços BT, que será usado para conectar e ouvir a serial
    List<BluetoothDevice> listaPareados; // declara uma lista dos dispositivos BT pareados
    // declara um UUID para este aplicativo
    private static final UUID meuUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    // declara os elementos de tela
    ListView listaAparelhosBT;
    //TextView tvMensagem;

    public static Context context;
    public static Context getContext(){
        return MainActivity.context;
    }

    @Bind(R.id.tv_temperatura)
    TextView tvTemperatura;

    @Bind(R.id.tv_luminosidade)
    TextView tvLuminosidade;

    @Bind(R.id.tv_latitude)
    TextView tvLatitude;

    @Bind(R.id.tv_data)
    TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



            // associa os campos de tela que são usados
            listaAparelhosBT = (ListView)findViewById(R.id.listBTDevices);
            //tvMensagem = (TextView)findViewById(R.id.tvMensagem);

            // cria o evento onItemClickListener na ListView
            listaAparelhosBT.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> pai, View v, int pos, long posL){
                            conectarAparelhoSelecionado(pos, posL);
                        }
                    }
            );

            try{
            // associa o adaptador BT do telefone
            BTAdapter = BluetoothAdapter.getDefaultAdapter();
            // verifica se o telefone tem suporte a BT
            if(BTAdapter == null){
                new AlertDialog.Builder(this).setTitle("Incompatibilidade!")
                        .setMessage("Seu aparelho não tem suporte a Bluetooth")
                        .setPositiveButton("Sair", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which){
                                System.exit(0);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }// if(BTAdapter == null)

            // verifica se o Bluetooth está habilitado
            if(!BTAdapter.isEnabled()){
                // se não estiver, habilita o serviço Bluetooth
                Intent habilitaBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(habilitaBT, REQUISITA_BLUETOOTH);
            }
            // lista os dispositivos pareados
            listarPareados();

        }catch(Exception e){
            Log.e("menu", e.getMessage());
            Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
        }


    }




    /*@OnClick(R.id.bt_medicao)
    public void btMedicao(){
        try{

            Toast.makeText(this, "Escaneando!", Toast.LENGTH_SHORT).show();

            Thread t = new Thread() {

                @Override
                public void run() {
                    try {
                        List<Medicao> medicaoList = MedicaoUtil.gerarListaMedicao();
                        final Context context = MainActivity.getContext();
                        for (Medicao medicao : medicaoList) {
                            final Medicao med = medicao;
                            Thread.sleep(1000);
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                        escreverDados(med);
                                        if(med.getTemperatura() >= 25){
                                            Toast.makeText(MainActivity.this, "Temperatura alta!", Toast.LENGTH_SHORT).show();
                                            MainActivity.this.notifyThis("IARA", "Temperatura alta!");
                                        }else {

                                        }
                                    }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };

            t.start();


            boolean apito = true;

            do{
                Medicao medicao = this.medicaoRandom();
                this.escreverDados(medicao);

                if(medicao.getTemperatura() >= 25){
                    apito = false;
                    Toast.makeText(this, "Temperatura alta!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Escaneando!", Toast.LENGTH_SHORT).show();
                }

            }while(apito);


        }catch (Exception e){
            Log.e("menu", e.getMessage());
            Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
        }

    }*/

    public void escreverDados(Medicao medicao){
        tvTemperatura.setText(medicao.getTemperatura().toString());
        tvTemperatura.setBackgroundColor(Color.parseColor(MedicaoUtil.getTempColor(medicao)));

        tvLuminosidade.setText(medicao.getLuminosidade().toString());
        tvLuminosidade.setBackgroundColor(Color.parseColor(MedicaoUtil.getPHColor(medicao)));

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        tvLatitude.setText(df.format(medicao.getLatitude()).toString());
        tvLatitude.setBackgroundColor(Color.parseColor(MedicaoUtil.getCondColor(medicao)));

        tvData.setText(medicao.getData().toString() + " - " + medicao.getCronometro().toString());


    }

    public void notifyThis(String title, String message) {
        NotificationCompat.Builder b = new NotificationCompat.Builder(MainActivity.this);
        b.setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.email)
                .setTicker("{your tiny message}")
                .setContentTitle(title)
                .setContentText(message)
                .setContentInfo("INFO");

        NotificationManager nm = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(1, b.build());
    }

    // lista os dispositivos já pareados ao telefone
    private void listarPareados(){
        // lê a relação de dispositivos pareados
        Set<BluetoothDevice> pareados = BTAdapter.getBondedDevices();
        // verifica se há aparelhos pareados
        if(pareados.size()>0){
            // declara uma lista de nomes de dispositivos
            List<String> nomesAparelhos = new ArrayList<String>();
            listaPareados = new ArrayList<BluetoothDevice>();
            listaEnderecosBT = new ArrayList<String>();
            // lê os dispositivos pareados
            for(BluetoothDevice aparelho: pareados){
                nomesAparelhos.add(aparelho.getName() + " - Atualizar Medição");
                listaEnderecosBT.add(aparelho.getAddress());
                listaPareados.add(aparelho);
            }
            // adiciona os nomes dos dispositivos a uma lista
            listaAparelhosBT.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomesAparelhos));
        }
    }

    public void conectarAparelhoSelecionado(int pos, long posL){
        // verifica o item que está na posição selecionada
        String nome = listaAparelhosBT.getItemAtPosition(pos).toString();
        String btAddress = listaEnderecosBT.get(pos).toString();
        BluetoothDevice aparelho = listaPareados.get(pos);

        //tvMensagem.setText(nome + " - " + btAddress);

        ConectarAparelhoBT conectarBT = new ConectarAparelhoBT(aparelho, meuUUID);
        conectarBT.start();
    }

    // define a classe que estabelece a conexão com o dispositivo Bletooth
    private class ConectarAparelhoBT extends Thread {
        private BluetoothSocket mmSocket; // socket por a comunicação ocorre
        private BluetoothDevice mmAparelho; // id deste aparelho

        // este construtor recebe o aparelho que será conectado, e o UUID do meu aparelho
        public ConectarAparelhoBT(BluetoothDevice aparelho, UUID uuid){
            BluetoothSocket tmp = null;
            mmAparelho = aparelho;

            try{
                // cria o socket de comunicação
                mmSocket = mmAparelho.createRfcommSocketToServiceRecord(uuid);
            }catch(IOException ioe){
                ioe.printStackTrace();
                ioe.getMessage();
            }
        }

        @Override
        public void run(){
            try{
                // estabelece a conexão no socket que foi criado
                mmSocket.connect();
                // gerencia a conexão BT
                GerenciarConexaoBT gBT = new GerenciarConexaoBT(mmSocket);
                gBT.start();
            }catch(IOException ioe){
                try{
                    mmSocket.close();
                }catch(IOException closeException){
                    closeException.printStackTrace();
                    closeException.getMessage();
                }
                ioe.printStackTrace();
                ioe.getMessage();
            }
            return;
        }

        public void cancel(){
            try{
                mmSocket.close();
            }catch(IOException closeException){
                closeException.printStackTrace();
                closeException.getMessage();
            }
        }
    }

    // define a classe que gerencia a conexão via BT
    private class GerenciarConexaoBT extends Thread{
        // declara as varáveis que armazenam o socket e a stream de entrada
        private BluetoothSocket btSocketConectado;
        private InputStream streamEntrada;

        // declara o construtor que define o socket onde a comunicação cai ocorrer
        public GerenciarConexaoBT(BluetoothSocket bs){
            btSocketConectado = bs;
            streamEntrada = null;

            try{
                // lê os dados que vem pelo socket da comunicação serial
                streamEntrada = btSocketConectado.getInputStream();
            }catch(IOException ioe){
                ioe.getMessage();
                ioe.printStackTrace();
            }
        }

        @Override
        public void run(){
            byte[] buffer = new byte[1024];
            int bytes;

            while(true){
                try{
                    // declara uma janela de bytes
                    boolean tempFormat = true;
                    String regex = "(\\d+)(.*)";

                    // define a variável onde os bytes da comunicação serial forem descarregados
                    String textoRecebido = null;// = new String(buffer, 0, bytes);

                    while (tempFormat){
                        bytes = streamEntrada.read(buffer);
                        textoRecebido = new String(buffer, 0, bytes);
                        if((textoRecebido.contains(".") && textoRecebido.contains(","))
                                && textoRecebido.matches(regex)){
                            String[] parts = textoRecebido.split(",");
                            textoRecebido = parts[0];
                            tempFormat = false;
                        }
                    };

                    final String temp = textoRecebido;
                    // monta a string a ser mostrada
                    //final String mensagemRecebida = String.valueOf(bytes) +  " bytes recebidos:\n" + textoRecebido;

                    Medicao medicao = MedicaoUtil.medicaoRandom();
                    medicao.setTemperatura(Float.parseFloat(temp));
                    final Medicao med = medicao;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // exibe a mensagem
                            escreverDados(med);
                            tvTemperatura.setText(temp);
                            if(med.getTemperatura() >= 25){
                                Toast.makeText(MainActivity.this, "Temperatura alta!", Toast.LENGTH_SHORT).show();
                                MainActivity.this.notifyThis("IARA", "Temperatura alta!");
                            }else {

                            }
                        }
                    });

                }catch(IOException ioe){
                    ioe.getMessage();
                    ioe.printStackTrace();
                }
            }
        }

    }


}
