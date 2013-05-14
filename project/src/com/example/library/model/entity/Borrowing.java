package com.example.library.model.entity;

import java.io.Serializable;
import java.util.Date;

public class Borrowing implements Serializable {

    private long id;
    private Date borrowingDate;
    private Date returnDate;
    private int alerts;
    private int penaltyAttached;
    private BookCopy bookCopy;
    
    public Borrowing() {
        
    }
    
    public void setId(long id) {
        this.id = id;
    }
    public void setBorrowingDate(Date borrowingDate) {
        this.borrowingDate = borrowingDate;
    }
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    public void setAlerts(int alerts) {
        this.alerts = alerts;
    }
    public void setPenaltyAttached(int penaltyAttached) {
        this.penaltyAttached = penaltyAttached;
    }
    public void setBookCopy(BookCopy bookCopy) {
        this.bookCopy = bookCopy;
    }

    public long getId() {
        return id;
    }
    public Date getBorrowingDate() {
        return borrowingDate;
    }
    public Date getReturnDate() {
        return returnDate;
    }
    public int getAlerts() {
        return alerts;
    }
    public int getPenaltyAttached() {
        return penaltyAttached;
    }
    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public String toString() {
        return "Borrowing [id = "+getId()+", borrowingDate = "+getBorrowingDate()+", returnDate = "+getReturnDate()+", alerts = "+getAlerts()+", penaltyAttached = "+getPenaltyAttached()+", bookCopy = "+getBookCopy()+"]";
    }

}