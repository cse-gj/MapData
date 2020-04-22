package com.sample.mapdata;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

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

    public void onMessageReceived(RemoteMessage remoteMessage){//메세지 수신시 일단 공백

        String from = remoteMessage.getFrom();//발신자확인
        Map<String, String> data = remoteMessage.getData();//메세지 안의 데이터 반환 map객체 안에 넣음
        String contents = data.get("contents");//발신 데이터 확인
    }

}
