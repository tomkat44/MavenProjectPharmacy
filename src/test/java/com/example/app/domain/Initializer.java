package com.example.app.domain;

import com.example.app.persistence.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import javax.persistence.Query;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * Κλάση που αναλαμβάνει να αρχικοποιήσει τα δεδομένα της βάσης δεδομένων<p>
 * Βοηθητική κλάση που παρέχει δεδομένα για τα παραδείγματα και τις δοκιμασίες ελέγχου<p>
 */
public class Initializer {

    //διαγράφουμε όλα τα δεδομένα στη βάση δεδομένων
    public void  eraseData() {
        EntityManager em = JPAUtil.getCurrentEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        /*Πρέπει πρώτα να διαγραφεί το φάρμακο και μετά την δραστική Ουσία
        * διότι αν το έκανα ανάποδα δεν θα έδειχναν πουθενά */
        Query query;

        query = em.createNativeQuery("delete from authentications");
        query.executeUpdate();

        query = em.createNativeQuery("delete from prescription_drug");
        query.executeUpdate();

        query = em.createNativeQuery("delete from quantityExecutions");
        query.executeUpdate();

        //Πρώτα δαιγράφω την ενδιάμεση ΣΥΣΧΕΤΙΣΗ και μετά τα DRUG & PRESCRIPTION
        query = em.createNativeQuery("delete from quantityPrescriptions");
        query.executeUpdate();

        query = em.createNativeQuery("delete from prescriptions");
        query.executeUpdate();

        query = em.createNativeQuery("delete from drugs");
        query.executeUpdate();

        query = em.createNativeQuery("delete from activeSubstances");
        query.executeUpdate();

        query = em.createNativeQuery("delete from prescriptionExecutions");
        query.executeUpdate();

        query = em.createNativeQuery("delete from doctors");
        query.executeUpdate();

        query = em.createNativeQuery("delete from pharmacists");
        query.executeUpdate();

        query = em.createNativeQuery("delete from patients");
        query.executeUpdate();

        tx.commit();
        em.close();
    }


    public void prepareData() throws DomainException {

        // πριν εισάγουμε τα δεδομένα διαγράφουμε ότι υπάρχει
        eraseData();

        //Γιατροί
        Doctor dr1 = new Doctor("John", "Papadopoulos",  "15020201111", "144441111", "john@doctor.com", "d1111");
        Doctor dr2 = new Doctor("Max", "Tasopoulos",  "15020202222", "144442222", "max@doctor.com", "d2222");
        Doctor dr3 = new Doctor("Christina", "Xoleva",  "15020203333", "144443333", "chris@doctor.com", "d3333");

        //Ασθενείς
        Patient ip1 = new Patient("Spyros", "Alexandrou", "12121201112", "spy@patient.com", "ip1111" );
        Patient ip2 = new Patient("Laoura", "Narges", "12121202222", "laoura@patient.com", "ip2222" );

        //Φαρμακοποιοί
        Pharmacist ph1 = new Pharmacist("Maria", "Damiges", "133331111", "maria@pharmacist.com", "ph1111");
        Pharmacist ph2 = new Pharmacist("Don", "Volikas", "133332222", "don@pharmacist.com", "ph2222");

        //auhtentication users
        Authentication auth = new Authentication(dr1.getEmail(),dr1.getPassword());
        Authentication auth2 = new Authentication(ph1.getEmail(), ph2.getPassword());

        //Δραστικές ουσίες
        ActiveSubstance a11 = new ActiveSubstance("Paraketamol", 10);
        ActiveSubstance a12 = new ActiveSubstance("Nabilon", 2);
        ActiveSubstance a13 = new ActiveSubstance("Povidoni", 100);

        //Drugs και εισαγωγή σε αυτά των ActiveSubstance
        Drug d11 = new Drug("Depon", 5.44, medicineCategory.ORIGINALS);
        d11.setActiveSubstance(a11);

        Drug d12 = new Drug("Lourofen", 8.44, medicineCategory.GENERICS);
        d12.setActiveSubstance(a12);

        Drug d13 = new Drug("Betedin", 2.88, medicineCategory.GENERICS);
        d13.setActiveSubstance(a13);


        //############### Δημιουργία 1ης συνταγής με την εκτέλεσή της #####################
        Prescription p11 = new Prescription(dr1.getAmka(), ip1.getAmka(), "headacke");
        p11.setDoctorToPrescription(dr1); //doctor to Prescription
        p11.setPatientToPrescription(ip1); //patient to prescription
        p11.addDrug(d11); //Εφοδιασμός μίας συνταγής με ένα φάρμακο

        //Δημιουργώ την καταχώρηση που θα έχει τις ΠΟΣΟΤΗΤΕΣ των φαρμακών που αναγράφονται στην Prescription
        QuantityPrescription qp11 = new QuantityPrescription(p11, d11, 4);
        p11.addQuantityPrescription(qp11);
        //Εισαγωγή δευτέρου Drug
        p11.addDrug(d12);
        QuantityPrescription qp12 = new QuantityPrescription(p11, d12, 8);
        p11.addQuantityPrescription(qp12);

        //@@@Εκτέλεση 1ης Συνταγής
        PrescriptionExecution pe1 = new PrescriptionExecution(ph1.getAfm(), LocalDate.now());
        pe1.setPharmacist(ph1);



        //Φτιάχω και εισάγω στο EXECUTION πόσα τελικά φάρμακα λήφθηκαν από τον φαρμακοποιό
        QuantityExecution qe1 = new QuantityExecution(d11, pe1, 4 );

        QuantityExecution qe2 = new QuantityExecution(d12, pe1, 7 );


        //Δημιουργία λίστας με έλεγχο φαρμάκων που παραλήφθηκαν
        List<QuantityExecution> qeCheck = new ArrayList<>();
        List<QuantityPrescription> qpCheck = new ArrayList<>();
        qeCheck.add(qe1);
        qeCheck.add(qe2);
        qpCheck.add(qp11);
        qpCheck.add(qp12);

        try {
            pe1.setExecutionFlag(qeCheck, qpCheck);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Εισαγωγή των 2 Execution στο Prescription
        pe1.addQuantityExecution(qe1);
        pe1.addQuantityExecution(qe2);

        //Εισαγωγή της εκτέλεσης στην Prescription
        p11.setPrescriptionExecution(pe1);



        //##################  Δημιουργίά 2ης Prescription χωρίς εκτέλεση#######################
        Prescription p12 = new Prescription(dr2.getAmka(), ip2.getAmka(), "osfialgia");
        p12.setDoctorToPrescription(dr2);
        p12.setPatientToPrescription(ip2);
        p12.addDrug(d13);
        QuantityPrescription qp13 = new QuantityPrescription(p12, d13, 12);
        p12.addQuantityPrescription(qp13);


        //##################  Δημιουργίά 3ης Prescription χωρίς εκτέλεση#######################
        Prescription p13 = new Prescription(dr3.getAmka(), ip1.getAmka(), "brokenLeg");
        p13.setDoctorToPrescription(dr3);
        p13.setPatientToPrescription(ip1);
        p13.addDrug(d11);
        QuantityPrescription qp14 = new QuantityPrescription(p13, d11, 20);
        p13.addQuantityPrescription(qp14);


        EntityManager em;
        em = JPAUtil.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        /*Πρέπει πρώτα να αποθηκευτεί η Δραστική ουσία και μετα΄το ΦΑΡΜΑΚΟ*/
        em.persist(a11);
        em.persist(a12);
        em.persist(a13);

        //εισαγωγή των ιατρών
        em.persist(dr1);
        em.persist(dr2);
        em.persist(dr3);

        //εισαγωγή των patient
        em.persist(ip1);
        em.persist(ip2);

        //εισαγωγή των Pharmacist
        em.persist(ph1);
        em.persist(ph2);

        //να αποθηκευτεί πρώτα o user (Authentication)
        em.persist(auth);
        em.persist(auth2);

        //PRESCRIPTIONS
        em.persist(p11);
        em.persist(p12);
        em.persist(p13);

        tx.commit();
        //em.close();


        //  #####################     DEBUGGING
        /*
        System.out.println("\n*********** START INITIALIZER DEBUGGING*****************\n");
        Query query;
        query = em.createQuery("select p from Prescription p where prescription_execution is not null");
        List<Prescription> prescriptions = query.getResultList();
        assertEquals(1, prescriptions.size());

        //Καθολική μεταβλητή για να λάβω μέσα από την FOR τις ποσότητες
       // List<QuantityPrescription> q_Check;

        System.out.println("\n*********** PRESCRIPTION INITIALIZER*****************\n");
        for (Prescription prescription : prescriptions) {
            System.out.println("Id (prescription) = " + prescription.getId());
            System.out.println("AMKA Doctor = " + prescription.getDoctorAMKA());
            System.out.println("AMKA Patient = " + prescription.getPatientAMKA());
            System.out.println("Diagnosi = " + prescription.getDiagnosis());
            System.out.println("\n");

            query = em.createQuery("select qp from QuantityPrescription qp where prescription_id = '" + prescription.getId() + "'");
            List<QuantityPrescription> qps = query.getResultList();

            System.out.println("\n************ QUANTITY PRESCRIPTION INITIALIZER *************\n");
            for (QuantityPrescription qp : qps) {
                System.out.println("Id Quantity = " + qp.getId());
                System.out.println("Quantity = " + qp.getQuantityPrescription());
                System.out.println("Quantity pre_id = " + qp.getPrescription().getId());
                System.out.println("Quantity Drug_id= " + qp.getDrug().getId());
                System.out.println("Quantity Drug_name= " + qp.getDrug().getDrugName());
                System.out.println("\n");
            }

            System.out.println("\n***********DRUG*****************\n");
            query = em.createQuery("select d from Drug d ");
            List<Drug> d = query.getResultList();

            for (Drug ds : d) {
                System.out.println("Id DRUG = " + ds.getId());
                System.out.println("DRUG name = " + ds.getDrugName());
                System.out.println("\n");
            }

        }//DEBUGING
        System.out.println("\n*********** END OF INITIALIZER DEBUGGING*****************\n");
   */
    }
}
