package com.example.app.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 50, nullable = false)
    private String lastName;

    @Column(name = "AMKA", length = 200, nullable = false)
    private String amka;

    @Column(name = "AFM", length = 200, nullable = false)
    private String afm;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 50)
    private String password;

    @Embedded
    private Address address;

    //cascade={CascadeType.PERSIST, CascadeType.MERGE},
    @OneToMany(
            mappedBy="doctorToPrescription", fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Prescription> prescriptions = new HashSet<Prescription>();

    public Doctor() {

    }
    public Doctor(String firstName, String lastName, String amka, String afm, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.amka = amka;
        this.afm = afm;
        this.address = address == null ? null : new Address(address);
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id){this.id=id;}

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

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
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

    public Set<Prescription> getPrescriptionFromDoctor() {
        return new HashSet<>(prescriptions);
    }
    public void addNewPrescription(Prescription prescription) {
        prescriptions.add(prescription);
    }
    public void removePrescription(Prescription prescription) {
        prescriptions.remove(prescription);
    }

}
