package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import com.example.app.util.SystemDateStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QuantityPrescriptionTest {

    @Test
    public void checkQuantityPrescription(){
        QuantityPrescription quantityPrescription = new QuantityPrescription();
        quantityPrescription.setQuantityPrescription(14);
        assertEquals(14, quantityPrescription.getQuantityPrescription());
    }


}