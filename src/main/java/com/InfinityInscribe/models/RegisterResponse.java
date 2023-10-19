package com.InfinityInscribe.models;

public class RegisterResponse {
    private  String Id;
    private  String firstName;
    private  String lastName;
    private  String Email;
    private String username;
    private String bio;

    public RegisterResponse(String id, String name, String surname, String email, String username, String bio) {
        Id = id;
        firstName = name;
        lastName = surname;
        Email = email;
        this.username = username;
        this.bio = bio;
    }

    public String getId() {
        return Id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }
}
