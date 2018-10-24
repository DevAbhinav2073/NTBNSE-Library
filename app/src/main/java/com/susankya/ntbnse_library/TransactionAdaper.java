package com.susankya.ntbnse_library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.susankya.model.Transaction;

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

public class TransactionAdaper extends RecyclerView.Adapter<TransactionAdaper.MyViewHolder> {

private List<Transaction> transactionList;
    Context c;

public class MyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.book_name)TextView bookName;
    @BindView(R.id.member_name)TextView memberName;
    @BindView(R.id.issue_date)TextView issueDate;
    @BindView(R.id.fine_status)TextView fineStatus;
    @BindView(R.id.returnButton)Button returnBook;
    public MyViewHolder(View view) {
        super(view);
        ButterKnife.bind(this,view);
    }
}


    public TransactionAdaper(List<Transaction> moviesList,Context c) {
        this.transactionList = moviesList;
        this.c=c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       final Transaction transaction = transactionList.get(position);
        /*SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        String date=dateFormatter.format(transaction.getIssueDate().toDate());*/

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withLocale(Locale.ROOT)
                .withChronology(ISOChronology.getInstanceUTC());

        DateTime dt = formatter.parseDateTime(transaction.getIssueDate());
        String todaysDate=dt.toDate().toString();
        String localTime=dt.toLocalDate().toString();
        int daysHavingBook= Days.daysBetween(dt.toLocalDate() ,DateTime.now().toLocalDate() ).getDays();
        Log.d(TAG, "onBindViewHolder: "+dt.toString()+" "+localTime);

        holder.bookName.setText(transaction.getBookName()+"\n("+transaction.getBookCode()+")");
        holder.memberName.setText(transaction.getMemberName()+"\n("+transaction.getRollNumber()+")");
        Log.d(TAG, "onBindViewHolder: "+transaction.getId());
        holder.returnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiClient.m.api.returnBook(transaction.getId()+"").enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        ServerResponse sr=response.body();
                        Log.d(TAG, "onResponse: "+response.body());
                       if(sr.isSuccessful()){
                            Toast.makeText(c,"Book Returned",Toast.LENGTH_SHORT).show();
                            transactionList.remove(position);
                           notifyDataSetChanged();
                        }else{

                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {

                    }
                });
            }
        });
        holder.issueDate.setText("Issued On: \n"+localTime);
        if(transaction.getRemainingDays()!=0){
            holder.fineStatus.setText("Remaining Day\n:"+transaction.getRemainingDays());
            holder.fineStatus.setTextColor(c.getResources().getColor(R.color.green));

        }else if (transaction.getFine()!=0)
        {
            holder.fineStatus.setText("Fine \n Rs " +transaction.getFine());
            holder.fineStatus.setTextColor(c.getResources().getColor(R.color.red));
        }
//        if(daysHavingBook>100) {
//            int fine=(daysHavingBook-100)*2;
//            holder.fineStatus.setText("Fine \n Rs " +fine);
//            holder.fineStatus.setTextColor(c.getResources().getColor(R.color.red));
//
//        }
//        else if(daysHavingBook<100){
//            holder.fineStatus.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }
}
