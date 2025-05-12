package com.flightbooking;

import java.time.LocalDate;
import java.util.UUID;

public class Passenger {
    private String passengerId;
    private String name;
    private String passportNumber;
    private LocalDate dateOfBirth;
    private String nationality;
    private String contactInfo;

    public Passenger(String name, String passportNumber, LocalDate dateOfBirth, 
                    String nationality, String contactInfo) {
        this.passengerId = UUID.randomUUID().toString();
        this.name = name;
        this.passportNumber = passportNumber;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.contactInfo = contactInfo;
    }

    public String getPassengerId() {
        return passengerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int calculateAge() {
        return LocalDate.now().getYear() - dateOfBirth.getYear();
    }

    public boolean isAdult() {
        return calculateAge() >= 18;
    }
} 