package com.susankya.ntbnse_library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
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


public class TransactionHistoryForAdmin extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

      private String mParam1;
    private String mParam2;
    @BindView(R.id.rvTransaction)RecyclerView rvTransaction;
    @BindView(R.id.button_scanner)ImageButton  scannerButton;
    @BindView(R.id.emptyView)TextView emptyView;
    @BindView(R.id.searchEt)EditText editText;
    @BindView(R.id.button_search)ImageButton searchButton;
    ArrayList<Transaction> transactions,searchTransaction;
    private  TransactionAdaper adapter;
    public TransactionHistoryForAdmin() {
        // Required empty public constructor
    }

    public static TransactionHistoryForAdmin newInstance(String param1, String param2) {
        TransactionHistoryForAdmin fragment = new TransactionHistoryForAdmin();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d("onActivityResult", "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            Toast.makeText(getActivity(),re,Toast.LENGTH_SHORT).show();

            Log.d("onActivityResult", "onActivityResult: ." + re);

        }
        // else continue with any other code you need in the method
        //this.finish();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        loadData();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d(TAG, "setUserVisibleHint: ");
        if (isVisibleToUser)
           loadData();
        
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
        View v= inflater.inflate(R.layout.fragment_transaction_history_for_admin, container, false);
        ButterKnife.bind(this,v);
        //populate();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().isEmpty())
                    return;
                search(editText.getText().toString().toLowerCase().trim());


            }
        });
        scannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setCaptureActivity(CustomScannerActivity.class);
                integrator.initiateScan();
            }
        });
        emptyView.setText("Loading...");
        manageEmptyView(false);

        loadData();

        return  v;
    }

    private void loadData(){
        ApiClient.m.api.getAllTransaction().enqueue(new Callback<List<Transaction>>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                transactions= (ArrayList<Transaction>) response.body();
                Log.d(TAG, "onResponse: "+transactions.size());
//                Log.d(TAG, "onResponse: "+transactions.get(0).getBookName());
                adapter=new TransactionAdaper(transactions,getActivity());
                LinearLayoutManager llm=new LinearLayoutManager(getActivity());
                rvTransaction.setLayoutManager(llm);
                rvTransaction.setItemAnimator(new DefaultItemAnimator());
                rvTransaction.setAdapter(adapter);
                manageEmptyView(false);
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {

            }
        });
    }
    private void search(String str){
        searchTransaction=new ArrayList<>();

        for(Transaction t:transactions){
            if(t.getRollNumber().toLowerCase().contains(str)||t.getMemberName().toLowerCase().contains(str)){
                searchTransaction.add(t);
            }

        }
        adapter=new TransactionAdaper(searchTransaction,getActivity());
        rvTransaction.setAdapter(adapter);
        manageEmptyView(true);
    }
    private void manageEmptyView(Boolean isSearch) {
        if (!Utilities.isNetworkAvailable(getActivity())) {
            emptyView.setText("No Internet");
            emptyView.setVisibility(View.VISIBLE);
            return;
        }
        if(isSearch)
        {
            if(searchTransaction.size()==0){
                emptyView.setText("No Result Found");}

        }

        if(transactions.size()==0){
            emptyView.setVisibility(View.VISIBLE);


            emptyView.setText("No Transaction");
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
