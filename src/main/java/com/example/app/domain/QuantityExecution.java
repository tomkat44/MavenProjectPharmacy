package com.example.app.domain;


import javax.persistence.*;

@Entity
@Table(name="quantityExecutions")
public class QuantityExecution {

    @Id
    @Column(name="Id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name="quantityExecutionPieces", length = 10, nullable = false)
    private Integer quantityExecutionPieces;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drug_id", nullable = false)
    private Drug drug;

    //, cascade = CascadeType.ALL
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "prescriptionExecution_id")
    private PrescriptionExecution prescriptionExecution; //Αυτό το όνομα πρέπει να είναι το ίδιο στο mappedBy = "prescription"

    public QuantityExecution(){

    }

    public QuantityExecution(Drug drug, PrescriptionExecution prescriptionExecution, int quantityExecution) {
        this.drug = drug;
        this.prescriptionExecution = prescriptionExecution;
        this.quantityExecutionPieces = quantityExecution;
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

    public PrescriptionExecution getPrescriptionExecution() {
        return prescriptionExecution;
    }

    public void setPrescriptionExecution(PrescriptionExecution prescriptionExecution){

        this.prescriptionExecution = prescriptionExecution;
    }

    public void setQuantityExecutionPieces(Integer quantityExecutionPieces) {
        this.quantityExecutionPieces = quantityExecutionPieces;
    }

    public int getQuantityExecutionPieces() {
        return quantityExecutionPieces;
    }

    public void setQuantityExecutionPieces(int quantityExecution) {
        this.quantityExecutionPieces = quantityExecution;
    }
}

