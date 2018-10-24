package com.susankya.ntbnse_library;

import com.susankya.model.Book;
import com.susankya.model.Transaction;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface LibraryApi {

    @GET("search/{book}")
    Call<List<Book>> search(@Path("book") String apiKey);

    @GET("issuedBooks/")
    Call<List<Transaction>> getAllTransaction();

    @GET("availableBooks/")
    Call<List<Book>> getAllAvailableBooks();

    @GET("issueDetail/{rollNum}/")
    Call<List<Transaction>> getMyTransaction(@Path("rollNum") String rollnum);

    @GET("issueBook/{rollNumber}/{bookCode}/{datetime}/")
    Call<ServerResponse> issueBook(@Path("rollNumber") String rollnumber, @Path("bookCode") String bookcode,@Path("datetime") String datetime);



    @GET("getTransactionDetail/{rollNumber}/{bookCode}")
    Call<Transaction> getTransactionDetail(@Path("rollNumber") String rollnumber, @Path("bookCode") String bookcode);

    @GET("returnBook/{id}/")
    Call<ServerResponse> returnBook(@Path("id") String id);

    @GET("getSetReturnDate/")
    Call<ServerResponse> getReturnDate();


    @FormUrlEncoded
    @POST("getSetReturnDate/")
    Call<ServerResponse> setReturnDate(@Field("returnDate") String returnDate);

    @FormUrlEncoded
    @POST("login/admin/")
    Call<ServerResponse> adminLogin(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("login/user/")
    Call<ServerResponse> userLogin(@Field("rollNumber") String rollNumber, @Field("passCode") String passCode);


}
