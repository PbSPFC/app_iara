package com.example.pb.appiara;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pb.appiara.entity.Medicao;
import com.example.pb.appiara.util.MedicaoUtil;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Float.parseFloat;

public class MainActivity extends AppCompatActivity {

    public static Context context;
    public static Context getContext(){
        return MainActivity.context;
    }

    @Bind(R.id.tv_leituras)
    TextView tvLeitura;

    @Bind(R.id.tv_turbidez)
    TextView tvTurbidez;

    @Bind(R.id.tv_temperatura)
    TextView tvTemperatura;

    @Bind(R.id.tv_luminosidade)
    TextView tvLuminosidade;

    @Bind(R.id.tv_latitude)
    TextView tvLatitude;

    @Bind(R.id.tv_longitude)
    TextView tvLongitude;

    @Bind(R.id.tv_data)
    TextView tvData;

    @Bind(R.id.tv_cronometro)
    TextView tvCronometro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try{

        }catch(Exception e){
            Log.e("menu", e.getMessage());
            Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
        }


    }




    @OnClick(R.id.bt_medicao)
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


            /*boolean apito = true;

            do{
                Medicao medicao = this.medicaoRandom();
                this.escreverDados(medicao);

                if(medicao.getTemperatura() >= 25){
                    apito = false;
                    Toast.makeText(this, "Temperatura alta!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Escaneando!", Toast.LENGTH_SHORT).show();
                }

            }while(apito);*/


        }catch (Exception e){
            Log.e("menu", e.getMessage());
            Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
        }

    }

    public void escreverDados(Medicao medicao){
        tvLeitura.setText(medicao.getLeitura().toString());
        tvTurbidez.setText(medicao.getTurbidez().toString());
        tvTemperatura.setText(medicao.getTemperatura().toString());
        tvLuminosidade.setText(medicao.getLuminosidade().toString());
        tvLatitude.setText(medicao.getLatitude().toString());
        tvLongitude.setText(medicao.getLongitude().toString());
        tvData.setText(medicao.getData().toString());
        tvCronometro.setText(medicao.getCronometro().toString());
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

}
