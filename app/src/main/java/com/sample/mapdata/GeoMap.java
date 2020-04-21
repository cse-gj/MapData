/*package com.sample.mapdata;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.IOException;
import java.util.List;

public class GeoMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource; // 현재 액티비티를 켤 때 받아올 위치정보를 저장
    LocationManager locationManager;

    NaverMap mnaverMap;
    com.naver.maps.map.overlay.Marker marker;
    CameraPosition cameraPosition;

    private Geocoder geocoder;
    private TextView tvAddress;
    private TextView tvLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding_map);

        tvAddress = (TextView) findViewById(R.id.tvAdress);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        marker = new Marker();

        // 위치 정보 받아오기
        final int loactionpermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);//위치권한
        if (loactionpermissionCheck == PackageManager.PERMISSION_DENIED) {  // 권한거부 되있다면
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //권한허용창
        }

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, //위치제공자
                0, //최소시간간격
                0,   //최소반경거리
                mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, //위치제공자
                0, //최소시간간격
                0,   //최소반경거리
                mLocationListener);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//gps 켜져있는지 체크
            //gps설정화면으로이동
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intent);
        }

        // 주소 불러오기
        geocoder = new Geocoder(this);

        // 지도 객체 받아오기
        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.zoompos_map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.zoompos_map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        mnaverMap = naverMap;
        updateMap(new LatLng(0,0));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        Toast.makeText(this, "gps권한을 승인하셨습니다.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //콜백 메소드
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //위치값 갱신시 이벤트발생
            //값은 location 형태로 리턴

            double latitude = location.getLatitude(); //위도
            double longitude = location.getLongitude(); //경도
            double altitude = location.getAltitude(); //고도
            float accuracy = location.getAccuracy(); //정확도
            String provider = location.getProvider(); //위치제공자
            // gps가 network위치보다 정확도가 좋다

            updateMap(new LatLng(latitude,longitude));
            getAddress(new LatLng(latitude,longitude));
            tvLocation.setText("좌표 : " + "(" + latitude + "," + longitude+")");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //disable시
        }

        @Override
        public void onProviderEnabled(String provider) {
            //Enable시
        }

        @Override
        public void onProviderDisabled(String provider) {
            //변경시
        }
    };

    public void updateMap(LatLng latLng) {
        //오버레이 표시
        marker.setPosition(latLng);
        marker.setMap(mnaverMap);

        // 카메라 위치와 줌 조절 (숫자가 클수록 확대)
        cameraPosition = new CameraPosition(latLng, 17);
        mnaverMap.setCameraPosition(cameraPosition);
    }

    private void getAddress(LatLng latLng) {

        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(latLng.latitude,latLng.longitude,10); // 얻어올 값의 개수
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("getAddress", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size() == 0) {
                tvAddress.setText("주소 : 해당지역의 주소 정보가 없습니다.");
            } else {
                tvAddress.setText("주소 : " + list.get(0).getAddressLine(0).toString());
            }
        }
    }

}
*/