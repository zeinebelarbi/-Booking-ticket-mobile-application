package com.example.mobileapplication;

public class UserProfile {
    private String name ;
    private String email;
    private String phone;
    private String cin ;


    public UserProfile(String name, String email, String phone, String cin) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.cin = cin;
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
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }
}

