package com.susankya.ntbnse_library;

import android.app.Activity;
import android.content.Intent;
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
import com.susankya.model.Transaction;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnActivity extends AppCompatActivity {

    @BindView(R.id.scannedBookCode)EditText bookCodeEt;
    @BindView(R.id.button_scan_book)ImageButton scanBookButton;
    @BindView(R.id.scannedIdCode)TextView idCardNumberEt;
    @BindView(R.id.button_scan_card)ImageButton scanIDCardButton;
    @BindView(R.id.getDetails)Button getDetails;
    @BindView(R.id.transactionView)View transactionView;

    @BindView(R.id.book_name)TextView bookName;
    @BindView(R.id.member_name)TextView memberName;
    @BindView(R.id.issue_date)TextView issueDate;
    @BindView(R.id.fine_status)TextView fineStatus;
    @BindView(R.id.returnButton)Button returnBook;

    private View pressedView;
    private String rollNumber="",bookCode="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return);

        ButterKnife.bind(this);
        rollNumber=getIntent().getStringExtra("rollNumber");
        bookCode=getIntent().getStringExtra("bookCode");
        bookCodeEt.setText(bookCode);
        idCardNumberEt.setText(rollNumber);

        scanBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedView=view;
                IntentIntegrator integrator = new IntentIntegrator(ReturnActivity.this);
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();

            }
        });

        scanIDCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressedView=view;
                IntentIntegrator integrator = new IntentIntegrator(ReturnActivity.this);
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();
            }
        });

        getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String bookCode=bookCodeEt.getText().toString();
                String idCard=idCardNumberEt.getText().toString();
                if(!Utilities.isNetworkAvailable(ReturnActivity.this)){
                    Toast.makeText(ReturnActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                    JSONArray ja=new JSONArray();
                    try {
                        ja=new JSONArray(LocalStorage.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONObject job=new JSONObject();
                    try {
                        job.put("idCardNumber",idCard);
                        job.put("bookCode",bookCode);
                        job.put("issueDate",DateTime.now().toLocalDate().toString());
                        job.put("transactionType",Constants.TRAN_RETURN);
                        ja.put(job);
                        Log.d("TAG", "onClick: "+ja.toString());
                        LocalStorage.saveString(ja.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finish();
                    return;
                }

                if(!(bookCode.isEmpty()||idCard.isEmpty())){
                    ApiClient.m.api.getTransactionDetail(idCard,bookCode).enqueue(new Callback<Transaction>() {
                        @Override
                        public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                            final Transaction transaction=response.body();
                            try{
                                Log.d("TAG", "onResponse: "+response.body()+" "+transaction.getIssueDate().toString());

                                if(transaction!=null){
                                    transactionView.setVisibility(View.VISIBLE);
                                    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                                            .withLocale(Locale.ROOT)
                                            .withChronology(ISOChronology.getInstanceUTC());
                                    DateTime dt = formatter.parseDateTime(transaction.getIssueDate());
                                    String todaysDate=dt.toDate().toString();
                                    String localTime=dt.toLocalDate().toString();
                                    bookName.setText(transaction.getBookName()+"\n("+transaction.getBookCode()+")");
                                    memberName.setText(transaction.getMemberName()+"\n("+transaction.getRollNumber()+")");
                                    returnBook.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            ApiClient.m.api.returnBook(transaction.getId()+"").enqueue(new Callback<ServerResponse>() {
                                                @Override
                                                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                                    ServerResponse sr=response.body();
                                                    if(sr.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(),"Book Returned",Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }else{

                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<ServerResponse> call, Throwable t) {

                                                }
                                            });
                                        }
                                    });
                                    issueDate.setText("Issued On: \n"+localTime);
                                    if(transaction.getRemainingDays()!=0){
                                        fineStatus.setText("Remaining Day\n:"+transaction.getRemainingDays());
                                        fineStatus.setTextColor(getResources().getColor(R.color.green));

                                    }else if (transaction.getFine()!=0)
                                    {
                                        fineStatus.setText("Fine \n Rs " +transaction.getFine());
                                        fineStatus.setTextColor(getResources().getColor(R.color.red));
                                    }
                                }
                            }
                        catch(Exception e){
                                e.printStackTrace();
                            Toast.makeText(getApplicationContext(),transaction.getResult(),Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Transaction> call, Throwable t) {

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
