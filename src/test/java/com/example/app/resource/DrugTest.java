package com.example.app.resource;


import com.example.app.representation.DrugRepresentation;
import com.example.app.representation.PatientRepresentation;
import com.example.app.representation.PharmacistRepresentation;
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


import javax.ws.rs.core.Response;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class DrugTest extends IntegrationBase {

    @Test
    @TestTransaction
    public void find() {
        DrugRepresentation drug = when().get(Fixture.API_ROOT + Uri.DRUG + "/" + 6001)
                .then()
                .statusCode(200)
                .extract().as(DrugRepresentation.class);
        Assertions.assertEquals(6001, drug.id);
        Assertions.assertEquals("Lourofen", drug.drugName);
        Assertions.assertEquals(5001, drug.activeSubstance.id);




    }

    @Test
    @TestTransaction
    public void listAll() {
        List<DrugRepresentation> drug = when().get(Fixture.API_ROOT + Uri.DRUG + "/all")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<DrugRepresentation>>() {});

        Assertions.assertEquals(3, drug.size());
    }

    @Test
    @TestTransaction
    public void search() throws JsonMappingException, JsonProcessingException {


        List<DrugRepresentation> drug = given().queryParam("drugName", "Lour")
                .when().get(Fixture.API_ROOT + Uri.DRUG)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<DrugRepresentation>>() {}) ;

        Assertions.assertEquals(1, drug.size());

    }

    @Test
    @TestTransaction
    public void successDrugSubmission(){

        DrugRepresentation drugRepresentation = Fixture.getNewDrugRepresentationForPost();

        DrugRepresentation dr = given()
                .contentType(ContentType.JSON)
                .body(drugRepresentation)
                .when()
                .post(Fixture.API_ROOT + "/drugs")
                .then().statusCode(201)
                .extract().as(DrugRepresentation.class);

        assertEquals("Panadol", dr.drugName);
        assertEquals(6, dr.id);


        DrugRepresentation updated = when().get(Fixture.API_ROOT + Uri.DRUG + "/6")
                .then()
                .statusCode(200)
                .extract().as(DrugRepresentation.class);

        Assertions.assertEquals(6, updated.id);
    }

    @Test
    @TestTransaction
    public void updateDrug() {
        DrugRepresentation ph = when().get(Fixture.API_ROOT + Uri.DRUG + "/6002")
                .then()
                .statusCode(200)
                .extract().as(DrugRepresentation.class);

        ph.drugName = "Asvestio";

        given()
                .contentType(ContentType.JSON)
                .body(ph)
                .when().put(Fixture.API_ROOT + Uri.DRUG + "/6002" )
                .then().statusCode(204);


        DrugRepresentation updated = when().get(Fixture.API_ROOT + Uri.DRUG + "/6002")
                .then()
                .statusCode(200)
                .extract().as(DrugRepresentation.class);

        Assertions.assertEquals("Asvestio", updated.drugName);
        Assertions.assertEquals(5002, updated.activeSubstance.id);

    }

    @Test
    @TestTransaction
    public void updateNonExistingDrug() {
        when().get(Fixture.API_ROOT + Uri.DRUG + "/6099")
                .then()
                .statusCode(404);
    }

    @Test
    @TestTransaction
    void submitExistingDrug(){

        DrugRepresentation dto = Fixture.getExistingDrugRepresentation();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(Fixture.API_ROOT + Uri.DRUG)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }


}
