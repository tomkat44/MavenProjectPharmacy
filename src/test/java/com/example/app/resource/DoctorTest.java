package com.example.app.resource;


import com.example.app.representation.ActiveSubstanceRepresentation;
import com.example.app.representation.DoctorRepresentation;
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

@QuarkusTest
public class DoctorTest extends IntegrationBase {

    @Test
    @TestTransaction
    public void find(){
        DoctorRepresentation d = when().get(Fixture.API_ROOT + Uri.DOCTOR + "/" + 2000)
                .then()
                .statusCode(200)
                .extract().as(DoctorRepresentation.class);
        Assertions.assertEquals("Papadopoulos", d.lastName);
        Assertions.assertEquals(2000, d.id);
    }

    @Test
    @TestTransaction
    public void listAll() {
        List<DoctorRepresentation> as = when().get(Fixture.API_ROOT + Uri.DOCTOR + "/all")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<DoctorRepresentation>>() {});

        Assertions.assertEquals(3, as.size());
    }

    @Test
    @TestTransaction
    public void successDoctorSubmission(){

        DoctorRepresentation doctorRepresentation = Fixture.getDoctorRepresentation();

        DoctorRepresentation savedDoctor = given()
                .contentType(ContentType.JSON)
                .body(doctorRepresentation)
                .when()
                .post(Fixture.API_ROOT + "/doctors")
                .then().statusCode(201)
                .extract().as(DoctorRepresentation.class);

        Assertions.assertEquals(5,savedDoctor.id);

        DoctorRepresentation d = when().get(Fixture.API_ROOT + Uri.DOCTOR + "/" + 5)
                .then()
                .statusCode(200)
                .extract().as(DoctorRepresentation.class);
        Assertions.assertEquals(5, d.id);

    }

    //---------GET - AFM-----------
    @Test
    @TestTransaction
    public void findByAfm() throws JsonMappingException, JsonProcessingException {

        List<DoctorRepresentation> doc = given().queryParam("doctorAfm", "144442222").when().get(Fixture.API_ROOT + Uri.DOCTOR)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<DoctorRepresentation>>() {});
        System.out.println(doc.get(0).afm);
        System.out.println(doc.get(0).lastName);
        Assertions.assertEquals("144442222", doc.get(0).afm);

    }

    //-------PUT------------
    @Test
    @TestTransaction
    public void successUpdateDoctor() {
        DoctorRepresentation doc = when().get(Fixture.API_ROOT + Uri.DOCTOR + "/2002")
                .then()
                .statusCode(200)
                .extract().as(DoctorRepresentation.class);

        doc.email = "xolevac@doctor.com";

        given()
                .contentType(ContentType.JSON)
                .body(doc)
                .when().put(Fixture.API_ROOT + Uri.DOCTOR + "/2002" )
                .then().statusCode(204);

        DoctorRepresentation updatedDoc = when().get(Fixture.API_ROOT + Uri.DOCTOR + "/2002")
                .then()
                .statusCode(200)
                .extract().as(DoctorRepresentation.class);

        Assertions.assertEquals("xolevac@doctor.com", updatedDoc.email);
    }

    @Test
    @TestTransaction
    void submitExistingDoctor(){

        DoctorRepresentation dto = Fixture.getExistingDoctorRepresentation();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(Fixture.API_ROOT + Uri.DOCTOR)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @TestTransaction
    public void updateNonExistingDoctor() {
        when().get(Fixture.API_ROOT + Uri.DOCTOR + "/2199")
                .then()
                .statusCode(404);
    }

//    @Test
//    @TestTransaction
//    void removeExistingDoctor(){
//
//        when()
//                .delete(Fixture.API_ROOT + Uri.DOCTOR + "/2001")
//                .then()
//                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
//    }

    @Test
    @TestTransaction
    void removeNonExistingDoctor(){

        when()
                .delete(Fixture.API_ROOT + Uri.DOCTOR + "4000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }


}
