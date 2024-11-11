package com.example.app.representation;

import com.example.app.domain.*;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class QuantityPrescriptionMappingTest extends IntegrationBase {
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
        QuantityPrescriptionRepresentation dto = Fixture.getQuantityPrescription();
        Assertions.assertNotNull(dto);

        QuantityPrescription model = quantityPrescriptionMapper.toModel(dto);
        Assertions.assertNotNull(model.getId());
        Assertions.assertEquals(dto.drug.drugName, model.getDrug().getDrugName());
        Assertions.assertEquals(dto.quantityPrescription, model.getQuantityPrescription());
    }

    @Test
    @TestTransaction
    void testToModel2() {
        QuantityPrescriptionRepresentation dto = Fixture.getExistingQuantityPrescription();
        Assertions.assertNotNull(dto);

        QuantityPrescription model = quantityPrescriptionMapper.toModel(dto);
        Assertions.assertNotNull(model.getId());
        Assertions.assertEquals(dto.drug.drugName, model.getDrug().getDrugName());
        Assertions.assertEquals(dto.quantityPrescription, model.getQuantityPrescription());
    }

    @Test
    @TestTransaction
    void testToRepresentation(){

        Drug drug = drugMapper.toModel(Fixture.getNewDrugRepresentation());
        Prescription prescription = new Prescription("11112222", "33334444", "throughtpain");

        QuantityPrescription qp = new QuantityPrescription(prescription, drug, 12);
        qp.setId(8011);
        System.out.println("PRESCRIPTION ID fot prescription Mapper Test = "+qp.getId());

        QuantityPrescriptionRepresentation qpr = quantityPrescriptionMapper.toRepresentation(qp);

        assertEquals(qpr.quantityPrescription, qp.getQuantityPrescription());
        assertEquals(qpr.drug.id, qp.getDrug().getId());
        assertEquals(qpr.drug.drugName, qp.getDrug().getDrugName());

    }


}
