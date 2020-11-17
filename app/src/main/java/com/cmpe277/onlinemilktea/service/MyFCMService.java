package com.cmpe277.onlinemilktea.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cmpe277.onlinemilktea.Common.Common;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Random;

public class MyFCMService extends FirebaseMessagingService {
    public MyFCMService() {
    }

    final String TAG = "MyToken new";


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("New token", s);
        Common.updateToken(this,s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        System.out.print("From: " + remoteMessage.getFrom());
        Map<String, String> dataRecv = remoteMessage.getData();

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

        }

        if (dataRecv != null) {
            Common.showNotification(this, new Random().nextInt(),
                    dataRecv.get(Common.NOTIF_TITLE),
                    dataRecv.get(Common.NOTIF_CONTENT),
                    null);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }



}