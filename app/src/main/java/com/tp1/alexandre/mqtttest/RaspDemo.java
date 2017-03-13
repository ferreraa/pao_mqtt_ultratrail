package com.tp1.alexandre.mqtttest;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by alexandre on 05/03/17.
 */

public class RaspDemo extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("depart","je commence");
        SystemClock.sleep(1000);


        PahoMqttSubscribe pms = new PahoMqttSubscribe(this);

        pms.start();



        Log.i("continuing","j'ai lancé psub");


 //       PahoMqttPublish pmp = new PahoMqttPublish(this);
  //      pmp.start();



        try {
            pms.join();
  //          pmp.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("finishing","J'ai terminé");



    }

}
