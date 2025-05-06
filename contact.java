package com.example.demo;
public class contact {
    private String firstName;
    private String name;
    private String phone;

    public contact(String firstName, String name, String phone) {
        this.firstName = firstName;
        this.name = name;
        this.phone = phone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return firstName + " " + name + " - " + phone;
    }
}

