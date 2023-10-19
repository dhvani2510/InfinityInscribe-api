package com.InfinityInscribe.entities;

import jakarta.persistence.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Collections;

@Document(collection = "users")
public class User extends BaseEntity implements UserDetails {
    private String bio;
    @Id
    private String id;
    private String firstName;
    private String lastName;

    private String email;
    private String username;
    private String password;

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = LocalDate.parse(joiningDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public LocalDate joiningDate;

    public User(String email, String password)  {
        this.email = email;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }


    private User(UserBuilder builder) {
        this.firstName=builder.firstName;
        this.lastName=builder.lastName;
        this.id=builder.Id;
        this.email=builder.Email;
        this.password= builder.Password;
        this.username = builder.username;
        this.bio = builder.bio;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public static class UserBuilder{

        // required parameters
        private String firstName;
        private String lastName;

        // optional parameters
        private String Id;
        private String username;

        private String Email;
        private String Password;

        private String bio;

        public UserBuilder(String name, String surname){
            this.firstName=name;
            this.lastName=surname;
        }

        public UserBuilder setId(String id) {
            this.Id = id;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.Email = email;
            return this;
        }
        public UserBuilder setPassword(String password) {
            this.Password = password;
            return this;
        }

        public User build(){
            return new User(this);
        }

        public String getUsername() {
            return username;
        }

        public UserBuilder setUsername(String username) {
            this.username = username;
            return this;
        }

        public String getBio() {
            return bio;
        }

        public UserBuilder setBio(String bio) {
            this.bio = bio;
            return  this;
        }
    }


}
