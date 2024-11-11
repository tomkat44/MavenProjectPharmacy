package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

public class ActiveSubstanceTest {


    @Test
    public void addActiveSubstance() {
        ActiveSubstance as1 = new ActiveSubstance();
        as1.setName("Povidoni");
        as1.setQuantity(22);
        Assertions.assertEquals(22, as1.getQuantity());


    }
    @Test
    public void checkActiveSubstanceEquals() {
        ActiveSubstance as1 = new ActiveSubstance();
        as1.setName("Povidoni");
        as1.setQuantity(22);
        ActiveSubstance as2 = new ActiveSubstance("horonali", 22);
        boolean check = as1.equals(as2);
        Assertions.assertFalse(check);
    }


}
