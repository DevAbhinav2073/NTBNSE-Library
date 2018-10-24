package com.susankya.ntbnse_library;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 * Created by abhinav on 12/5/17.
 */

public class ServerResponse {
    @Expose
    @SerializedName("Result")
    private  String Result;

    @Expose
    @SerializedName("token")
    private  String token;

    @Expose
    @SerializedName("return_date")
    private String returnDate;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
    public boolean hasToken(){
        return  (token!=null);
    }
    public boolean isSuccessful(){
        try {
            return (Result.equals("Success"));

        }
        catch (Exception e){
            return  false;
        }
    }
}
