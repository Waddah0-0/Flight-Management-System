package com.flightbooking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Agent extends User {
    private String agentId;
    private String department;
    private double commission;
    private List<Booking> managedBookings;

    public Agent(String username, String password, String name, String email, 
                String contactInfo, String department) {
        super(username, password, name, email, contactInfo, UserRole.AGENT);
        this.agentId = UUID.randomUUID().toString();
        this.department = department;
        this.commission = 0.0;
        this.managedBookings = new ArrayList<>();
    }

    public String getAgentId() {
        return agentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        if (commission >= 0) {
            this.commission = commission;
        } else {
            throw new IllegalArgumentException("Commission cannot be negative");
        }
    }

    public List<Booking> getManagedBookings() {
        return new ArrayList<>(managedBookings);
    }

    public void addManagedBooking(Booking booking) {
        managedBookings.add(booking);
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

    public Flight createFlight(String flightNumber, String airline, String origin, 
                             String destination, LocalDateTime departureTime, 
                             LocalDateTime arrivalTime) {
        // TODO: Implement flight creation logic
        return new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime);
    }

    public Booking createBookingForCustomer(Customer customer, Flight flight, 
                                          List<Passenger> passengers, SeatClass seatClass) {
        // TODO: Implement booking creation logic
        Booking booking = new Booking(customer, flight, passengers, seatClass);
        managedBookings.add(booking);
        return booking;
    }

    public boolean modifyBooking(Booking booking, SeatClass newSeatClass) {
        if (managedBookings.contains(booking)) {
            // TODO: Implement booking modification logic
            return true;
        }
        return false;
    }

    public List<Booking> generateBookingReport(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement report generation logic
        return new ArrayList<>();
    }
} 