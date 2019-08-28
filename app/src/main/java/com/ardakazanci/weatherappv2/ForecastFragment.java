package com.ardakazanci.weatherappv2;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ardakazanci.weatherappv2.Adapter.WeatherForecastAdapter;
import com.ardakazanci.weatherappv2.Common.Common;
import com.ardakazanci.weatherappv2.Model.FiveDays.WeatherForecastResult;
import com.ardakazanci.weatherappv2.R;
import com.ardakazanci.weatherappv2.Retrofit.IOpenWeatherMap;
import com.ardakazanci.weatherappv2.Retrofit.RetrofitClient;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

/**
 * 5 Günlük hava durumu tahmini
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mService;

    TextView txt_city_name, txt_geo_coord;
    RecyclerView recyclerView_forecast;


    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    static ForecastFragment instance;

    public static ForecastFragment getInstance() {
        if (instance == null)
            instance = new ForecastFragment();
        return instance;
    }


    public ForecastFragment() {

        compositeDisposable = new CompositeDisposable();
        Retrofit retrofit = RetrofitClient.getInstance();
        mService = retrofit.create(IOpenWeatherMap.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_forecast, container, false);

        txt_city_name = view.findViewById(R.id.txt_city_name);
        txt_geo_coord = view.findViewById(R.id.txt_geo_coord);
        recyclerView_forecast = view.findViewById(R.id.recyclerview_forecast);
        recyclerView_forecast.setHasFixedSize(true);
        recyclerView_forecast.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        getForecastWeatherInformation();

        return view;
    }

    private void getForecastWeatherInformation() {

        compositeDisposable.add(mService.getForecastWeatherByLatLng(

                String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.APP_ID,
                "metric"


                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WeatherForecastResult>() {
                            @Override
                            public void accept(WeatherForecastResult weatherForecastResult) throws Exception {

                                displayForecastWeather(weatherForecastResult);

                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                                Log.d("ForecastFragment", "" + throwable.getMessage());

                            }
                        })


        );

    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {

        txt_city_name.setText(new StringBuilder(weatherForecastResult.city.name));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));

        WeatherForecastAdapter adapter = new WeatherForecastAdapter(getContext(), weatherForecastResult);
        recyclerView_forecast.setAdapter(adapter);

    }

}
