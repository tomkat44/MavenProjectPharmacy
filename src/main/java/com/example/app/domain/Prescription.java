package com.example.app.domain;

import com.example.app.util.SystemDate;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
//import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "prescriptions")
public class Prescription {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    /*Στο παρακάτω ορίζω μία συσχέτιση πολλά προς πολλά και δημιουργώ έναν ενδιάμεσο πίνακα
    * Αυτός έσει σαν προτεύοντα κλειδιά τα id αυτών που είναι η συσχέτιση
    * Τέλος βάζω σε Μορφή INTERFACE τα φάρμακα
    * PERSIST και MERGE για να καταχορούνται σαν ένα*/
   /*$ @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE },
            fetch=FetchType.LAZY)
    @JoinTable(name="prescription_drug",
            joinColumns = {@JoinColumn(name="prescription_id")},
            inverseJoinColumns = {@JoinColumn(name="drug_id")}
    )
    private Set<Drug> drugs = new HashSet<Drug>();*/

    /*Δεν βάζω NULLABLE γιατί μία συνταγή μπορεί να μην εκτελεστεί ΠΟΤΕ
    * Cascade ALL διότι αν διαγράψω το Prescription θελω να διαγραφεί και το Execution*/


    /*Δεν βάζω NULLABLE γιατί μία συνταγή μπορεί να μην εκτελεστεί ΠΟΤΕ
     * Cascade ALL διότι αν διαγράψω το Prescription θελω να διαγραφεί και το Execution*/


    @Column(name="doctorAMKA", length=15, nullable=false)
    private String doctorAMKA;
    @Column(name="patientAMKA", length=15, nullable=false)
    private String patientAMKA;
    @Column(name="diagnosis", length=4000, nullable=false)
    private String diagnosis;

    @Column(name="creationDate", length=40)
    private String creationDate = LocalDate.now().toString();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctorToPrescription",nullable = false)
    private Doctor doctorToPrescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patientToPrescription", nullable = false)
    private Patient patientToPrescription;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "prescription_execution")
    private PrescriptionExecution prescriptionExecution;

    /*Σχέση πολλά προς 1 το οποίο πρέπει να είναι mappedBy = "prescription" όπου
     * αυτό είναι το ίδιο όνομα με τον πίνακα @ManyToOne στο QuantityPrescription*/
    @OneToMany(mappedBy = "prescription", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<QuantityPrescription> quantityPrescriptions = new HashSet<QuantityPrescription>();


    public Prescription(){
    }

    public Prescription(String doctorAMKA, String patientAMKA, String diagnosis) {
        this.doctorAMKA = doctorAMKA;
        this.patientAMKA = patientAMKA;
        this.diagnosis = diagnosis;
        this.creationDate = LocalDate.now().toString();
        //this.prescriptionExecution = prescriptionExecution;

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public Set<QuantityPrescription> getDrugs() {
        return new HashSet<>(quantityPrescriptions);
    }

    /*Θεν θέλω SetDrug αλλά θέλω ένα τρόπο να εισάγω και να διαγράφω Drugs από το Prescription*/
    /*public void addDrug(Drug drug) {
        QuantityPrescription qp = new QuantityPrescription(this, drug, );

        drug.addQuantityPrescription(quantityPrescriptions);
    drugs.add(drug);
    }*/


    /*Όταν θέλω να διαγράψω μόνο τις QuantityPrescriptions ενός Prescription χωρίς διαγραφή
    όλου του Prescription

    * Το Drug είναι ενψμένομένο με το Quantity prescription με OneToMany
    * Τώρα όταν καλέσω διαγράψω ένα φάρμακο από΄μία συνταγή τότε το Prescription
    * καλέι την μέθοδο drug.removeQuantityPrescription(drug.getQuantityPrescriptions())
    * με σκοπό να του εποστρέψει εκίνο το QP που είναι ίδιο με το ID του φαρμάκου
    * Επειτα΄καλείται η removeQuantityPrescription(qp); όπου χρησιμοποιεί την
    * προηγούμενη επιστροφή για να διαγράψει το κατάλληλο QP.
    * ΑΠοτέλεσμα να διαγράφεται το κατάλληλο QP*/
//    public void removeDrug(Drug drug) {
//        System.out.println("DRUG NAME = "+drug.getQuantityPrescriptions().size());
//        System.out.println("ID NAME = "+id);
//        if(drug != null){
//            QuantityPrescription qp  = drug.getQuantityPrescriptionFromDrug(id, drug.getQuantityPrescriptions());
//            //System.out.println("DRUG NAME = "+qp.getDrug().getDrugName());
//            removeQuantityPrescription(qp);
//            //$drugs.remove(drug);
//        }
//    }

    public void setQuantityPrescriptions(Set<QuantityPrescription> quantityPrescriptions) {
        this.quantityPrescriptions = quantityPrescriptions;
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

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }



    public boolean checkPharmacistGiveCorrectDrugs(List<QuantityPrescription> q_prescription, List<QuantityExecution> q_execution){
        boolean flag = true;

        /*Το βάζω σύμφωνα με το size του execution καθώς ο φαρμακοποιός μπορεί
        * να δώσει λιγότερα φάρμακα στον ασθενή. */
            for(int i=0; i<q_execution.size(); i++){
                System.out.println("q_prescription.DrugID != q_prescription.DrugID "+ q_prescription.get(i).getDrug().getId() +"\t"+ q_execution.get(i).getDrug().getId());
                //Μόνο με equals διότι με το == δεν το καταλαβαίνει
                /*Θα πρέπει όχι μόνο το φάρμακο να είναι το ίδιο αλλά θα πρέπει και η
                * ποσότητα που έδωσε ο φαρμακοποιός να έινια ίση ή μικρότερη από αυτή που έδωσε
                * ο γιατρός*/
                if(!(q_prescription.get(i).getDrug().getId()).equals(q_execution.get(i).getDrug().getId())
                && (q_prescription.get(i).getQuantityPrescription()) < (q_execution.get(i).getQuantityExecutionPieces())){

                    flag = false;
                }
        }

        return flag;
    }



    public void setExecutionFlag2() throws DomainException {

        List<QuantityPrescription> q_prescription = new ArrayList<>();
        for(QuantityPrescription qp: this.getQuantityPrescriptions()){
            System.out.println("q_prescription.add(qp);"+ qp.getId() +"\t"+ qp.getQuantityPrescription());
            q_prescription.add(qp);
        }

        List<QuantityExecution> q_execution = new ArrayList<>();
        for(QuantityExecution qe : this.getPrescriptionExecution().getQuantityExecutions()){
            System.out.println("q_execution.add(qe);"+ qe.getId()+"\t"+qe.getQuantityExecutionPieces());
            q_execution.add(qe);
        }

        this.getPrescriptionExecution().setExecutionFlag(q_execution, q_prescription);

    }

}


