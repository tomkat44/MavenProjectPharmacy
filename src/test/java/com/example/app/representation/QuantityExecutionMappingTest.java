package com.example.app.representation;

import com.example.app.domain.*;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.graalvm.polyglot.impl.AbstractPolyglotImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class QuantityExecutionMappingTest extends IntegrationBase {
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
        QuantityExecutionRepresentation dto = Fixture.getNewQuantityExecution();
        Assertions.assertNotNull(dto);

        QuantityExecution model = quantityExecutionMapper.toModel(dto);
        Assertions.assertNotNull(model.getId());
        Assertions.assertEquals(dto.id, model.getId());
        Assertions.assertEquals(dto.quantityExecutionPieces, model.getQuantityExecutionPieces());
        Assertions.assertEquals(dto.drug.drugName, model.getDrug().getDrugName());
        Assertions.assertEquals(dto.drug.id, model.getDrug().getId());
    }

    @Test
    @TestTransaction
    void testToRepresentation(){

        Drug drug = drugMapper.toModel(Fixture.getNewDrugRepresentation());
        PrescriptionExecution pe = new PrescriptionExecution("11112222");
        pe.setId(1111);

        QuantityExecution qe = new QuantityExecution( drug, pe, 12);
        qe.setId(8011);
        //System.out.println("PRESCRIPTION ID fot prescription Mapper Test = "+qe.getId());

        QuantityExecutionRepresentation qpr = quantityExecutionMapper.toRepresentation(qe);

        assertEquals(qpr.quantityExecutionPieces, qe.getQuantityExecutionPieces());
        assertEquals(qpr.drug.id, qe.getDrug().getId());
        assertEquals(qpr.drug.drugName, qe.getDrug().getDrugName());

    }


}
