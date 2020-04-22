package com.sample.mapdata;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.http.Tag;

public class PushActivity extends FirebaseMessagingService {
    private static final String TAG = "FMS";
    public PushActivity() {


    }

    @Override
    public void onNewToken(String token) {//새로운 토큰확인 호출
        super.onNewToken(token);
        Log.e(TAG, "onNewToken호출됨 :" +token);//로그에 새로운 토큰 값 출력
    }

    public void onMessageReceived(RemoteMessage remoteMessage){

    }

}
