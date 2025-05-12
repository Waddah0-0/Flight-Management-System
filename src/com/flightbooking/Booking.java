package com.flightbooking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Booking {
    private String bookingId;
    private Customer customer;
    private Flight flight;
    private List<Passenger> passengers;
    private SeatClass seatClass;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private Payment payment;

    public Booking(Customer customer, Flight flight, List<Passenger> passengers, SeatClass seatClass) {
        this.bookingId = UUID.randomUUID().toString();
        this.customer = customer;
        this.flight = flight;
        this.passengers = new ArrayList<>(passengers);
        this.seatClass = seatClass;
        this.status = BookingStatus.RESERVED;
        this.bookingTime = LocalDateTime.now();
    }

    public String getBookingId() {
        return bookingId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public double calculateTotalPrice() {
        return flight.calculateTotalPrice(seatClass, passengers.size());
    }

    public boolean confirm() {
        if (status == BookingStatus.RESERVED) {
            status = BookingStatus.CONFIRMED;
            return true;
        }
        return false;
    }

    public boolean cancel() {
        if (status != BookingStatus.CANCELLED && status != BookingStatus.COMPLETED) {
            status = BookingStatus.CANCELLED;
            flight.releaseSeats(seatClass, passengers.size());
            return true;
        }
        return false;
    }

    public boolean complete() {
        if (status == BookingStatus.CONFIRMED) {
            status = BookingStatus.COMPLETED;
            return true;
        }
        return false;
    }
} 