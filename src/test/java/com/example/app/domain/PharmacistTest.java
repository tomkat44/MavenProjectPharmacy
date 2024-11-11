package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PharmacistTest {

    @Test
    public void testAddress() {
        Address address = new Address();
        address.setStreet("Zaxaria");

        Doctor doctor = new Doctor();
        doctor.setAddress(address);

        /*Τα παρακάτω τα έχω σβήσει για κάποιο λόγο αν και κρατάει τα στοιχεία σωστά
         * δεν έχει σωστά τις διευθύνσεις πράγμα που θεωρώ ότι όταν δημιουργεί την
         * Address και την εισάγω δημιουργεί ένα αντίγραφο κάπου αλλού και δείχνει εκεί\
         * διότι τα στοιχεία City είναι σωστά*/
        Assertions.assertNotSame(address, doctor.getAddress());
        Assertions.assertEquals(address.getStreet(), doctor.getAddress().getStreet());
        address.setStreet("Kolokotroni");

        //System.out.println("\nADDRESS 2= "+ doctor.getAddress().getCity() +"=" + address.getCity() );
        Assertions.assertFalse(address.equals(doctor.getAddress()) );

        /*Αλλάζει την διεύθυνση αλλά δεν την κρατάει κάπου και ελέγχει ότι δεν την αλλάζει */
        Address newAddress = doctor.getAddress();
        Assertions.assertNotSame(newAddress, doctor.getAddress());
        //System.out.println("\nADDRESS 3= "+ doctor.getAddress().getCity() +"=" + newAddress.getCity() );
        Assertions.assertTrue(newAddress.getStreet().equals(doctor.getAddress().getStreet()) );


        Assertions.assertTrue(newAddress.getStreet().equals(doctor.getAddress().getStreet()) );
        newAddress.setStreet("Kolokotroni");

        Assertions.assertFalse(newAddress.getStreet().equals(doctor.getAddress().getStreet()) );
    }

    @Test
    public void newPharmacist(){
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setFirstName("Maria");
        pharmacist.setLastName("Papadogona");
        assertEquals("Maria", pharmacist.getFirstName());
    }

    /*Δυνατότητα ο Φαρμακοποιός να λάβει την Prescription από τον ασθενή
    * με χρήση ενός SET που υπάρχει μέσα στον Pharmacist*/
    @Test
    public void getPrescriptionFromPatient(){
        Patient patient = new Patient();
        patient.setEmail("patient@patient.gr");
        Doctor doctor = new Doctor();
        doctor.setEmail("doctor@doctor.gr");
        Pharmacist pharmacist = new Pharmacist();
        pharmacist.setEmail("pharmacist@pharmacist.gr");
        Prescription prescription1 = new Prescription();
        Prescription prescription2 = new Prescription();

        doctor.addNewPrescription(prescription1);
        doctor.addNewPrescription(prescription2);

        assertEquals(2, pharmacist.getPrescriptions(patient, doctor).size());

    }


}
