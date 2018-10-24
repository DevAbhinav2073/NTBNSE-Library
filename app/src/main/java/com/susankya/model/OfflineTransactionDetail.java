package com.susankya.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by abhinav on 12/11/17.
 */

public class OfflineTransactionDetail {
    @SerializedName("idCardNumber")
    private String idCardNumber;
    @SerializedName("bookCode")
    private String bookCode;

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getTransactonType() {
        return transactonType;
    }

    public void setTransactonType(String transactonType) {
        this.transactonType = transactonType;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @SerializedName("transactionType")

    private String transactonType;
    @SerializedName("issueDate")
    private String time;
}
