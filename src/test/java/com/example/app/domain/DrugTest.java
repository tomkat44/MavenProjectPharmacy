package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import com.example.app.representation.DrugRepresentation;
import com.example.app.representation.PharmacistRepresentation;
import com.example.app.util.Fixture;
import io.quarkus.test.TestTransaction;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

public class DrugTest {

    @Test
    public void addNewDrug(){
        Drug drug = new Drug("Depon", 5.11, medicineCategory.ORIGINALS);
        ActiveSubstance as = new ActiveSubstance();
        drug.setActiveSubstance(as);

        assertNotNull(drug.getActiveSubstance());
        assertEquals(5.11, drug.getDrugPrice());

    }


}
