package com.guesthouse.entities;

import java.time.LocalDate;

public class Payment {
    private int       paymentId;
    private int       bookingId;
    private double    amount;
    private String    method;       // "Cash", "Card", "Online"
    private LocalDate paymentDate;
    private String    status;       // "Paid", "Pending", "Failed"

    public Payment(int paymentId, int bookingId, double amount,
                   String method, LocalDate paymentDate, String status) {
        this.paymentId   = paymentId;
        this.bookingId   = bookingId;
        this.amount      = amount;
        this.method      = method;
        this.paymentDate = paymentDate;
        this.status      = status;
    }

    public int       getPaymentId()   { return paymentId; }
    public int       getBookingId()   { return bookingId; }
    public double    getAmount()      { return amount; }
    public String    getMethod()      { return method; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public String    getStatus()      { return status; }

    public void setAmount(double amount)         { this.amount = amount; }
    public void setMethod(String method)         { this.method = method; }
    public void setStatus(String status)         { this.status = status; }
    public void setPaymentDate(LocalDate date)   { this.paymentDate = date; }

    @Override
    public String toString() {
        return "Payment #" + paymentId + " | Booking: " + bookingId +
               " | Rs. " + amount + " | " + method + " | " + status;
    }
}
