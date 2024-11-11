package com.example.app.domain;


import com.example.app.util.SystemDate;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "prescriptionExecutions")
public class PrescriptionExecution {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacist_id", nullable = false)
    private Pharmacist pharmacist;

    //Σύνδεση με το QuantityExecution
    @OneToMany(mappedBy = "prescriptionExecution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuantityExecution> quantityExecutions = new HashSet<QuantityExecution>();

    /*Το θέλω για να μπορώ να πάρω τα στοιχεία της συνταγής μεσα από την εκτέλεσή της*/
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription")
    private Prescription prescription;

    @Column(name="pharmacistAFM", length=15, nullable=false)
    private String doctorAMKA;

    @Column(name="executionDate", length=40, nullable=false)
    private LocalDate executionDate = SystemDate.now();

    @Column(name="summaryCost", length=15, nullable=false)
    private double summaryCost;

    @Column(name="executionFlag", length=15, nullable=false)
    private executionPrescriptionFlag executionFlag;


    public PrescriptionExecution(){
    }

    public PrescriptionExecution(String doctorAMKA, LocalDate executionDate) {
        this.doctorAMKA = doctorAMKA;
        this.executionDate = executionDate;
        this.executionFlag = executionPrescriptionFlag.PENDING;
    }

    public int getId() {
        return id;
    }

    public String getDoctorAMKA() {
        return doctorAMKA;
    }

    public void setDoctorAMKA(String doctorAMKA) {
        this.doctorAMKA = doctorAMKA;
    }

    public LocalDate getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(LocalDate executionDate) {
        this.executionDate = executionDate;
    }


    /*Υλοποιείται το συνολικό κόστος με τρόπο τέτοιο ώστε να υπολογίζεται και το κόστος με την έκπτωση
    * ανάλογα με το αν το φάρμακι είνια προτώτυπο ή γενόσημο*/
    public double getSummaryCost(List<QuantityExecution> qe ) {
        summaryCost=0;
        for (QuantityExecution qes : qe){
            if(qes.getDrug().getMedicineCategory()==medicineCategory.ORIGINALS){
                summaryCost = summaryCost + (qes.getQuantityExecutionPieces()*qes.getDrug().getDrugPrice())*0.02;
            } else {
                summaryCost = summaryCost + (qes.getQuantityExecutionPieces()*qes.getDrug().getDrugPrice())*0.10;
            }
        }
        return summaryCost;
    }

    public executionPrescriptionFlag isExecutionFlag() {
        return executionFlag;
    }

    /*Κατά την εκτέλεση της συνταγής θα ελέγχεται αν ο φαρμακοποιός έδωσε περισσότερα
     * από τα φάρμακα που έγραψε ο γιατρός*/
    public void setExecutionFlag(List<QuantityExecution> q_execution, List<QuantityPrescription> q_prescription) throws DomainException {

        boolean flag = true;
        if(q_prescription.size()==0){
            executionFlag = executionPrescriptionFlag.CANCELED;
            flag = false;
        } else {
            for (int i = 0; i < q_prescription.size(); i++) {
                if (q_execution.get(i).getQuantityExecutionPieces() > q_prescription.get(i).getQuantityPrescription()) {
                    throw new DomainException("QuantityExecution Pieces is Higher than Doctors QuantityPrescription");
                } else if (q_execution.get(i).getQuantityExecutionPieces() < q_prescription.get(i).getQuantityPrescription()) {
                    flag = false;
                } else {
                    flag = true;
                }//if
            }//for
            if (flag) {
                executionFlag = executionPrescriptionFlag.EXECUTED;
            } else {
                executionFlag = executionPrescriptionFlag.PARTIALLY_EXECUTED;
            } //if
        } //if
    }

    //@ManyToOne Pharmacist
    public Pharmacist getPharmacist() {
        return pharmacist;
    }
    public void setPharmacist(Pharmacist pharmacist) {
        this.pharmacist = pharmacist;
    }


    //@OneToMany Getter kai ADD (όχι SETTER διότι είναι Interface
    public Set<QuantityExecution> getQuantityExecutions() {
        return quantityExecutions;
    }

    public void addQuantityExecution(QuantityExecution quantityExecution) {
        quantityExecutions.add(quantityExecution);
    }
    public void removeQuantityExecution(QuantityExecution quantityExecution) {
        quantityExecutions.remove(quantityExecution);
    }

    /*@OneToOne with Prescription
    Το θέλω για να μπορώ να πάρω τα στοιχεία της συνταγής μεσα από την εκτέλεσή της*/
    public Prescription getPrescription() {
        return prescription;
    }
}
