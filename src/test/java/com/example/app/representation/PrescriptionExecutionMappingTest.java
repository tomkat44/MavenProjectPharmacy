package com.example.app.representation;

import com.example.app.domain.Drug;
import com.example.app.domain.Pharmacist;
import com.example.app.domain.PrescriptionExecution;
import com.example.app.domain.QuantityExecution;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PrescriptionExecutionMappingTest extends IntegrationBase {
    @Inject
    PrescriptionMapper prescriptionMapper;
    @Inject
    DoctorMapper doctorMapper;

    @Inject
    PrescriptionExecutionMapper prescriptionExecutionMapper;

    @Inject
    PharmacistMapper pharmacistMapper;

    @Inject
    QuantityExecutionMapper quantityExecutionMapper;

    @Inject
    DrugMapper drugMapper;

    @Test
    @TestTransaction
    void testToModel() {
        PrescriptionExecutionRepresentation dto = Fixture.getPrescriptionExecution();
        Assertions.assertNotNull(dto);

        QuantityExecution qe = quantityExecutionMapper.toModel(Fixture.getNewQuantityExecution());
        List<QuantityExecution> qes = new ArrayList<>();
        Assertions.assertNotNull(qes);
        System.out.println("qe.getQuantityExecutionPieces() = "+ qe.getQuantityExecutionPieces());
        qes.add(qe);

        PrescriptionExecution model = prescriptionExecutionMapper.toModel(dto);
        //Assertions.assertNotNull(model.getId());
        Assertions.assertEquals(dto.id, model.getId());
        Assertions.assertEquals(dto.executionDate, model.getExecutionDate());

        Assertions.assertEquals(dto.quantityExecutions.size(), 1);
        Assertions.assertEquals(dto.quantityExecutions.get(0).quantityExecutionPieces, 2);



        //Assertions.assertEquals(dto.executionFlag, model.isExecutionFlag());
        //Το έβγαλα μετο χέρι και είναι price=8.88 * quantity=11 * ORIGINAL=0.02
        Assertions.assertEquals(1.9536000000000002, model.getSummaryCost(qes));

    }

    @Test
    @TestTransaction
    void testToRepresentation(){

        Drug drug = drugMapper.toModel(Fixture.getNewDrugRepresentation());
        Pharmacist pharmacist = pharmacistMapper.toModel(Fixture.getExistingPharmacistRepresentation());
        PrescriptionExecution pe = new PrescriptionExecution("11112222");
        pe.setId(1111);
        pe.setPharmacist(pharmacist);

        QuantityExecution qe = new QuantityExecution( drug, pe, 12);
        qe.setId(8011);
        System.out.println("PRESCRIPTION ID fot prescription Mapper Test = "+qe.getId());

        qe = quantityExecutionMapper.toModel(Fixture.getNewQuantityExecution());
        List<QuantityExecution> qes = new ArrayList<>();
        qes.add(qe);
        pe.setSummaryCost(pe.getSummaryCost(qes));
//        System.out.println("PRESCRIPTION EXECUTION  COST f = "+pe.getSummaryCost(qes));
//        System.out.println("PRESCRIPTION EXECUTION  COST f = "+pe.getPharmacist().getEmail());

        PrescriptionExecutionRepresentation per = prescriptionExecutionMapper.toRepresentation(pe);


        assertEquals(per.pharmacist.email, pe.getPharmacist().getEmail());
        assertEquals(per.executionFlag, pe.isExecutionFlag());
        //assertEquals(per.summaryCosts, pe.getSummaryCost());

    }

}
