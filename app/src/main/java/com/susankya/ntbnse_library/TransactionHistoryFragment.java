package com.susankya.ntbnse_library;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.model.Book;
import com.susankya.model.Transaction;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class TransactionHistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    MySharedPreferences mypref;
    private TransactionIndividualAdapter adapter;
    @BindView(R.id.rvTransaction)RecyclerView rvTransaction;
    @BindView(R.id.emptyView)TextView emptyView;
    ArrayList<Transaction> transactions;
    public TransactionHistoryFragment() {
        // Required empty public constructor
    }
public static TransactionHistoryFragment newInstance(String param1, String param2) {
        TransactionHistoryFragment fragment = new TransactionHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        transactions=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_transaction_history, container, false);
        ButterKnife.bind(this,v);
        //populate();
        mypref=new MySharedPreferences(getActivity(),Constants.BASIC_SHAREDPREF);
        Log.d(TAG, "onResponse: transaction for user "+mypref.getRollNumber());
        emptyView.setText("Loading...");
        manageEmptyView();
        ApiClient.m.api.getMyTransaction(mypref.getRollNumber()).enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                transactions= (ArrayList<Transaction>) response.body();
                Log.d(TAG, "onResponse: transaction for user"+response.body());
                 adapter=new TransactionIndividualAdapter(transactions,getActivity());
                rvTransaction.setAdapter(adapter);
                manageEmptyView();

            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());

            }
        });
       adapter=new TransactionIndividualAdapter(transactions,getActivity());
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());
        rvTransaction.setLayoutManager(llm);
        rvTransaction.setItemAnimator(new DefaultItemAnimator());
        rvTransaction.setAdapter(adapter);
       return  v;
    }
    private void manageEmptyView() {
        if (!Utilities.isNetworkAvailable(getActivity())) {
            emptyView.setText("No Internet");
            emptyView.setVisibility(View.VISIBLE);
            return;
        }   if(transactions.size()==0){
            emptyView.setVisibility(View.VISIBLE);
            emptyView.setText("No Transactions");
        }
        else {
            emptyView.setText("");
            emptyView.setVisibility(View.GONE);
        }
    }
    private void populate(){
        for(int i=0;i<10;i++){
            Transaction t=new Transaction();
            t.setBookName("Bookname");
            t.setMemberName("MemberName");
            t.setBookCode("BookCode");
            t.setRollNumber("ROllNumber");
            t.setIssueDate(DateTime.now().toString());
            transactions.add(t);
        }


    }

}
