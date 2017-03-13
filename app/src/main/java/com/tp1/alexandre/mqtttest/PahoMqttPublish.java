package com.tp1.alexandre.mqtttest;

import android.content.Context;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;

/**
 * Created by alexandre on 05/03/17.
 */

public class PahoMqttPublish extends Thread {


    private static final String LOGTAG = "Publish";
    MqttAndroidClient client;

    Context context;

    public PahoMqttPublish(Context context) {
        this.context = context;
    }

    public void doDemoPublish() {


        try {
            client = new MqttAndroidClient(context, "tcp://m2m.eclipse.org:1883", UUID.randomUUID().toString());
            Log.i(LOGTAG,"Client created");
            Log.i(LOGTAG, "je suis : "+client.getClientId());

            client.connect(null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken mqttToken) {

                    Log.i(LOGTAG, "Connected!");

                    Log.i(LOGTAG, "Topics=" + mqttToken.getTopics());

                    MqttMessage message = new MqttMessage("Hello, I am the first step of a long journey".getBytes());
                    message.setQos(2);
                    message.setRetained(false);

                    try {
                        client.publish("messages", message);



                        while(true) {
                            Thread.sleep(15000);
                            message = new MqttMessage("Je suis dans le while".getBytes());
                            message.setQos(2);
                            message.setRetained(false);

                            Log.i(LOGTAG, "Message published");

                            try {
                                client.publish("messages",message);
                            } catch (MqttException e) {
                                e.printStackTrace();
                                client.disconnect();
                                Log.i(LOGTAG, "client disconnected : "+e.toString());

                            }
                        }



                    } catch (MqttPersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (MqttException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    // TODO Auto-generated method stub
                    Log.i(LOGTAG, "Client connection failed: " + arg1.getMessage());

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void run() {
        Log.i(LOGTAG,"STARTED");

        doDemoPublish();
    }
}
