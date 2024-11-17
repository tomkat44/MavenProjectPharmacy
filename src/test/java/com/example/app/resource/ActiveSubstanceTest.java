package com.example.app.resource;

import com.example.app.domain.ActiveSubstance;
import com.example.app.domain.Pharmacist;
import com.example.app.persistence.ActiveSubstanceRepository;
import com.example.app.representation.ActiveSubstanceMapper;
import com.example.app.representation.ActiveSubstanceRepresentation;
import com.example.app.representation.DrugRepresentation;
import com.example.app.representation.PharmacistRepresentation;
import com.example.app.util.Fixture;
import com.example.app.util.IntegrationBase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

import static org.wildfly.common.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;



@QuarkusTest
public class ActiveSubstanceTest extends IntegrationBase {

    @Test
    @TestTransaction
    public void find() {
        ActiveSubstanceRepresentation as = when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/" + 5000)
                .then()
                .statusCode(200)
                .extract().as(ActiveSubstanceRepresentation.class);
        Assertions.assertEquals(5000, as.id);


    }

    @Test
    @TestTransaction
    public void listAll() {
        List<ActiveSubstanceRepresentation> as = when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/all")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<ActiveSubstanceRepresentation>>() {});

        Assertions.assertEquals(4, as.size());
    }

    @Test
    @TestTransaction
    public void search() throws JsonMappingException, JsonProcessingException {

        List<ActiveSubstanceRepresentation> as = given().queryParam("name", "Nabilon")
                .when().get(Fixture.API_ROOT + Uri.SUBSTANCE)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<ActiveSubstanceRepresentation>>() {}) ;

        Assertions.assertEquals(1, as.size());

    }


    @Test
    @TestTransaction
    void submitNewActiveSubstance(){

        ActiveSubstanceRepresentation dto = Fixture.getNewActiveSubstanceRepresentation();
        String pass = "abc";
        ActiveSubstanceRepresentation asDto = given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(Fixture.API_ROOT + Uri.SUBSTANCE)
                .then().statusCode(201)
                .extract().as(ActiveSubstanceRepresentation.class);

        assertNotNull(asDto.id);
        Assertions.assertEquals(4, asDto.id);

        ActiveSubstanceRepresentation as = when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/" + 4)
                .then()
                .statusCode(200)
                .extract().as(ActiveSubstanceRepresentation.class);
        Assertions.assertEquals(4, as.id);
    }

    @Test
    @TestTransaction
    public void updateActiveSubstance() {
        ActiveSubstanceRepresentation ph = when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/5002")
                .then()
                .statusCode(200)
                .extract().as(ActiveSubstanceRepresentation.class);

        ph.substanceName = "Povidoni_new";

        given()
                .contentType(ContentType.JSON)
                .body(ph)
                .when().put(Fixture.API_ROOT + Uri.SUBSTANCE + "/5002" )
                .then().statusCode(204);


        ActiveSubstanceRepresentation updated = when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/5002")
                .then()
                .statusCode(200)
                .extract().as(ActiveSubstanceRepresentation.class);

        Assertions.assertEquals("Povidoni_new", updated.substanceName);
    }


    @Test
    @TestTransaction
    void submitExistingActiveSubstance(){

        ActiveSubstanceRepresentation dto = Fixture.getExistingActiveSubstanceRepresentation();

        /*Κάνει το POST και ελέγχει ότι φέρνει 404 ότι δηλαδή του έδωσα σαν όρισμα να δώσει
        λόγω του λανθασμένου POST*/
       given()
                .contentType(ContentType.JSON)
                .body(dto)
                .when()
                .post(Fixture.API_ROOT + Uri.SUBSTANCE)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());

    }

    @Test
    @TestTransaction
    public void updateNonExistingSubstance() {
        when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/5199")
                .then()
                .statusCode(404);
    }


    @Test
    @TestTransaction
    void removeExistingSubstanceWithWrongPass(){

        /*Εάν δοθεί σωστός κωδικός η Active Substance Διαγράφεται*/
        given()
                .contentType(ContentType.JSON)
                .body("ab")
                .when()
                .delete(Fixture.API_ROOT + Uri.SUBSTANCE + "/5003")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        ActiveSubstanceRepresentation updated = when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/5003")
                .then()
                .statusCode(200)
                .extract().as(ActiveSubstanceRepresentation.class);

        Assertions.assertEquals(5003, updated.id);

    }

    @Test
    @TestTransaction
    void removeExistingSubstanceWithCorrectPass(){

        /*Σε περίπτωση λάθος κωδικού η ενέργεια δεν πραγματοποιείται και η Active Substance παραμάνει μέσα στην DB*/
        given()
                .contentType(ContentType.JSON)
                .body("abc")
                .when()
                .delete(Fixture.API_ROOT + Uri.SUBSTANCE + "/5003")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        when().get(Fixture.API_ROOT + Uri.SUBSTANCE + "/5003")
                .then()
                .statusCode(404);

    }

    @Test
    @TestTransaction
    void removeNonExistingSubstance(){

        when()
                .delete(Fixture.API_ROOT + Uri.SUBSTANCE + "4000")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}
