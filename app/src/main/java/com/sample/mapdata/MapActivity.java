package com.sample.mapdata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapActivity extends AppCompatActivity implements NaverMap.OnMapClickListener,Overlay.OnClickListener, OnMapReadyCallback, NaverMap.OnCameraChangeListener, NaverMap.OnCameraIdleListener{
    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;

    private NaverMap naverMap;
    Marker marker;
    CameraPosition cameraPosition;
    private InfoWindow infoWindow;// 인포 윈도우에 대한 레퍼런스 변수
    private FusedLocationSource locationSource;
    private List<Marker> markerList = new ArrayList<Marker>();
    private boolean isCameraAnimated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 위치 정보 받아오기
        final int loactionpermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//위치권한
        if (loactionpermissionCheck == PackageManager.PERMISSION_DENIED) {  // 권한거부 되있다면
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //권한허용창
        }
    }

    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);// 사용자가 gps를 허가 하게 끔 뭍기
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        naverMap.addOnCameraChangeListener(this);// 카메라 이동을 감지하는 리스
        naverMap.addOnCameraIdleListener(this);// 지도를 드래그 해서 멈췄을 때 알려주는 것
        naverMap.setOnMapClickListener(this);// 지도 어딘가를 클릭 했을 때 리스너로 호출
        naverMap.setLocationTrackingMode(LocationTrackingMode.Face); // 위치를 추적하면서 카메라의 좌표와 베어링도 따라 움직이는 모드
        naverMap.setIndoorEnabled(true);//  실내지도 활성화 여부를 지정합니다.
        naverMap.setBuildingHeight(0.5f);// 건물 높이

        LatLng mapCenter = naverMap.getCameraPosition().target;//지도상의서의 중심점을 얻을 수가 있다.
        fetchStoreSale(mapCenter.latitude, mapCenter.longitude, 3000);
        //위도와 경도가 담겨져 있다.
        // 반경 3000m 이내에 있는 것을 조회가 가능합니다.

        infoWindow = new InfoWindow();// 객체, 마커 클릭
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(this) {// 간단하게 표시하
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();//연결된 마커를 가지고 올 수 있다.
                Store store = (Store) marker.getTag();
                return store.name+"\n"+store.addr;//store 객체 가져오기
            }
        });

        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                //2차 배열 for문
                Marker marker = infoWindow.getMarker();
                Store store = (Store) marker.getTag();//가져온 Store정보를 가지고 오기
                View view = View.inflate(MapActivity.this, R.layout.view_info_window, null);//xml 파일 가지고 뷰를 인포메이션 하면 뷰가 만들어 진다.
                ((TextView) view.findViewById(R.id.name)).setText(store.name);//store에 네임을 반환해준다.

                if ("plenty".equalsIgnoreCase(store.remain_stat)) {
                    ((TextView) view.findViewById(R.id.stock)).setText("100개 이상");//텍스트뷰를 가지고 온다.
                } else if ("some".equalsIgnoreCase(store.remain_stat)) {
                    ((TextView) view.findViewById(R.id.stock)).setText("30개 이상 100개 미만");
                } else if ("few".equalsIgnoreCase(store.remain_stat)) {
                    ((TextView) view.findViewById(R.id.stock)).setText("2개 이상 30개 미만");
                } else if ("empty".equalsIgnoreCase(store.remain_stat)) {
                    ((TextView) view.findViewById(R.id.stock)).setText("1개 이하");
                } else if ("break".equalsIgnoreCase(store.remain_stat)) {
                    ((TextView) view.findViewById(R.id.stock)).setText("판매중지");
                } else {
                    ((TextView) view.findViewById(R.id.stock)).setText(null);
                }
                ((TextView) view.findViewById(R.id.time)).setText("입고 " + store.stock_at);// 입고 시간을 가져다 준다.
                return view;//말 풍선 안에 실행이 된다.
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }
    @Override
    public void onCameraChange(int reason, boolean animated) {// 파라미터로 애니메이션 유/무 확인
        isCameraAnimated = animated;// 마지막 상태를 가지고 있으며
    }

    @Override
    public void onCameraIdle() {//지도의 이동이 멈췄을
        if (isCameraAnimated) {
            LatLng mapCenter = naverMap.getCameraPosition().target;//카메라 포지션을 멈춰서 그 포지션을 가지
            fetchStoreSale(mapCenter.latitude, mapCenter.longitude, 5000);// 화면에 가지고 온다.
        }
    }

    private void fetchStoreSale(double lat, double lng, int m) { //레트로핏 api를 통해서 호출을 하게 된다.
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://8oi9s0nnth.apigw.ntruss.com").addConverterFactory(GsonConverterFactory.create()).build();
        // 해당 api를 제공하고 있는 도메인, 전달된 자바 객체를 두개를 이용해서 retro api를 생성해준다.
        MaskApi maskApi = retrofit.create(MaskApi.class);//자바 객체를 생성합니다.
        maskApi.getStoresByGeo(lat, lng, m).enqueue(new Callback<StoreSale>() {//StoreSale라는 api를 구현
            //async라는 것은 callback으로 전달 받습니다.
            @Override
            public void onResponse(Call<StoreSale> call, Response<StoreSale> response) {
                if (response.code() == 200) {// 호출이 성공적이 었을 때, 200은 확인 요
                    StoreSale result = response.body();
                    updateMapMarkers(result);// 지도상의 표시를 해줍니다.
                }
            }
            @Override
            public void onFailure(Call<StoreSale> call, Throwable t) {// 호출이 실패를 하였을 경우

            }
        });
    }
    private void updateMapMarkers(StoreSale result) {
        resetMarkerList();
        if (result.stores != null && result.stores.size() > 0) {// 판매처가 하나 이상 반환이 된 경우에는
            for (Store store : result.stores) {//판매처 갯수 만큼 마커를 개수로 돌려줍니다.
                Marker marker = new Marker();// 마커의 위치
                marker.setTag(store);//store정보를 태깅합니다.
                marker.setPosition(new LatLng(store.lat, store.lng));// 어디에 전달되는지를 마커에 위치를 설
                if ("plenty".equalsIgnoreCase(store.remain_stat)) {// 재고 현황 충분
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_green));
                } else if ("some".equalsIgnoreCase(store.remain_stat)) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_yellow));
                } else if ("few".equalsIgnoreCase(store.remain_stat)) {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_red));
                } else {
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_gray));
                }
                marker.setAnchor(new PointF(0.5f, 1.0f));//x좌표 ,y좌표
                marker.setMap(naverMap);// 표시를 하고자 하는 지도를 나타내준다.
                marker.setOnClickListener(this);
                markerList.add(marker);// 마커를 저장해 두면
            }
        }
    }
    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        if (infoWindow.getMarker() != null) { //인포 윈도우의 마커가 연결이 되어 있으면
            infoWindow.close();//그러면 닫는다.
        }
    }

    @Override
    public boolean onClick(@NonNull Overlay overlay) {
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            if (marker.getInfoWindow() != null) {// 인포 윈도우에 마커가 떠 있다면
                infoWindow.close();// 닫힌다.
            } else {
                infoWindow.open(marker);
            }
            return true;
        }
        return false;
    }
    private void resetMarkerList() { //마커에 저장해
        if (markerList != null && markerList.size() > 0) {
            for (Marker marker : markerList) {//리스트를 따라다니면서
                marker.setMap(null);// 마커가 표시되어야 할 지도를 null로 표시해줍니다.
            }
            markerList.clear();// 가져온 마커를 지도상에서 지워줍니다.
        }
    }
}


