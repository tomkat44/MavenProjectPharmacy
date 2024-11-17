package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PatientTest {

    private EntityManager em;
    private Patient patient;
    private Address address;
    private Prescription prescription;


    @Test
    public void newPatient(){
        patient = new Patient();
        address = new Address();
        address.setNumber("13");
        patient.setAddress(address);
        assertEquals("13",patient.getAddress().getNumber());
    }

    @Test
    public void testAddress() {
        Address address = new Address();
        address.setNumber("99");

        Patient patient = new Patient();
        patient.setAddress(address);

        /*Τα παρακάτω τα έχω σβήσει για κάποιο λόγο αν και κρατάει τα στοιχεία σωστά
         * δεν έχει σωστά τις διευθύνσεις πράγμα που θεωρώ ότι όταν δημιουργεί την
         * Address και την εισάγω δημιουργεί ένα αντίγραφο κάπου αλλού και δείχνει εκεί\
         * διότι τα στοιχεία City είναι σωστά*/
        Assertions.assertNotSame(address, patient.getAddress());

        Assertions.assertEquals(address.getNumber(), patient.getAddress().getNumber());

        address.setNumber("80");

        //System.out.println("\nADDRESS 2= "+ doctor.getAddress().getCity() +"=" + address.getCity() );
        Assertions.assertFalse(address.equals(patient.getAddress()) );

        /*Αλλάζει την διεύθυνση αλλά δεν την κρατάει κάπου και ελέγχει ότι δεν την αλλάζει */
        Address newAddress = patient.getAddress();
        Assertions.assertNotSame(newAddress, patient.getAddress());
        //System.out.println("\nADDRESS 3= "+ doctor.getAddress().getCity() +"=" + newAddress.getCity() );
        Assertions.assertTrue(newAddress.getNumber().equals(patient.getAddress().getNumber()) );

        Assertions.assertTrue(newAddress.getNumber().equals(patient.getAddress().getNumber()) );
        newAddress.setNumber("80");

        Assertions.assertFalse(newAddress.getNumber().equals(patient.getAddress().getNumber()) );
    }


    @Test
    public void seePrescription(){
        Patient patient = new Patient();
        patient.setAmka("12131455567");
        Doctor doctor = new Doctor();
        Prescription prescription1 = new Prescription();
        Prescription prescription2 = new Prescription();
        Prescription prescription3 = new Prescription();

        doctor.addNewPrescription(prescription1);
        doctor.addNewPrescription(prescription2);

        /*Ο ασθενής θα μπορεί να δει τις συνταγές του
        * δίνοντας τον ιαστό που του τις έργαψε
        * δεν έχω ακόμη το id*/
        assertEquals(2, patient.getPrescriptions(doctor).size());
        assertEquals("12131455567", patient.getAmka());
    }

}
