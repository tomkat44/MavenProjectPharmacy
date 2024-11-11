package com.example.app.domain;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "patients")
public class Patient {

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

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "password", length = 50)
    private String password;

    @Embedded
    private Address address;

    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy="patientToPrescription", fetch=FetchType.LAZY)
    private Set<Prescription> prescriptions = new HashSet<Prescription>();

    public Patient() {

    }

    public Patient(String firstName, String lastName, String amka, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.amka = amka;
        this.email = email;
        this.password = password;
        this.address = address == null ? null : new Address(address);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getAmka() {
        return amka;
    }

    public void setAmka(String amka) {
        this.amka = amka;
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

    /*Ο ασθενής θα μπορεί να δει τις Συνταγές που του έχουν γραφετεί δίνοντας
    * το ιατρό που του τις έγραψε*/
    public Set<Prescription> getPrescriptions(Doctor doctor) {

        return new HashSet<>(doctor.getPrescriptionFromDoctor());
    }


}


