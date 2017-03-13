package com.tp1.alexandre.mqtttest;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

import java.util.UUID;


/**
 * Created by alexandre on 05/03/17.
 */

public class PahoMqttSubscribe extends Thread implements MqttCallback  {

    private static final String LOGTAG = "Subscribe";
    private Context context;

    MqttAndroidClient client;

    public PahoMqttSubscribe(Context context) {
        this.context = context;
    }

    /**
     * This method is called when the connection to the server is lost.
     *
     * @param cause the reason behind the loss of connection.
     */
    @Override
    public void connectionLost(Throwable cause) {
        Log.i(LOGTAG,"connectionLost : "+cause.toString());
    }

    /**
     * This method is called when a message arrives from the server.
     * <p>
     * <p>
     * This method is invoked synchronously by the MQTT client. An
     * acknowledgment is not sent back to the server until this
     * method returns cleanly.</p>
     * <p>
     * If an implementation of this method throws an <code>Exception</code>, then the
     * client will be shut down.  When the client is next re-connected, any QoS
     * 1 or 2 messages will be redelivered by the server.</p>
     * <p>
     * Any additional messages which arrive while an
     * implementation of this method is running, will build up in memory, and
     * will then back up on the network.</p>
     * <p>
     * If an application needs to persist data, then it
     * should ensure the data is persisted prior to returning from this method, as
     * after returning from this method, the message is considered to have been
     * delivered, and will not be reproducible.</p>
     * <p>
     * It is possible to send a new message within an implementation of this callback
     * (for example, a response to this message), but the implementation must not
     * disconnect the client, as it will be impossible to send an acknowledgment for
     * the message being processed, and a deadlock will occur.</p>
     *
     * @param topic   name of the topic on the message was published to
     * @param message the actual message.
     * @throws Exception if a terminal error has occurred, and the client should be
     *                   shut down.
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i(LOGTAG, "message reçu : "+message.toString());
    }

    /**
     * Called when delivery for a message has been completed, and all
     * acknowledgments have been received. For QoS 0 messages it is
     * called once the message has been handed to the network for
     * delivery. For QoS 1 it is called when PUBACK is received and
     * for QoS 2 when PUBCOMP is received. The token will be the same
     * token as that returned when the message was published.
     *
     * @param token the delivery token associated with the message.
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    public void doDemoSubscribe() {
        try {
            client = new MqttAndroidClient(context, "tcp://m2m.eclipse.org:1883", UUID.randomUUID().toString());
            Log.i(LOGTAG,"Client created");
            Log.i(LOGTAG, "je suis : "+client.getClientId());

            client.setCallback(this);


            client.connect(null, new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken mqttToken) {

                    Log.i(LOGTAG,"connected! client = "+client.toString());

                    Log.i(LOGTAG, "Topics=" + mqttToken.getTopics());


                    Log.i(LOGTAG, "Topics=" + mqttToken.getTopics());

                    MqttMessage message = new MqttMessage("Hello, I am the first step of a long journey".getBytes());
                    message.setQos(1);
                    message.setRetained(true);


                    try {
                        client.publish("messagesaaa", message);

                        client.subscribe("messagesaaa",2);
                        Log.i(LOGTAG,"subscribed");


                        while (true) {
                            Log.i(LOGTAG,"still there");
                            message = new MqttMessage("Je suis dans le while".getBytes());
                            message.setQos(1);
                            message.setRetained(true);


                            try {
                                client.publish("messagesaaa",message);
                                Log.i(LOGTAG, "Message published");
                            } catch (MqttException e) {
                                e.printStackTrace();
                                client.disconnect();
                                Log.i(LOGTAG, "client disconnected : "+e.toString());

                            }

                            try { Thread.sleep (15000); } catch (InterruptedException e) {client.disconnect();}
                        }



                    } catch (MqttPersistenceException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (MqttException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken arg0, Throwable arg1) {
                    // TODO Auto-generated method stub
                    Log.i(LOGTAG, "Client connection failed: " + arg1.getMessage());

                }
            });




            // We’ll now idle here sleeping, but your app can be busy
            // working here instead

        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        Log.i(LOGTAG,"STARTED");

        doDemoSubscribe();
    }
}
