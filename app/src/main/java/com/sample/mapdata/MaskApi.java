package com.sample.mapdata;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface MaskApi {
    @Headers("Accept: application/json")
    @GET("/corona19-masks/v1/storesByGeo/json")
    Call<StoreSale> getStoresByGeo(@Query("lat") double lat, @Query("lng") double lng,@Query("m") int m);
    //위도 경도 반경 세가지 파라미터를 가지고 ,getStoresByGeo를 호출하면 그 결과로 StoreSale로 객체가 반환이 된다
}
//자바에서 해당 api를 호출하기 위한 인터페이스를 만든다.
