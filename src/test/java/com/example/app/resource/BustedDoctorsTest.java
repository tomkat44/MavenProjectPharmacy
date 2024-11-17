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

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;


@QuarkusTest
public class BustedDoctorsTest extends IntegrationBase {


    @Test
    @TestTransaction
    public void listBustedDoctors() {
        List<PrescriptionRepresentation> qp_rep = when().get(Fixture.API_ROOT + Uri.BUSTEDDOCTORS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PrescriptionRepresentation>>() {});

        
        Assertions.assertEquals(4, qp_rep.size());
        Assertions.assertEquals("15020201111", qp_rep.get(0).doctorAMKA);
        Assertions.assertEquals("15020203333", qp_rep.get(1).doctorAMKA);
        Assertions.assertEquals("15020203333", qp_rep.get(2).doctorAMKA);
        Assertions.assertEquals("15020202222", qp_rep.get(3).doctorAMKA);


    }



}
