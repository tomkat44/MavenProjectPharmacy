package com.example.app.domain;


import javax.persistence.*;

@Entity
@Table(name="quantityExecutions")
public class QuantityExecution {

    @Id
    @Column(name="Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "prescriptionExecution_id", nullable = false)
    private PrescriptionExecution prescriptionExecution; //Αυτό το όνομα πρέπει να είναι το ίδιο στο mappedBy = "prescription"

    @Column(name="quantityExecutionPieces", length = 10, nullable = false)
    private int quantityExecutionPieces;

    public QuantityExecution(){

    }

    public QuantityExecution(Drug drug, PrescriptionExecution prescriptionExecution, int quantityExecution) {
        this.drug = drug;
        this.prescriptionExecution = prescriptionExecution;
        this.quantityExecutionPieces = quantityExecution;
    }

    public int getId() {
        return id;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public PrescriptionExecution getPrescriptionExecution() {
        return prescriptionExecution;
    }

    public void setPrescriptionExecution(PrescriptionExecution prescriptionExecution){

        this.prescriptionExecution = prescriptionExecution;
    }

    public int getQuantityExecutionPieces() {
        return quantityExecutionPieces;
    }

    public void setQuantityExecutionPieces(int quantityExecution) {
        this.quantityExecutionPieces = quantityExecution;
    }
}
