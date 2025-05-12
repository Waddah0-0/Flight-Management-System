package com.flightbooking;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Flight {
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private Map<SeatClass, Integer> availableSeats;
    private Map<SeatClass, Double> prices;

    public Flight(String flightNumber, String airline, String origin, String destination,
                 LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.availableSeats = new HashMap<>();
        this.prices = new HashMap<>();
        initializeSeatsAndPrices();
    }

    private void initializeSeatsAndPrices() {
        // Initialize with default values
        availableSeats.put(SeatClass.ECONOMY, 100);
        availableSeats.put(SeatClass.BUSINESS, 30);
        availableSeats.put(SeatClass.FIRST_CLASS, 10);

        prices.put(SeatClass.ECONOMY, 100.0);
        prices.put(SeatClass.BUSINESS, 300.0);
        prices.put(SeatClass.FIRST_CLASS, 500.0);
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public void setAirline(String airline) {
        this.airline = airline;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getAvailableSeats(SeatClass seatClass) {
        return availableSeats.getOrDefault(seatClass, 0);
    }

    public void setAvailableSeats(SeatClass seatClass, int count) {
        if (count >= 0) {
            availableSeats.put(seatClass, count);
        } else {
            throw new IllegalArgumentException("Seat count cannot be negative");
        }
    }

    public double getPrice(SeatClass seatClass) {
        return prices.getOrDefault(seatClass, 0.0);
    }

    public void setPrice(SeatClass seatClass, double price) {
        if (price >= 0) {
            prices.put(seatClass, price);
        } else {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public boolean hasAvailableSeats(SeatClass seatClass, int count) {
        return getAvailableSeats(seatClass) >= count;
    }

    public void reserveSeats(SeatClass seatClass, int count) {
        if (!hasAvailableSeats(seatClass, count)) {
            throw new IllegalStateException("Not enough seats available");
        }
        setAvailableSeats(seatClass, getAvailableSeats(seatClass) - count);
    }

    public void releaseSeats(SeatClass seatClass, int count) {
        setAvailableSeats(seatClass, getAvailableSeats(seatClass) + count);
    }

    public double calculateTotalPrice(SeatClass seatClass, int passengerCount) {
        return getPrice(seatClass) * passengerCount;
    }
} 