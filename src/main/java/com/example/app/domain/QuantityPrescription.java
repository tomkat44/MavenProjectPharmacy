package com.example.app.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name="quantityPrescriptions")
public class QuantityPrescription {

    @Id
    @Column(name="Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="quantityPrescription", length = 10, nullable = false)
    private Integer quantityPrescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_id")
    private Prescription prescription; //Αυτό το όνομα πρέπει να είναι το ίδιο στο mappedBy = "prescription"


    public QuantityPrescription(){

    }

    /*Εδώ στον Constructor πρέπει να βάλω τα 2 κλειδιά που είναι από τους 2 πίνακες που ενώνονται*/
    public QuantityPrescription(Prescription prescription, Drug drug, Integer quantityPrescription) {
        this.prescription= prescription;
        this.drug = drug;
        this.quantityPrescription = quantityPrescription;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public Prescription getPrescription() {
        return prescription;
    }


    public void setQuantityPrescription(Integer quantityPrescription) {
        this.quantityPrescription = quantityPrescription;
    }


    public Integer getQuantityPrescription() {
        return quantityPrescription;
    }


    /*Ελέγχει αν η ποσότητα που έγραψε ο γιατρός για το συγκεκριμένο DRUG είναι μεγαλύτερη ή μικρότερη από
    * αυτή που αρίζει ο ΕΟΠΠΥ στην ActiveSubstance*/
    public boolean isInsideLimit(){
        boolean flag;

        String presentMonth = LocalDate.now().toString();


        System.out.println("prescription.getCreationDate() 1=  "+ this.getPrescription().getCreationDate().substring(5,7) +"\t"+ presentMonth.substring(5,7));
        if (this.getQuantityPrescription() <= this.getDrug().getActiveSubstance().getQuantity() &&
                this.getPrescription().getCreationDate().substring(5,7).equals(presentMonth.substring(5,7))){
            flag = true;
//            System.out.println("QuantityPrescription BUSTED= "+ this.getPrescription().getId());
        } else {
            flag = false;
        }
        return flag;
    }



    public boolean isInsideLimit2(Integer quantity){
        boolean flag;

        String presentMonth = LocalDate.now().toString();


        System.out.println("prescription.getCreationDate() 1=  "+ this.getPrescription().getCreationDate().substring(5,7) +"\t"+ presentMonth.substring(5,7));
        if (quantity <= this.getDrug().getActiveSubstance().getQuantity() &&
                this.getPrescription().getCreationDate().substring(5,7).equals(presentMonth.substring(5,7))){
            flag = true;
//            System.out.println("QuantityPrescription BUSTED= "+ this.getPrescription().getId());
        } else {
            flag = false;
        }
        return flag;
    }
}
