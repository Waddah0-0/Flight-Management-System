package com.flightbooking;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer extends User {
    private String customerId;
    private List<Booking> bookings;
    private List<Payment> paymentHistory;

    public Customer(String username, String password, String name, String email, String contactInfo) {
        super(username, password, name, email, contactInfo, UserRole.CUSTOMER);
        this.customerId = UUID.randomUUID().toString();
        this.bookings = new ArrayList<>();
        this.paymentHistory = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public List<Payment> getPaymentHistory() {
        return new ArrayList<>(paymentHistory);
    }

    public void addPayment(Payment payment) {
        paymentHistory.add(payment);
    }

    @Override
    public boolean login(String username, String password) {
        // TODO: Implement actual authentication logic
        return this.getUsername().equals(username) && this.getPassword().equals(password);
    }

    @Override
    public void logout() {
        // TODO: Implement logout logic
    }

    @Override
    public void updateProfile() {
        // TODO: Implement profile update logic
    }

    public Booking createBooking(Flight flight, List<Passenger> passengers, SeatClass seatClass) {
        // TODO: Implement booking creation logic
        Booking booking = new Booking(this, flight, passengers, seatClass);
        bookings.add(booking);
        return booking;
    }

    public boolean cancelBooking(Booking booking) {
        if (bookings.contains(booking)) {
            // TODO: Implement booking cancellation logic
            return true;
        }
        return false;
    }

    public Payment makePayment(Booking booking, PaymentMethod method, double amount) {
        // TODO: Implement payment processing logic
        Payment payment = new Payment(booking, method, amount);
        paymentHistory.add(payment);
        return payment;
    }
} 