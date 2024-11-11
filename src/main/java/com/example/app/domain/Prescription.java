package com.example.app.domain;

import com.example.app.util.SystemDate;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    /*Στο παρακάτω ορίζω μία συσχέτιση πολλά προς πολλά και δημιουργώ έναν ενδιάμεσο πίνακα
    * Αυτός έσει σαν προτεύοντα κλειδιά τα id αυτών που είναι η συσχέτιση
    * Τέλος βάζω σε Μορφή INTERFACE τα φάρμακα
    * PERSIST και MERGE για να καταχορούνται σαν ένα*/
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            fetch=FetchType.LAZY)
    @JoinTable(name="prescription_drug",
            joinColumns = {@JoinColumn(name="prescription_id")},
            inverseJoinColumns = {@JoinColumn(name="drug_id")}
    )
    private Set<Drug> drugs = new HashSet<Drug>();

    /*Σχέση πολλά προς 1 το οποίο πρέπει να είναι mappedBy = "prescription" όπου
    * αυτό είναι το ίδιο όνομα με τον πίνακα @ManyToOne στο QuantityPrescription*/
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<QuantityPrescription> quantityPrescriptions = new HashSet<QuantityPrescription>();

    //ΠΡΟΣΟΧΗ Θέλει ΥΠΟΧΡΕΩΤΙΚΑ να βάλω ιατρό.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctorToPrescription", nullable = false)
    private Doctor doctorToPrescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientToPrescription", nullable = false)
    private Patient patientToPrescription;


    /*Δεν βάζω NULLABLE γιατί μία συνταγή μπορεί να μην εκτελεστεί ΠΟΤΕ
    * Cascade ALL διότι αν διαγράψω το Prescription θελω να διαγραφεί και το Execution*/
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_execution")
    private PrescriptionExecution prescriptionExecution;

    @Column(name="doctorAMKA", length=15, nullable=false)
    private String doctorAMKA;
    @Column(name="patientAMKA", length=15, nullable=false)
    private String patientAMKA;
    @Column(name="diagnosis", length=4000, nullable=false)
    private String diagnosis;

    @Column(name="creationDate", length=40, nullable=false)
    private LocalDate creationDate = SystemDate.now();


    public Prescription(){
    }

    public Prescription(String doctorAMKA, String patientAMKA, String diagnosis) {
        this.doctorAMKA = doctorAMKA;
        this.patientAMKA = patientAMKA;
        this.diagnosis = diagnosis;
        //this.prescriptionExecution = prescriptionExecution;

    }


    public Integer getId() {
        return id;
    }

    public String getDoctorAMKA() {
        return doctorAMKA;
    }

    public void setDoctorAMKA(String doctorAMKA) {
        this.doctorAMKA = doctorAMKA;
    }

    public String getPatientAMKA() {
        return patientAMKA;
    }

    public void setPatientAMKA(String patientAMKA) {

        this.patientAMKA = patientAMKA;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    //Getter - Setter για το @Many to One doctorToPrescription
    public Doctor getDoctorToPrescription() {
        return doctorToPrescription;
    }
    public void setDoctorToPrescription(Doctor doctorToPrescription) {
        this.doctorToPrescription = doctorToPrescription;
    }

    //Getter - Setter για το @Many to One patientToPrescription
    public Patient getPatientToPrescription() {
        return patientToPrescription;
    }
    public void setPatientToPrescription(Patient patientToPrescription) {
        this.patientToPrescription = patientToPrescription;
    }

    // @OneToOne για PrescriptionExecution
    public PrescriptionExecution getPrescriptionExecution() {
        return prescriptionExecution;
    }
    public void setPrescriptionExecution(PrescriptionExecution prescriptionExecution) {
        this.prescriptionExecution = prescriptionExecution;
    }


    /*Θέλω να μπορώ να τραβάω τα Drug που τα έχω κάνει interface και γίνεται με το παρακάτω*/
    public Set<Drug> getDrugs() {
        return new HashSet<>(drugs);
    }

    /*Θεν θέλω SetDrug αλλά θέλω ένα τρόπο να εισάγω και να διαγράφω Drugs από το Prescription*/
    public void addDrug(Drug drug) {
                    drugs.add(drug);
    }


    /*Όταν θέλω να διαγράψω μόνο τις QuantityPrescriptions ενός Prescription χωρίς διαγραφή
    όλου του Prescription

    * Το Drug είναι ενψμένομένο με το Quantity prescription με OneToMany
    * Τώρα όταν καλέσω διαγράψω ένα φάρμακο από΄μία συνταγή τότε το Prescription
    * καλέι την μέθοδο drug.removeQuantityPrescription(drug.getQuantityPrescriptions())
    * με σκοπό να του εποστρέψει εκίνο το QP που είναι ίδιο με το ID του φαρμάκου
    * Επειτα΄καλείται η removeQuantityPrescription(qp); όπου χρησιμοποιεί την
    * προηγούμενη επιστροφή για να διαγράψει το κατάλληλο QP.
    * ΑΠοτέλεσμα να διαγράφεται το κατάλληλο QP*/
    public void removeDrug(Drug drug) {
        if(drug != null){
            QuantityPrescription qp  = drug.getQuantityPrescriptionFromDrug(id, drug.getQuantityPrescriptions());
            //System.out.println("DRUG NAME = "+qp.getDrug().getDrugName());
            removeQuantityPrescription(qp);
            drugs.remove(drug);
        }
    }

    /*Θα επιστρέφει την κίστα με τα Quantity Prescriptions για τον καθορισμό που αριθμού του σκευάσματος
    * Επιπλέων θα μπορώ να εισάγω τιμές αριθμού σκευασμάτων */
    public Set<QuantityPrescription> getQuantityPrescriptions() {
        return new HashSet<>(quantityPrescriptions);
    }

    /*Εδώ θέλω να εισάγω μία ΠΟΣΟΤΗΤΑ φαρμάκου μέσα στην συνταγή που χρησιμοποιείται από το Test*/
    public void addQuantityPrescription(QuantityPrescription quantityPrescription) {
        if(quantityPrescription != null) {
            quantityPrescriptions.add(quantityPrescription);
        }
    }
    public void removeQuantityPrescription(QuantityPrescription quantityPrescription) {
        if(quantityPrescription != null){
            quantityPrescriptions.remove(quantityPrescription);
        }
    }

}


