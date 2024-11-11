package com.example.app.resource;

import com.example.app.representation.PrescriptionRepresentation;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.when;


@QuarkusTest
public class BustedPrescriptionTest extends IntegrationBase {


    @Test
    @TestTransaction
    public void listBustedPrescriptions() {
        List<PrescriptionRepresentation> qp_rep = when().get(Fixture.API_ROOT + Uri.BUSTEDPRESCRIPTIONS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PrescriptionRepresentation>>() {});


        Assertions.assertEquals(2, qp_rep.size());
        Assertions.assertEquals(7000, qp_rep.get(0).id);
        Assertions.assertEquals(7200, qp_rep.get(1).id);


    }



}
