package com.flightbooking;

import java.io.Serializable;
import java.util.UUID;

public abstract class User implements Serializable {
    private String userId;
    private String username;
    private String password;
    private String name;
    private String email;
    private String contactInfo;
    private UserRole role;

    public User(String username, String password, String name, String email, String contactInfo, UserRole role) {
        this.userId = UUID.randomUUID().toString();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.contactInfo = contactInfo;
        this.role = role;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (isValidPassword(password)) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password must be at least 6 characters and contain both letters and numbers");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public UserRole getRole() {
        return role;
    }

    // Validation methods
    private boolean isValidPassword(String password) {
        return password != null && 
               password.length() >= 6 && 
               password.matches(".*[a-zA-Z].*") && 
               password.matches(".*\\d.*");
    }

    private boolean isValidEmail(String email) {
        return email != null && 
               email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    // Abstract methods
    public abstract boolean login(String username, String password);
    public abstract void logout();
    public abstract void updateProfile();
} 