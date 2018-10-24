package com.susankya.ntbnse_library;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;

import com.susankya.model.Book;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class AvailableBooks extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    BookAdapter adapter;
    private ArrayList<Book> books,searchBooks;
    @BindView(R.id.rv_books)RecyclerView rvBooks;
    @BindView(R.id.emptyView)TextView emptyView;
    @BindView(R.id.searchEt)EditText editText;
    @BindView(R.id.button_search)ImageButton searchButton;


    public AvailableBooks() {
        // Required empty public constructor
    }

    public static AvailableBooks newInstance(String param1, String param2) {
        AvailableBooks fragment = new AvailableBooks();
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
        books=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_available_books, container, false);
        ButterKnife.bind(this,v);
       // populate();
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().isEmpty())
                    return;
                search(editText.getText().toString().toLowerCase().trim());


            }
        });
        emptyView.setText("Loading...");
        manageEmptyView(false);
        ApiClient.m.api.getAllAvailableBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                books= (ArrayList<Book>) response.body();
                Log.d(TAG, "onResponse: this"+response.body().size());
                adapter=new BookAdapter(books);
                rvBooks.setAdapter(adapter);
                manageEmptyView(false);

            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {

            }
        });
        adapter=new BookAdapter(books);
        LinearLayoutManager llm=new LinearLayoutManager(getActivity());

        rvBooks.setLayoutManager(llm);
        rvBooks.setItemAnimator(new DefaultItemAnimator());
        rvBooks.setAdapter(adapter);
        return  v;
    }
    private void search(String str){
        searchBooks=new ArrayList<>();

        for(Book b:books){
            if(b.getName().toLowerCase().contains(str)||b.getAuthor().toLowerCase().contains(str)){
                searchBooks.add(b);
            }

        }
        adapter=new BookAdapter(searchBooks);
        rvBooks.setAdapter(adapter);
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
        if(searchBooks.size()==0){
            emptyView.setText("No Result Found");}

        }

        if(books.size()==0){
            emptyView.setVisibility(View.VISIBLE);


                emptyView.setText("No Books");
        }
        else {
            emptyView.setText("");
            emptyView.setVisibility(View.GONE);
        }
    }
    private void populate(){
        for(int i=0;i<10;i++){
            Book b=new Book();
            b.setAuthor("author");
            b.setId(i+1);
            b.setName("Maths");
            b.setTotal(10);
            b.setRemaining(5);
            books.add(b);
        }

    }

}
