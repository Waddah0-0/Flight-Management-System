package com.flightbooking;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private String paymentId;
    private Booking booking;
    private PaymentMethod method;
    private double amount;
    private PaymentStatus status;
    private LocalDateTime paymentTime;
    private String transactionReference;

    public Payment(Booking booking, PaymentMethod method, double amount) {
        this.paymentId = UUID.randomUUID().toString();
        this.booking = booking;
        this.method = method;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.paymentTime = LocalDateTime.now();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (amount >= 0) {
            this.amount = amount;
        } else {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public boolean process() {
        // TODO: Implement actual payment processing logic
        if (status == PaymentStatus.PENDING) {
            status = PaymentStatus.COMPLETED;
            return true;
        }
        return false;
    }

    public boolean refund() {
        if (status == PaymentStatus.COMPLETED) {
            status = PaymentStatus.REFUNDED;
            return true;
        }
        return false;
    }
} 