package com.susankya.ntbnse_library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.susankya.model.OfflineTransactionDetail;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by abhinav on 12/6/17.
 */

public class OfflineTransactionAdapter extends RecyclerView.Adapter<OfflineTransactionAdapter.MyViewHolder> {

    private List<OfflineTransactionDetail> offlineTransactionDetailList;
    Activity c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.idCardNumber)TextView roll;
        @BindView(R.id.bookCode)TextView bookCode;
        @BindView(R.id.issueReturn)Button issueReturn;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public OfflineTransactionAdapter(List<OfflineTransactionDetail> moviesList, Activity c) {
        this.offlineTransactionDetailList = moviesList;
        this.c=c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offline_transaction_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OfflineTransactionDetail transaction = offlineTransactionDetailList.get(position);
        /*SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        String date=dateFormatter.format(transaction.getIssueDate().toDate());*/

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
                .withLocale(Locale.ROOT)
                .withChronology(ISOChronology.getInstanceUTC());

        DateTime dt = formatter.parseDateTime(transaction.getTime());
        String todaysDate=dt.toDate().toString();
        String localTime=dt.toLocalDate().toString();
        int daysHavingBook= Days.daysBetween(dt.toLocalDate() ,DateTime.now().toLocalDate() ).getDays();
        Log.d(TAG, "onBindViewHolder: "+dt.toString()+" "+localTime);
        holder.roll.setText("Roll Number: "+transaction.getIdCardNumber());
        holder.bookCode.setText("Book Code: "+transaction.getBookCode());
        switch (transaction.getTransactonType()){
            case Constants.TRAN_ISSUE:
                holder.issueReturn.setText("Issue Book");
                break;
            case Constants.TRAN_RETURN:
                holder.issueReturn.setText("Return Book");
                break;
        }
        final String type=transaction.getTransactonType();
        holder.issueReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Utilities.isNetworkAvailable(c)){
                    Toast.makeText(c,"No internet connection",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(type.equals(Constants.TRAN_ISSUE)){
                    issueBook(transaction.getTime(),transaction.getBookCode(), transaction.getIdCardNumber(),position);
                }
                else  if(type.equals(Constants.TRAN_RETURN)){
                    Intent i=new Intent(c,ReturnActivity.class);
                    i.putExtra("rollNumber",transaction.getIdCardNumber());
                    i.putExtra("bookCode",transaction.getBookCode());
                    c.startActivity(i);

                }

            }
        }   );

    }
    private void issueBook(String timeNow,String bookCode,String idCard,final int position){
        ApiClient.m.api.issueBook(idCard,bookCode,timeNow).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse =response.body();
                Log.d("TAG", "onResponse: "+ response.body());

                try {
                    Log.d("TAG", "onResponse: "+ serverResponse.getResult());

                if(serverResponse.isSuccessful())
                {
                    Toast.makeText(c,"Book Issued",Toast.LENGTH_SHORT).show();
                    offlineTransactionDetailList.remove(position);
                    notifyDataSetChanged();
                    Gson gson = new Gson();
                    String jsonString=gson.toJson(offlineTransactionDetailList);
                    LocalStorage.saveString(jsonString);
                }
                else {
                    Toast.makeText(c, serverResponse.getResult(),Toast.LENGTH_SHORT).show();
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return offlineTransactionDetailList.size();
    }
}
