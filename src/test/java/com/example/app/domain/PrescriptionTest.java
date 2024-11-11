package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PrescriptionTest {

    @Test
    public void checkPrescription(){
        Prescription prescription = new Prescription();
        prescription.setDiagnosis("igmoritida");
        assertEquals("igmoritida", prescription.getDiagnosis());
    }

    /*Δημιουργεί ένα 2 φάρμακα
    * Εισάγει Γιατρούς, Ασθενείς, Φάρμακα, QuantityPrescriptions
    * Διαγράφει ένα φάρμακο από κάθε pRescription και
    * ελέγχει ότι αυτόματα διαγράφηκαν και τα αντίστοιχα QuantityPrescriptions
    * Η αυτόματη διαγραφή πραγματοποιείται με DomainLogic*/
    @Test
    public void newPrescriptionNotExecuted(){
        Prescription prescription = new Prescription();
        Prescription prescription2 = new Prescription();

        Patient patient = new Patient();
        patient.setAmka("01010134567");

        prescription.setPatientAMKA(patient.getAmka());
        prescription.setPatientToPrescription(patient);
        prescription2.setPatientAMKA(patient.getAmka());
        prescription2.setPatientToPrescription(patient);

        Doctor doctor = new Doctor();
        doctor.setAmka("1111112345");

        prescription.setDoctorAMKA(doctor.getAmka());
        prescription.setDoctorToPrescription(doctor);
        prescription2.setDoctorAMKA(doctor.getAmka());
        prescription2.setDoctorToPrescription(doctor);

        //ADD Drugs
        Drug drug1 = new Drug();
        drug1.setDrugName("drug1");
        Drug drug2 = new Drug();
        drug2.setDrugName("drug2");
        Drug drug3 = new Drug();
        drug3.setDrugName("drug3");


        //ADD Quantity Prescription
        QuantityPrescription quantityPrescription1 = new QuantityPrescription(prescription, drug1, 10);
        QuantityPrescription quantityPrescription2 = new QuantityPrescription(prescription, drug2, 5);
        QuantityPrescription quantityPrescription3 = new QuantityPrescription(prescription, drug3, 1);

        prescription.addQuantityPrescription(quantityPrescription1);
        prescription.addQuantityPrescription(quantityPrescription2);
        prescription.addQuantityPrescription(quantityPrescription3);

        prescription2.addQuantityPrescription(quantityPrescription1);
        prescription2.addQuantityPrescription(quantityPrescription2);

        prescription.addDrug(drug1);
        prescription.addDrug(drug2);
        prescription.addDrug(drug3);

        prescription2.addDrug(drug1);
        prescription2.addDrug(drug2);

        assertEquals(3, prescription.getDrugs().size());
        assertTrue(prescription.getDrugs().contains(drug2));

        assertEquals(2, prescription2.getDrugs().size());

        drug1.addQuantityPrescription(quantityPrescription1);
        drug2.addQuantityPrescription(quantityPrescription2);
        drug3.addQuantityPrescription(quantityPrescription3);

       // assertEquals(1, drug1.getQuantityPrescriptions().size());

        assertEquals(3, prescription.getQuantityPrescriptions().size());
        assertTrue(prescription.getQuantityPrescriptions().contains(quantityPrescription3));

        assertEquals(2, prescription2.getQuantityPrescriptions().size());

        //REMOVE one Drug θέλω να δώ ότι απομακρύνεται και το QuantityPrescriptions
        prescription.removeDrug(drug3);
        assertEquals(2, prescription.getDrugs().size());
        assertFalse(prescription.getDrugs().contains(drug3));
        assertEquals(2, prescription.getQuantityPrescriptions().size());
        assertFalse(prescription.getQuantityPrescriptions().contains(quantityPrescription3));

        //REMOVE one Drug από το δεύτερο Prescription θέλω
        // να δώ ότι απομακρύνεται και το QuantityPrescriptions
        prescription2.removeDrug(drug2);
        assertEquals(1, prescription2.getDrugs().size());
        assertFalse(prescription2.getDrugs().contains(drug2));
        assertEquals(1, prescription2.getQuantityPrescriptions().size());
        assertFalse(prescription2.getQuantityPrescriptions().contains(quantityPrescription2));


    }





}
