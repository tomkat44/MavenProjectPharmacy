package com.example.app.representation;

import com.example.app.domain.ActiveSubstance;
import com.example.app.resource.Uri;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

@QuarkusTest
public class ActiveSubstanceMappingTest extends IntegrationBase {
@Inject
ActiveSubstanceMapper activeSubstanceMapper;


    @Test
    @TestTransaction
    void toModel() {

        ActiveSubstanceRepresentation dto = Fixture.getNewActiveSubstanceRepresentation();
        Assertions.assertNotNull(dto);

        ActiveSubstance model = activeSubstanceMapper.toModel(dto);
        Assertions.assertEquals(dto.id, model.getId());
        Assertions.assertEquals(dto.substanceName, model.getName());
        Assertions.assertEquals(dto.quantity, model.getQuantity());

    }

    @Test
    @TestTransaction
    void toRepresentation() {


    }


}
