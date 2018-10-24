package com.susankya.ntbnse_library;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IssueActivity extends AppCompatActivity {


    @BindView(R.id.scannedBookCode)EditText bookCodeEt;
    @BindView(R.id.button_scan_book)ImageButton scanBookButton;
    @BindView(R.id.scannedIdCode)TextView idCardNumberEt;
    @BindView(R.id.button_scan_card)ImageButton scanIDCardButton;
    @BindView(R.id.issueButton)Button issueButton;


    private View pressedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue);

        ButterKnife.bind(this);
        scanBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedView=view;
                IntentIntegrator integrator = new IntentIntegrator(IssueActivity.this);
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();

            }
        });
        scanIDCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedView=view;
                IntentIntegrator integrator = new IntentIntegrator(IssueActivity.this);

                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();
            }
        });

        issueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bookCode=bookCodeEt.getText().toString();
                String idCard=idCardNumberEt.getText().toString();
                DateTime dateTime=new DateTime();
                Log.d("TAG", "onClick: "+dateTime.toString());
                String timeNow=dateTime.toString();
                if(!Utilities.isNetworkAvailable(IssueActivity.this)){
                    Toast.makeText(IssueActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    JSONArray ja=new JSONArray();
                    try {
                       ja=new JSONArray(LocalStorage.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject job=new JSONObject();
                    try {
                        job.put("issueDate",DateTime.now().toLocalDate().toString());
                        Log.d("TAG", "onClick: "+DateTime.now().toLocalDate().toString());
                        job.put("idCardNumber",idCard);
                        job.put("bookCode",bookCode);
                        job.put("transactionType",Constants.TRAN_ISSUE);
                        ja.put(job);
                        Log.d("TAG", "onClick: "+ja.toString());
                        LocalStorage.saveString(ja.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                    return;
                }

                Log.d("TAG", "onClick: "+timeNow);
                if(!(bookCode.isEmpty()||idCard.isEmpty())){
                    ApiClient.m.api.issueBook(idCard,bookCode,timeNow).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            ServerResponse serverResponse =response.body();
                            Log.d("TAG", "onResponse: "+ response.body());

                            try {
                                Log.d("TAG", "onResponse: "+ serverResponse.getResult());

                            if(serverResponse.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Book Issued",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(getApplication(), serverResponse.getResult(),Toast.LENGTH_SHORT).show();
                            }
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(),"Something went wrong, Please check the data.",Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {

                        }
                    });
                }
                else{
                    Snackbar.make(view,"Please scan book and id card",Snackbar.LENGTH_SHORT).show();
                }
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("onActivityResult", "onActivityResult: ."+requestCode);
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            switch (pressedView.getId()){
                case R.id.button_scan_card:
                    idCardNumberEt.setText(re);
                    break;
                case R.id.button_scan_book:
                    bookCodeEt.setText(re);
                    break;
            }
            Toast.makeText(getApplicationContext(),re,Toast.LENGTH_SHORT).show();

            Log.d("onActivityResult", "onActivityResult: ." + re);

        }
        // else continue with any other code you need in the method
        //this.finish();

    }
}
