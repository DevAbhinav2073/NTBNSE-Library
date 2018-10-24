package com.susankya.ntbnse_library;

/**
 * Created by abhinav on 12/1/17.
 */

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import net.danlew.android.joda.JodaTimeAndroid;

import java.io.IOException;
import java.security.SecureRandom;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

public class ApiClient extends Application {
    public static Retrofit retrofit = null;
    public static ApiClient m;
    public Bitmap zoomedImage;
    public MySharedPreferences sharedPref;
    public static android.support.v4.app.FragmentManager fm;
    public static Retrofit getClient(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }

    public  static OkHttpClient client;
    LibraryApi api;

    public void restartClientForToken()
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        client=httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", "Token "+sharedPref.getToken()); // <-- this is the important line
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).addInterceptor(new CustomInterceptor()).build();

        retrofit=getClient(Constants.BASE_URL);
        m.api=retrofit.create(LibraryApi.class);
    }

    public static Retrofit getClientless(String baseUrl)
    {
        OkHttpClient.Builder client=new OkHttpClient.Builder();
        client.addInterceptor(new CustomInterceptor());
        OkHttpClient cl=client.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(cl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static void tokenLessClient()
    {
        retrofit=getClientless(Constants.BASE_URL);
       ApiClient.m.api=retrofit.create(LibraryApi.class);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);

        m=ApiClient.this;
        sharedPref=new MySharedPreferences(getApplicationContext(),Constants.BASIC_SHAREDPREF);

        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);
//     if (!sharedPref.isAdminSignedIn())
        if(!sharedPref.isAdminSignedIn())
        {
            tokenLessClient();
            Log.d(TAG, "onCreate: called");
        }
        else {
            restartClientForToken();
            //Toast.makeText(this,"this",Toast.LENGTH_SHORT).show();
        }
    }

    public static String s(int resource,Context c)
    {
        return c.getApplicationContext().getResources().getString(resource);
    }
}
