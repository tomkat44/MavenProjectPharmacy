package com.example.app.domain;

import javax.persistence.*;
import java.util.Objects;


@Entity
@Table(name="quantityPrescriptions")
public class QuantityPrescription {

    @Id
    @Column(name="Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription; //Αυτό το όνομα πρέπει να είναι το ίδιο στο mappedBy = "prescription"

    @Column(name="quantityPrescription", length = 10, nullable = false)
    private int quantityPrescription;



    public QuantityPrescription(){

    }

    /*Εδώ στον Constructor πρέπει να βάλω τα 2 κλειδιά που είναι από τους 2 πίνακες που ενώνονται*/
    public QuantityPrescription(Prescription prescription, Drug drug, int quantityPrescription) {
        this.prescription= prescription;
        this.drug = drug;
        this.quantityPrescription = quantityPrescription;
    }


    public int getId() {
        return id;
    }

    public Drug getDrug() {
        return drug;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public int getQuantityPrescription() {
        return quantityPrescription;
    }

    public void setQuantityPrescription(int quantityPrescription) {
        this.quantityPrescription = quantityPrescription;
    }

    /*Ελέγχει αν η ποσότητα που έγραψε ο γιατρός για το συγκεκριμένο DRUG είναι μεγαλύτερη ή μικρότερη από
    * αυτή που αρίζει ο ΕΟΠΠΥ στην AActiveSubstance*/
    public boolean isInsideLimit(){
        boolean flag;
        this.getDrug().getActiveSubstance().getQuantity();
        //System.out.println("QuantityPrescription = "+ qp.getDrug().getActiveSubstance().getQuantity());
        if (this.getQuantityPrescription() <= this.getDrug().getActiveSubstance().getQuantity()){
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
}
