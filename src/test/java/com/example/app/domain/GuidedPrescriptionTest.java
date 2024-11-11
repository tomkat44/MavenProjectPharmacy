package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GuidedPrescriptionTest {

    //domain logic
    //υλοποιηση της isInsideLimit()
    @Test
    public void checkPrescriptionLimits() {

        Prescription prescription = new Prescription();

        Patient patient = new Patient();
        patient.setAmka("01010134567");

        prescription.setPatientAMKA(patient.getAmka());
        prescription.setPatientToPrescription(patient);

        Doctor doctor = new Doctor();
        doctor.setAmka("1111112345");

        prescription.setDoctorAMKA(doctor.getAmka());
        prescription.setDoctorToPrescription(doctor);

        ActiveSubstance as1 = new ActiveSubstance();
        as1.setName("Paraketamol");
        as1.setQuantity(10);

        ActiveSubstance as2 = new ActiveSubstance();
        as2.setName("Nabilon");
        as2.setQuantity(2);

        //ADD Drugs
        Drug drug1 = new Drug();
        drug1.setDrugName("Depon");
        drug1.setActiveSubstance(as1);
        Drug drug2 = new Drug();
        drug2.setActiveSubstance(as2);
        drug2.setDrugName("Lurofen");


        //ADD Quantity Prescription
        QuantityPrescription quantityPrescription1 = new QuantityPrescription(prescription, drug1, 9);
        QuantityPrescription quantityPrescription2 = new QuantityPrescription(prescription, drug2, 5);

        prescription.addQuantityPrescription(quantityPrescription1);
        prescription.addQuantityPrescription(quantityPrescription2);

        //$prescription.addDrug(drug1);
        //$prescription.addDrug(drug2);

        //Φέρνει εκτός σειράς οπότε έτσι
        QuantityPrescription [] Quant = (prescription.getQuantityPrescriptions()).toArray(new QuantityPrescription[prescription.getQuantityPrescriptions().size()]);
        assertNotEquals(Quant[0].isInsideLimit(),Quant[1].isInsideLimit());
    }

}
