package br.edu.ifsp.arq.ads.dmos5.eventx.model;

import androidx.annotation.NonNull;
import androidx.room.Ignore;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private String id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String role;
    private String birthDate;
    private String state;
    private String country;

    public User(String name, String surname, String email, String password, String role, String birthDate, String state, String country) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.role = role;
        this.birthDate = birthDate;
        this.state = state;
        this.country = country;
    }

    @Ignore
    public User() {
        this("", "", "", "", "", "", "", "");
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
