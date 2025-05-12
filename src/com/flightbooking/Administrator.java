package com.flightbooking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Administrator extends User {
    private String adminId;
    private List<Flight> managedFlights;
    private List<User> managedUsers;

    public Administrator(String username, String password, String name, String email, String contactInfo) {
        super(username, password, name, email, contactInfo, UserRole.ADMINISTRATOR);
        this.adminId = UUID.randomUUID().toString();
        this.managedFlights = new ArrayList<>();
        this.managedUsers = new ArrayList<>();
    }

    public String getAdminId() {
        return adminId;
    }

    public List<Flight> getManagedFlights() {
        return new ArrayList<>(managedFlights);
    }

    public void addManagedFlight(Flight flight) {
        managedFlights.add(flight);
    }

    public List<User> getManagedUsers() {
        return new ArrayList<>(managedUsers);
    }

    public void addManagedUser(User user) {
        managedUsers.add(user);
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
        Flight flight = new Flight(flightNumber, airline, origin, destination, departureTime, arrivalTime);
        managedFlights.add(flight);
        return flight;
    }

    public boolean modifyFlight(Flight flight, String airline, String origin, String destination,
                              LocalDateTime departureTime, LocalDateTime arrivalTime) {
        if (managedFlights.contains(flight)) {
            flight.setAirline(airline);
            flight.setOrigin(origin);
            flight.setDestination(destination);
            flight.setDepartureTime(departureTime);
            flight.setArrivalTime(arrivalTime);
            return true;
        }
        return false;
    }

    public boolean deleteFlight(Flight flight) {
        return managedFlights.remove(flight);
    }

    public User createUser(String username, String password, String name, String email,
                          String contactInfo, UserRole role) {
        User user = null;
        switch (role) {
            case CUSTOMER:
                user = new Customer(username, password, name, email, contactInfo);
                break;
            case AGENT:
                user = new Agent(username, password, name, email, contactInfo, "Default Department");
                break;
            case ADMINISTRATOR:
                user = new Administrator(username, password, name, email, contactInfo);
                break;
        }
        if (user != null) {
            managedUsers.add(user);
        }
        return user;
    }

    public boolean deleteUser(User user) {
        return managedUsers.remove(user);
    }

    public List<Booking> generateSystemReport(LocalDateTime startDate, LocalDateTime endDate) {
        // TODO: Implement system-wide report generation logic
        return new ArrayList<>();
    }
} 