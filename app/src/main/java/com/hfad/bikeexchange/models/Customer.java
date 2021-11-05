package com.hfad.bikeexchange.models;

public class Customer {
    private String email;
    private String firstName;
    private String secondName;

    public Customer (String email, String firstName, String secondName) {
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public Customer () {}
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getSecondName() { return secondName; }

}
