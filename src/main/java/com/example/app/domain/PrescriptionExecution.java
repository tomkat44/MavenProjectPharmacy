package com.example.app.domain;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "prescriptionExecutions")
public class PrescriptionExecution {


    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name="pharmacistAFM", length=15)
    private String pharmacistAFM;

    @Column(name="executionDate", length=40, nullable=false)
    private String executionDate;// = SystemDate.now();

    @Column(name="summaryCost", length=15)
    private Double summaryCost;

    @Enumerated(EnumType.STRING)
    @Column(name="executionFlag", length=15)
    private executionPrescriptionFlag executionFlag;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "pharmacist_id")
    private Pharmacist pharmacist;

    //Σύνδεση με το QuantityExecution
    @OneToMany(mappedBy = "prescriptionExecution", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuantityExecution> quantityExecutions = new HashSet<QuantityExecution>();


    /*Το θέλω για να μπορώ να πάρω τα στοιχεία της συνταγής μεσα από την εκτέλεσή της*/
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription")
    private Prescription prescription;

    public PrescriptionExecution(){
    }

    public PrescriptionExecution(String pharmacistAFM) {
        this.pharmacistAFM = pharmacistAFM;
        this.executionDate = LocalDate.now().toString();
        this.executionFlag = executionPrescriptionFlag.PENDING;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDoctorAMKA() {
        return pharmacistAFM;
    }

    public void setDoctorAMKA(String doctorAMKA) {
        this.pharmacistAFM = doctorAMKA;
    }

    public String getPharmacistAFM() {
        return pharmacistAFM;
    }

    public void setPharmacistAFM(String pharmacistAFM) {
        this.pharmacistAFM = pharmacistAFM;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public Double getSummaryCost() {
        return summaryCost;
    }

    /*Υλοποιείται το συνολικό κόστος με τρόπο τέτοιο ώστε να υπολογίζεται και το κόστος με την έκπτωση
    * ανάλογα με το αν το φάρμακι είνια προτώτυπο ή γενόσημο*/
    public double getSummaryCost(List<QuantityExecution> qe ) {
        summaryCost=0.0;
        for (QuantityExecution qes : qe){
            if(qes.getDrug().getMedicineCategory()==medicineCategory.ORIGINALS){
                summaryCost = summaryCost + (qes.getQuantityExecutionPieces()*qes.getDrug().getDrugPrice())*0.02;
            } else {
                summaryCost = summaryCost + (qes.getQuantityExecutionPieces()*qes.getDrug().getDrugPrice())*0.10;
            }
        }
        return summaryCost;
    }

    public void setSummaryCost(Double summaryCost) {
        this.summaryCost = summaryCost;
    }

    public executionPrescriptionFlag getExecutionFlag() {
        return executionFlag;
    }

    public void setExecutionFlag(executionPrescriptionFlag executionFlag) {
        this.executionFlag = executionFlag;
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
            flag = true;
        } else {
            for (int i = 0; i < q_prescription.size(); i++) {
                if (q_execution.get(i).getQuantityExecutionPieces() > q_prescription.get(i).getQuantityPrescription()) {
                    throw new DomainException("QuantityExecution Pieces is Higher than Doctors QuantityPrescription");
                } else if (q_execution.get(i).getQuantityExecutionPieces() < q_prescription.get(i).getQuantityPrescription()) {
                    flag = false;
                    //break;
                } else {
                    if(flag!=false) {
                        flag = true;
                    }
                }//if
            }//for
            if (flag) {
                executionFlag = executionPrescriptionFlag.EXECUTED;
            } else {
                executionFlag = executionPrescriptionFlag.PARTIALLY;
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

    public void setQuantityExecutions(Set<QuantityExecution> quantityExecutions) {
        this.quantityExecutions = quantityExecutions;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }


    public void addQuantityExecution(QuantityExecution quantityExecution) {
        quantityExecutions.add(quantityExecution);
    }
    public void removeQuantityExecution(QuantityExecution quantityExecution) {
        quantityExecutions.remove(quantityExecution);
    }


}
