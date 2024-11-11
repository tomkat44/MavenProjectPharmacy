package com.example.app.resource;

import com.example.app.representation.*;
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
public class PharmacistTest extends IntegrationBase {

    @Test
    @TestTransaction
    public void find() {
        PharmacistRepresentation ph = when().get(Fixture.API_ROOT + Uri.PHARMACIST + "/" + 4000)
                .then()
                .statusCode(200)
                .extract().as(PharmacistRepresentation.class);
        Assertions.assertEquals(4000, ph.id);
    }

    @Test
    @TestTransaction
    public void listAll() {
        List<PharmacistRepresentation> as = when().get(Fixture.API_ROOT + Uri.PHARMACIST + "/all")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PharmacistRepresentation>>() {});

        Assertions.assertEquals(2, as.size());
    }


    @Test
    @TestTransaction
    void successPharmacistSubmission(){

        PharmacistRepresentation pharmacistRepresentation = Fixture.getPharmacistRepresentation();

        PharmacistRepresentation phdto = given()
                .contentType(ContentType.JSON)
                .body(pharmacistRepresentation)
                .when()
                .post(Fixture.API_ROOT + "/pharmacists")
                .then().statusCode(201)
                .extract().as(PharmacistRepresentation.class);

        assertEquals(8, phdto.id);

        PharmacistRepresentation updated = when().get(Fixture.API_ROOT + Uri.PHARMACIST + "/8")
                .then()
                .statusCode(200)
                .extract().as(PharmacistRepresentation.class);

        Assertions.assertEquals(8, updated.id);

    }

    @Test
    @TestTransaction
    public void updatePharmacist() {
        PharmacistRepresentation ph = when().get(Fixture.API_ROOT + Uri.PHARMACIST + "/4001")
                .then()
                .statusCode(200)
                .extract().as(PharmacistRepresentation.class);

        ph.firstName = "NIKOLAS";

        given()
                .contentType(ContentType.JSON)
                .body(ph)
                .when().put(Fixture.API_ROOT + Uri.PHARMACIST + "/4001" )
                .then().statusCode(204);


        PharmacistRepresentation updated = when().get(Fixture.API_ROOT + Uri.PHARMACIST + "/4001")
                .then()
                .statusCode(200)
                .extract().as(PharmacistRepresentation.class);

        Assertions.assertEquals("NIKOLAS", updated.firstName);
    }




    //-----search by pharmacist afm------
    @Test
    @TestTransaction
    public void searchByAfm() throws JsonMappingException, JsonProcessingException{

        List<PharmacistRepresentation> ph_afm = given().queryParam("afm", "133331111").when().get(Fixture.API_ROOT + Uri.PHARMACIST)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PharmacistRepresentation>>() {});
        System.out.println(ph_afm.get(0).afm);
        Assertions.assertEquals("133331111", ph_afm.get(0).afm);


    }
    //------POST existing pharmacist------
    @Test
    @TestTransaction
    void submitExistingPharmacist(){

        PharmacistRepresentation dto = Fixture.getExistingPharmacistRepresentation2();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(Fixture.API_ROOT + Uri.PHARMACIST)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @TestTransaction
    public void updateNonExistingPharmacist() {
        when().get(Fixture.API_ROOT + Uri.PHARMACIST + "/4199")
                .then()
                .statusCode(404);
    }

     /* @Test
    @TestTransaction
    void removeExistingPharmacist(){

        when()
                .delete(Fixture.API_ROOT + Uri.PHARMACIST + "/4000")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        PharmacistRepresentation ph = when().get(Fixture.API_ROOT + Uri.PHARMACIST + "/" + 4000)
                .then()
                .statusCode(200)
                .extract().as(PharmacistRepresentation.class);
    }

    @Test
    @TestTransaction
    void removeNonExistingPharmacist(){

        when()
                .delete(Fixture.API_ROOT + Uri.PHARMACIST + "5000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }*/

}
