package com.example.app.representation;

import com.example.app.domain.Doctor;
import com.example.app.domain.Pharmacist;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class DoctorMappingTest extends IntegrationBase {
    @Inject
    DoctorMapper doctorMapper;

    @Test
    @TestTransaction
    void toModel() {
        DoctorRepresentation dto = Fixture.getDoctorRepresentation();
        Assertions.assertNotNull(dto);

        Doctor model = doctorMapper.toModel(dto);

        Assertions.assertEquals(dto.firstName, model.getFirstName());
    }


}
