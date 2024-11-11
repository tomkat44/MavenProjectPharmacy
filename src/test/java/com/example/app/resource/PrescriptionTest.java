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

import static com.example.app.domain.executionPrescriptionFlag.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PrescriptionTest extends IntegrationBase {

    @Test
    @TestTransaction
    public void find() {
        PrescriptionRepresentation prescription = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/" + 7000)
                .then()
                .statusCode(200)
                .extract().as(PrescriptionRepresentation.class);

        Assertions.assertEquals(7000, prescription.id);

        Assertions.assertEquals(2, prescription.quantityPrescriptions.size());
        Assertions.assertNotNull(prescription.quantityPrescriptions.get(1).drug.drugName);

        Assertions.assertEquals(9000, prescription.prescriptionExecution.id);
        Assertions.assertEquals(6.3432, prescription.prescriptionExecution.summaryCost);
        Assertions.assertEquals(PARTIALLY, prescription.prescriptionExecution.executionFlag);
        Assertions.assertEquals("maria@pharmacist.com", prescription.prescriptionExecution.pharmacist.email);

        Assertions.assertEquals(2, prescription.prescriptionExecution.quantityExecutions.size());
    }

    @Test
    @TestTransaction
    public void listAll() {
        List<PrescriptionRepresentation> as = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/all")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PrescriptionRepresentation>>() {});

        Assertions.assertEquals(5, as.size());
    }

    @Test
    @TestTransaction
    public void searchByPatientAMKA() throws JsonMappingException, JsonProcessingException {

        List<PrescriptionRepresentation> patientAMKA = given().queryParam("amka", "12121201112")
                .when().get(Fixture.API_ROOT + Uri.PRESCRIPTION)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PrescriptionRepresentation>>() {});

        System.out.println(patientAMKA.size());
        Assertions.assertEquals("12121201112", patientAMKA.get(0).patientAMKA);
        Assertions.assertEquals(3, patientAMKA.size());

    }

    @Test
    @TestTransaction
    void submitNewPrescriptionWithPrescriptionExecution() {

        PrescriptionRepresentation prescriptionRepresentation = Fixture.getPrescriptionRepresentation();

        //Θέλω να είναι null το quantity Execution γιατι ο ιατρός δεν μπορέι να κάνει και ΕΚΤΕΛΕΣΗ
        //prescriptionRepresentation.prescriptionExecution = Fixture.getPrescriptionExecution();

        PrescriptionRepresentation pr = given()
                .contentType(ContentType.JSON)
                .body(prescriptionRepresentation)
                .when()
                .post(Fixture.API_ROOT + Uri.PRESCRIPTION)
                .then().statusCode(201)
                .extract().as(PrescriptionRepresentation.class);

        assertEquals(12, pr.id);

        PrescriptionRepresentation updated = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/12")
                .then()
                .statusCode(200)
                .extract().as(PrescriptionRepresentation.class);

        Assertions.assertEquals(12, updated.id);
        Assertions.assertEquals(1, updated.quantityPrescriptions.size());
        Assertions.assertEquals(13, updated.prescriptionExecution.id);

    }

    @Test
    @TestTransaction
    void submitNewPrescriptionWithOutPrescriptionExecution() {

        PrescriptionRepresentation prescriptionRepresentation = Fixture.getPrescriptionRepresentationWithoutPrescriptionExecution();

        //Θέλω να είναι null το quantity Execution γιατι ο ιατρός δεν μπορέι να κάνει και ΕΚΤΕΛΕΣΗ
        //prescriptionRepresentation.prescriptionExecution = Fixture.getPrescriptionExecution();

        PrescriptionRepresentation pr = given()
                .contentType(ContentType.JSON)
                .body(prescriptionRepresentation)
                .when()
                .post(Fixture.API_ROOT + Uri.PRESCRIPTION)
                .then().statusCode(201)
                .extract().as(PrescriptionRepresentation.class);

        assertEquals(10, pr.id);

        PrescriptionRepresentation updated = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/10")
                .then()
                .statusCode(200)
                .extract().as(PrescriptionRepresentation.class);

        Assertions.assertEquals(10, updated.id);
        Assertions.assertEquals(1, updated.quantityPrescriptions.size());
        Assertions.assertEquals(null, updated.prescriptionExecution);

    }

    @Test
    @TestTransaction
    public void updatePrescriptions() {
        PrescriptionRepresentation pre = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/7002")
                .then()
                .statusCode(200)
                .extract().as(PrescriptionRepresentation.class);

        Assertions.assertEquals("chris@doctor.com", pre.doctorToPrescription.email);
        Assertions.assertEquals("spy@patient.com", pre.patientToPrescription.email);
        Assertions.assertEquals(1, pre.quantityPrescriptions.size());



        pre.prescriptionExecution = Fixture.getPrescriptionExecution();
        //pre.prescriptionExecution.quantityExecutions.add(Fixture.getNewQuantityExecutionWithExistingDrug());

        Assertions.assertEquals(1, pre.prescriptionExecution.quantityExecutions.size());


        given()
                .contentType(ContentType.JSON)
                .body(pre)
                .when().put(Fixture.API_ROOT + Uri.PRESCRIPTION + "/7002")
                .then().statusCode(204);


        PrescriptionRepresentation updated = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/7002")
                .then()
                .statusCode(200)
                .extract().as(PrescriptionRepresentation.class);



        Assertions.assertEquals("chris@doctor.com", updated.doctorToPrescription.email);
        Assertions.assertEquals("spy@patient.com", updated.patientToPrescription.email);


        Assertions.assertEquals("maria@pharmacist.com", updated.prescriptionExecution.pharmacist.email);
        Assertions.assertEquals(EXECUTED, updated.prescriptionExecution.executionFlag);
        Assertions.assertEquals(0.21760000000000002, updated.prescriptionExecution.summaryCost);

        Assertions.assertEquals(1, updated.quantityPrescriptions.size());
        Assertions.assertEquals(2, updated.quantityPrescriptions.get(0).quantityPrescription);
        Assertions.assertEquals("Depon", updated.quantityPrescriptions.get(0).drug.drugName);


        Assertions.assertEquals(1, updated.prescriptionExecution.quantityExecutions.size());
        Assertions.assertEquals(2, updated.prescriptionExecution.quantityExecutions.get(0).quantityExecutionPieces);


    }

    @Test
    @TestTransaction
    void submitExistingPrescription() {

        PrescriptionRepresentation dto = Fixture.getExistingPrescriptionRepresentation();

        given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(Fixture.API_ROOT + Uri.PRESCRIPTION)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @TestTransaction
    void removePrescriptionNotExecuted(){

        /*Για την ακρίβεια αυτή η DELETE δεν διαγράφει αλλά ακυρώνει μια συνταγή ΜΟΝΟ αν δεν είναι EXECUTED*/
        when()
                .delete(Fixture.API_ROOT + Uri.PRESCRIPTION + "/7001")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        PrescriptionRepresentation updated = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/7001")
                .then()
                .statusCode(200)
                .extract().as(PrescriptionRepresentation.class);



        Assertions.assertEquals(15, updated.prescriptionExecution.id);
        Assertions.assertEquals(CANCELED, updated.prescriptionExecution.executionFlag);

    }

    @Test
    @TestTransaction
    public void printPrescription() {
        PrescriptionRepresentation prescription = when().get(Fixture.API_ROOT + Uri.PRESCRIPTION + "/" + 7000)
                .then()
                .statusCode(200)
                .extract().as(PrescriptionRepresentation.class);

        //print prescription details
        PrintPrescriptionRepresentation pr_print = new PrintPrescriptionRepresentation();
        pr_print.print(prescription);
    }

}