/*package com.sample.mapdata;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Overlay;

public class Marker extends AppCompatActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_map);

        // 지도 객체 받아오기
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.marker_map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.marker_map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        //마커 클릭 리스너
        Overlay.OnClickListener listener = new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) {
                overlay.setMap(null);   //마커 삭제
                return true;
            }
        };

        // 마커 객체 생성
        Marker marker = new Marker();

        // 마커 객체를 지도에 표시
        marker.setMap(naverMap);

        // 마커 클릭 이벤트
        marker.setOnClickListener(listener);

        // 지도 클릭 이벤트
        naverMap.setOnMapClickListener((point, coord) -> {
            Marker m = new Marker(new LatLng(coord.latitude, coord.longitude));
            m.setMap(naverMap);
            m.setOnClickListener(listener);
        });
    }
}
*/
