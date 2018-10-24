package com.susankya.model;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

/**
 [
 {
 "id": 3,
 "issue_date": "2017-12-08T16:35:01Z",
 "return_date": null,
 "memberName": "Aditya Shah",
 "bookName": "Engineering Mathematics I",
 "rollNumber": "073bct504",
 "authors": "SB Sakya\r\n",
 "bookCode": "maths02"
 }
 ]
 */
//    ('id','issue_date','return_date','memberName','bookName','rollNumber','bookCode')

public class Transaction {
    @SerializedName("bookName")
    private String bookName;
    @SerializedName("Result")
    private String result;
    @SerializedName("id")
    private int id;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemainingDays() {
        return remainingDays;
    }

    public void setRemainingDays(int remainingDays) {
        this.remainingDays = remainingDays;
    }

    public int getFine() {
        return fine;
    }

    public void setFine(int fine) {
        this.fine = fine;
    }

    @SerializedName("memberName")
    private String memberName;

    @SerializedName("rollNumber")
    private String rollNumber;

    @SerializedName("bookCode")
    private String bookCode;

    @SerializedName("authors")
    private String authors;

    @SerializedName("issue_date")
    private String issueDate;

    @SerializedName("remainingDays")
    private int remainingDays;

    @SerializedName("fine")
    private int fine;

    @SerializedName("return_date")
    private String returnDate;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
