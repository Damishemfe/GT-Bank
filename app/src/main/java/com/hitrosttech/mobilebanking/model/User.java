package com.hitrosttech.mobilebanking.model;

import android.widget.EditText;

public class User {
    String firstName, lastName, email;
    long createdAt;

    //    Shortcut for Generating code is Alt + insert
    public User(EditText firstName, EditText lastName, EditText email, long time) {

    }

    public User(String firstName, String lastName, String email, long createdAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.createdAt = createdAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
