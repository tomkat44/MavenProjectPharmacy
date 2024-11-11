package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AuthenticationTest {

    private EntityManager em;


    /*Έλεγχος ότι περνάει τα username - password μαζί*/
    @Test
    public void testAuthentication() {
        Authentication user = new Authentication();
        Authentication user1 ;
        user1 = user.setUsernamePassword("John", "tt1626");
        assertEquals("John", user1.getUsername());

    }

    //Έλεγχος αλλαγής Password
    @Test
    public void changePassword(){

        //Επιτυχής έλεγχος
        Authentication test_user = new Authentication("john@doctor.com","d1111");
        try {
            test_user.changeLogInPassword(test_user.getUsername(), "d1111","d2222" );
        } catch (DomainException e) {
            throw new RuntimeException(e);
        }
        assertEquals("d2222",test_user.getPassword());

        //Αποτυχημένος κωδικός
        Authentication test_user2 = new Authentication("john@doctor.com","0");
        try {
            test_user2.changeLogInPassword("john@doctor.com","ewnwfj","d2222");
        } catch (DomainException e) {
            assertEquals("Wrong old username or password", e.getMessage());
        }

    }

}
