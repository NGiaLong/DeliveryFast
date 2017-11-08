package com.example.lungpanda.deliveryfast.api;

/**
 * Created by LungPanda on 10/12/2017.
 */
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static Retrofit retrofit = null;
    private static String mBaseUrl = "http://4adea3c2.ngrok.io";


    public static Retrofit retrofit () {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;

    }
    public static String getBaseUrl(){
        return mBaseUrl;
    }




}
