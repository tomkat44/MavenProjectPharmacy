package com.example.app.representation;

import com.example.app.domain.*;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PrescriptionMappingTest extends IntegrationBase {
    @Inject
    PrescriptionMapper prescriptionMapper;
    @Inject
    DoctorMapper doctorMapper;

    @Inject
    PrescriptionExecutionMapper prescriptionExecutionMapper;

    @Inject
    QuantityPrescriptionMapper quantityPrescriptionMapper;

    @Inject
    QuantityExecutionMapper quantityExecutionMapper;

    @Inject
    DrugMapper drugMapper;

    @Test
    @TestTransaction
    void testToModel() {
        PrescriptionRepresentation dto = Fixture.getPrescriptionRepresentation();
        Assertions.assertNotNull(dto);

        Prescription model = prescriptionMapper.toModel(dto);
        //Assertions.assertNotNull(model.getId());
        Assertions.assertEquals(dto.diagnosis, model.getDiagnosis());
        Assertions.assertEquals(dto.id, model.getId());

        Assertions.assertEquals(dto.doctorToPrescription.email, model.getDoctorToPrescription().getEmail());


        Assertions.assertEquals(dto.quantityPrescriptions.size(), model.getQuantityPrescriptions().size());
        Assertions.assertEquals(dto.quantityPrescriptions.get(0).drug.drugName, "Depon");
    }

    @Test
    @TestTransaction
    void testToRepresentation(){
        Prescription prescription = new Prescription("11112222", "33334444", "throughtpain");
        prescription.setId(6012);
        System.out.println("PRESCRIPTION ID fot prescription Mapper Test = "+prescription.getId());

        Doctor doctor = doctorMapper.toModel(Fixture.getDoctorRepresentation());
        prescription.setDoctorToPrescription(doctor);

        QuantityPrescription qp = quantityPrescriptionMapper.toModel(Fixture.getQuantityPrescription());
        prescription.addQuantityPrescription(qp);

        PrescriptionExecution pe = prescriptionExecutionMapper.toModel(Fixture.getPrescriptionExecution());
        prescription.setPrescriptionExecution(pe);

        PrescriptionRepresentation p = prescriptionMapper.toRepresentation(prescription);
        assertEquals(p.doctorAMKA, prescription.getDoctorAMKA());
        assertEquals(p.quantityPrescriptions.size(), prescription.getQuantityPrescriptions().size());
        assertEquals(p.prescriptionExecution.pharmacist.email, prescription.getPrescriptionExecution().getPharmacist().getEmail());
        //assertEquals(p.prescriptionExecution.executionDate, LocalDate.now());

        assertEquals(p.quantityPrescriptions.size(), prescription.getQuantityPrescriptions().size());
        assertEquals(p.quantityPrescriptions.get(0).drug.id, 6010);
    }


}
