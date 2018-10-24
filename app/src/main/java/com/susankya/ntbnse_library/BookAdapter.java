package com.susankya.ntbnse_library;

import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susankya.model.Book;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abhinav on 12/6/17.
 */

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.MyViewHolder> {

    private List<Book> booksList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.book_title)TextView bookTitle;
        @BindView(R.id.authors)TextView authors;
        @BindView(R.id.totalBooks)TextView totalBooks;
        @BindView(R.id.remainingBook)TextView reaminingBooks;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }


    public BookAdapter(List<Book> booksList) {
        this.booksList = booksList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_adapter_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Book book = booksList.get(position);
        holder.bookTitle.setText(book.getName());
        holder.authors.setText(book.getAuthor());
        holder.totalBooks.setText("Total\n\n"+book.getTotal()+"");
        holder.reaminingBooks.setText("Remaining\n\n"+book.getRemaining()+"");
    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }
}
