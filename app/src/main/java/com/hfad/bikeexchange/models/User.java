package com.hfad.bikeexchange.models;

public class User {
    private String email;
    private String firstName;
    private String secondName;

    public User(String email, String firstName, String secondName) {
        this.email = email;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public User() {}
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getSecondName() { return secondName; }

}
