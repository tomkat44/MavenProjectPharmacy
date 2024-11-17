package com.example.app.domain;

import com.example.app.persistence.JPAUtil;
import com.example.app.util.SystemDateStub;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

public class DoctorTest {


    @Test
    public void testAddress() {
        Address address = new Address();
        address.setCity("Athens");

        Doctor doctor = new Doctor();
        doctor.setAddress(address);

        /*Τα παρακάτω τα έχω σβήσει για κάποιο λόγο αν και κρατάει τα στοιχεία σωστά
        * δεν έχει σωστά τις διευθύνσεις πράγμα που θεωρώ ότι όταν δημιουργεί την
        * Address και την εισάγω δημιουργεί ένα αντίγραφο κάπου αλλού και δείχνει εκεί\
        * διότι τα στοιχεία City είναι σωστά*/
        Assertions.assertNotSame(address, doctor.getAddress());
        Assertions.assertEquals(address.getCity(), doctor.getAddress().getCity());
        address.setCity("Patra");

        //System.out.println("\nADDRESS 2= "+ doctor.getAddress().getCity() +"=" + address.getCity() );
        Assertions.assertFalse(address.equals(doctor.getAddress()) );

        /*Αλλάζει την διεύθυνση αλλά δεν την κρατάει κάπου και ελέγχει ότι δεν την αλλάζει */
        Address newAddress = doctor.getAddress();
        Assertions.assertNotSame(newAddress, doctor.getAddress());
        //System.out.println("\nADDRESS 3= "+ doctor.getAddress().getCity() +"=" + newAddress.getCity() );
        Assertions.assertTrue(newAddress.getCity().equals(doctor.getAddress().getCity()) );


        Assertions.assertTrue(newAddress.getCity().equals(doctor.getAddress().getCity()) );
        newAddress.setCity("Patra");

        Assertions.assertFalse(newAddress.getCity().equals(doctor.getAddress().getCity()) );
    }

    @Test
    public void testAddPrescriptionFromDoctor(){
        Drug drug1 = new Drug("Depon", 5.44, medicineCategory.ORIGINALS);// !!!!!!!!!!!!!!!
        Drug drug2 = new Drug("Panadol", 6.44, medicineCategory.ORIGINALS);

        QuantityPrescription quantityPrescription1 = new QuantityPrescription();
        QuantityPrescription quantityPrescription2 = new QuantityPrescription();
        Prescription prescription1 = new Prescription();
        Prescription prescription2 = new Prescription();

        //$prescription1.addDrug(drug1);
        //$prescription1.addDrug(drug2);

        prescription1.addQuantityPrescription(quantityPrescription1);
        prescription1.addQuantityPrescription(quantityPrescription2);

        Doctor doctor = new Doctor();
        doctor.addNewPrescription(prescription1);
        doctor.addNewPrescription(prescription2);
        Assertions.assertEquals(2, doctor.getPrescriptionFromDoctor().size());
        Assertions.assertEquals(2, prescription1.getDrugs().size());
        Assertions.assertEquals(0, prescription2.getDrugs().size());
        Assertions.assertEquals(2, prescription1.getQuantityPrescriptions().size());


        doctor.removePrescription(prescription1);
        Assertions.assertEquals(1, doctor.getPrescriptionFromDoctor().size());
        /*Εδώ έχω δημιουργήσει κάποια φάρμακα και διαγράφω μία Prescription
        * Δεν θέλω να διαγραφούν και τα φάρμακα*/
        Assertions.assertEquals(2, prescription1.getDrugs().size());

    }

    @Test
    public void testRemovePrescriptionFromDoctor(){
        Prescription prescription = new Prescription();
        Doctor doctor = new Doctor();

        doctor.addNewPrescription(prescription);
        Assertions.assertEquals(1, doctor.getPrescriptionFromDoctor().size());

        doctor.removePrescription(prescription);
        Assertions.assertEquals(0, doctor.getPrescriptionFromDoctor().size());
    }

}
