package com.example.app.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "pharmacists")
public class Pharmacist {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "AFM", length = 200, nullable = false)
    private String afm;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Embedded
    private Address address;

    public Pharmacist(){

    }

    public Pharmacist(String firstName, String lastName, String afm, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.afm = afm;
        this.address = address == null ? null : new Address(address);
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setAddress(Address address) {
        this.address = address == null ? null : new Address(address);
    }

    public Address getAddress() {
        return address == null ? null : new Address(address);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAfm() {
        return afm;
    }

    public void setAfm(String afm) {
        this.afm = afm;
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

    /*Δυνατότητα ο Φαρμακοποιός να λάβει την Prescription από τον ασθενή*/
    public Set<Prescription> getPrescriptions(Patient patient, Doctor doctor) {

        return new HashSet<>(patient.getPrescriptions(doctor));
    }
}
