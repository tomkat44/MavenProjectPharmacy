package com.example.app.representation;

import com.example.app.domain.Address;
import com.example.app.domain.Pharmacist;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PharmacistMappingTest extends IntegrationBase {
    @Inject
    PharmacistMapper pharmacistMapper;

    @Test
    @TestTransaction
    void toModel() {
        PharmacistRepresentation dto = Fixture.getPharmacistRepresentation();
        Assertions.assertNotNull(dto);

        Pharmacist model = pharmacistMapper.toModel(dto);

        Assertions.assertEquals(dto.firstName, model.getFirstName());
        Assertions.assertEquals(dto.lastName, model.getLastName());
        Assertions.assertEquals(dto.email, model.getEmail());
        Assertions.assertEquals(dto.id, model.getId());

    }

    @Test
    @Transactional
    void testToRepresentation(){
        Pharmacist pharmacist = new Pharmacist("Chloe", "Marg",  "144445555", "cd@ph.ph", null);
        pharmacist.setId(4005);
        Address ad = new Address("kolokotroni", "111", "Athens", null, null);
        pharmacist.setAddress(ad);

        PharmacistRepresentation pharmacistRepresentation = pharmacistMapper.toRepresentation(pharmacist);
        assertEquals(pharmacist.getId(), pharmacistRepresentation.id);
        assertEquals(pharmacist.getFirstName(), pharmacistRepresentation.firstName);
        assertEquals(pharmacist.getLastName(), pharmacistRepresentation.lastName);
        assertEquals(pharmacist.getEmail(), pharmacistRepresentation.email);
        assertEquals(pharmacist.getAfm(), pharmacistRepresentation.afm);

        assertEquals(pharmacist.getAddress().getNumber(), "111");

    }
}
