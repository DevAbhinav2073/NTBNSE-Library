package com.susankya.ntbnse_library;

import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.model.Transaction;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by abhinav on 12/6/17.
 */

public class TransactionIndividualAdapter extends RecyclerView.Adapter<TransactionIndividualAdapter.MyViewHolder> {

    private List<Transaction> moviesList;
    Context c;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.bookName)TextView bookName;
        @BindView(R.id.remainingDays)TextView reaminingDays;
        @BindView(R.id.issuedOn)TextView issueDate;


        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public TransactionIndividualAdapter(List<Transaction> moviesList, Context c) {
        this.moviesList = moviesList;
        this.c=c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_for_individual_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = moviesList.get(position);
        /*SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMM yyyy");
        String date=dateFormatter.format(transaction.getIssueDate().toDate());*/

        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .withLocale(Locale.ROOT)
                .withChronology(ISOChronology.getInstanceUTC());

        DateTime dt = formatter.parseDateTime(transaction.getIssueDate());
        int daysHavingBook=Days.daysBetween(dt.toLocalDate() ,DateTime.now().toLocalDate() ).getDays();
        String issuedDate=dt.toLocalDate().toString();

        Log.d(TAG, "onBindViewHolder: "+dt.toString()+" "+issuedDate);

        holder.bookName.setText(transaction.getBookName()+"\n("+transaction.getBookCode()+")");
        holder.issueDate.setText("Issued On: "+issuedDate);
        if(daysHavingBook>100) {
            int fine=(daysHavingBook-100)*2;
            holder.reaminingDays.setText("Fine \n Rs " +fine);
            holder.reaminingDays.setTextColor(c.getResources().getColor(R.color.red));

        }
        else if(daysHavingBook<100){
        holder.reaminingDays.setText("Remaining days \n"+(100-daysHavingBook));
        }
        else if(daysHavingBook==100){
            holder.reaminingDays.setText("Return Today");
            holder.reaminingDays.setTextColor(c.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
