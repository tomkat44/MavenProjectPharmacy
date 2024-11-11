package com.example.app.resource;

import com.example.app.representation.DoctorRepresentation;
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
public class PatientTest extends IntegrationBase {
    //--------GET--------
    @Test
    @TestTransaction
    public void findById(){
        PatientRepresentation patients = when().get(Fixture.API_ROOT + Uri.PATIENT+ "/" + 3000)
                .then()
                .statusCode(200)
                .extract().as(PatientRepresentation.class);
        Assertions.assertEquals(3000, patients.id);
        Assertions.assertEquals("Alexandrou", patients.lastName);
    }

    @Test
    @TestTransaction
    public void listAll() {
        List<PatientRepresentation> as = when().get(Fixture.API_ROOT + Uri.PATIENT + "/all")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PatientRepresentation>>() {});

        Assertions.assertEquals(2, as.size());
    }

    @Test
    @TestTransaction
    public void findByAmka() throws JsonMappingException, JsonProcessingException {

        List<PatientRepresentation> pat = given().queryParam("patientAmka", "12121202222").when().get(Fixture.API_ROOT + Uri.PATIENT)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PatientRepresentation>>() {});

        Assertions.assertEquals("12121202222", pat.get(0).amka);

    }
    //-----POST------------
    @Test
    @TestTransaction
    public void successPatientSubmission(){

        PatientRepresentation patientRepresentation = Fixture.getNewPatientRepresentation();

        PatientRepresentation pat = given()
                .contentType(ContentType.JSON)
                .body(patientRepresentation)
                .when()
                .post(Fixture.API_ROOT + "/patients")
                .then().statusCode(201)
                .extract().as(PatientRepresentation.class);

        assertEquals("12121202223", pat.amka);
        assertEquals(7, pat.id);

        PatientRepresentation updated = when().get(Fixture.API_ROOT + Uri.PATIENT + "/7")
                .then()
                .statusCode(200)
                .extract().as(PatientRepresentation.class);

        Assertions.assertEquals(7, updated.id);

    }

    //---------PUT----------------

    @Test
    @TestTransaction
    public void successUpdatePatient() {
        PatientRepresentation pat = when().get(Fixture.API_ROOT + Uri.PATIENT + "/3000")
                .then()
                .statusCode(200)
                .extract().as(PatientRepresentation.class);

        pat.email = "spy.alex@patient.com";


        given()
                .contentType(ContentType.JSON)
                .body(pat)
                .when().put(Fixture.API_ROOT + Uri.PATIENT + "/3000" )
                .then().statusCode(204);

        PatientRepresentation updatedPat = when().get(Fixture.API_ROOT + Uri.PATIENT + "/3000")
                .then()
                .statusCode(200)
                .extract().as(PatientRepresentation.class);

        Assertions.assertEquals("spy.alex@patient.com", updatedPat.email);

    }

    @Test
    @TestTransaction
    void submitExistingPatient(){

        PatientRepresentation dto = Fixture.getExistingPatientRepresentation();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(Fixture.API_ROOT + Uri.PATIENT)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @TestTransaction
    public void updateNonExistingPatient() {
        when().get(Fixture.API_ROOT + Uri.PATIENT + "/3199")
                .then()
                .statusCode(404);
    }

//    @Test
//    @TestTransaction
//    void removeExistingPatient(){
//
//        when()
//                .delete(Fixture.API_ROOT + Uri.PATIENT + "/3001")
//                .then()
//                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
//    }

    @Test
    @TestTransaction
    void removeNonExistingPatient(){

        when()
                .delete(Fixture.API_ROOT + Uri.PATIENT + "4000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}
