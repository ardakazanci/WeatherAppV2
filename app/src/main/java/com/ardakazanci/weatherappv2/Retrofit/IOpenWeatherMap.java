package com.ardakazanci.weatherappv2.Retrofit;

import com.ardakazanci.weatherappv2.Model.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * API ' den talep edebileceğimiz Yöntemler burada tanımlanacak
 */
public interface IOpenWeatherMap {

    /*
     weather endpoint'ine aşağıda yer alan query ' leri göndererek json result alacağız.
     */
    @GET("weather")
    Observable<WeatherResult> getWeatherByLatLng(@Query("lat") String lat,
                                  @Query("lon") String lng,
                                  @Query("appid") String appid,
                                  @Query("units") String unit);

}
