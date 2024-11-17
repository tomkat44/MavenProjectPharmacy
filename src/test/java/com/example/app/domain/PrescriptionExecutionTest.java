package com.example.app.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PrescriptionExecutionTest {

    LocalDate now = LocalDate.of(2022, 11, 16);

    @Test
    public void createPrescriptionExecution(){
        PrescriptionExecution prescriptionExecution = new PrescriptionExecution("11112222");
        prescriptionExecution.setExecutionDate(LocalDate.now().toString());
        assertEquals(LocalDate.now().toString(), prescriptionExecution.getExecutionDate());

    }

    @Test
    public void executePrescription(){
        Prescription prescription = new Prescription();
        Drug drug = new Drug();
        drug.setDrugName("Depon");

        QuantityPrescription quantityPrescription = new QuantityPrescription(prescription, drug, 10);
        prescription.addQuantityPrescription(quantityPrescription);
        List<QuantityPrescription> qpList = new ArrayList<>();
        qpList.add(quantityPrescription);

        drug.addQuantityPrescription(quantityPrescription);
        //$prescription.addDrug(drug);

        assertEquals(drug.getQuantityPrescriptions(), prescription.getQuantityPrescriptions());
        assertEquals(1, prescription.getDrugs().size());

        PrescriptionExecution prescriptionExecution = new PrescriptionExecution("11112222");
        prescriptionExecution.setExecutionDate(LocalDate.now().toString());

        QuantityExecution quantityExecution = new QuantityExecution(drug, prescriptionExecution, 12);
        List<QuantityExecution> qeList = new ArrayList<>();
        qeList.add(quantityExecution);

        try {
            prescriptionExecution.setExecutionFlag(qeList, qpList);
            assertEquals(executionPrescriptionFlag.EXECUTED, prescriptionExecution.isExecutionFlag());
        } catch (DomainException e) {
            assertEquals("QuantityExecution Pieces is Higher than Doctors QuantityPrescription", e.getMessage());
            //throw new RuntimeException(e);
        }

        prescription.setPrescriptionExecution(prescriptionExecution);

    }



}
