package com.susankya.ntbnse_library;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.susankya.model.Book;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySharedPreferences myPref=new MySharedPreferences(this,Constants.BASIC_SHAREDPREF);

        if(myPref.isUserSignedIn())
            getSupportFragmentManager().beginTransaction().add(R.id.frame,new UserTabsFragment()).commit();
        else if(myPref.isAdminSignedIn())
            getSupportFragmentManager().beginTransaction().add(R.id.frame,new AdminTabsFragment()).commit();
        else
            getSupportFragmentManager().beginTransaction().add(R.id.frame,new UserLoginFragment()).commit();
/*
       ApiClient.m.api.search("Engineering mathematics I").enqueue(new Callback<List<Book>>() {
              @Override
           public void onResponse(Call<List<Book>> call, retrofit2.Response<List<Book>> response) {
               List<Book> Book=response.body();
               Log.d("TAG", "onResponse: "+Book.size()+response.raw().body().toString()+"   "+Book.get(0).getRemaining());
                  Log.d("TAG", "onResponse: "+response.body());
              }

           @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.toString());
            }
        });
        /*IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScannerActivity.class);
        integrator.initiateScan();*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("onActivityResult", "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            Toast.makeText(getApplicationContext(),re,Toast.LENGTH_SHORT).show();

            Log.d("onActivityResult", "onActivityResult: ." + re);

        }
        // else continue with any other code you need in the method
        //this.finish();

    }
}
