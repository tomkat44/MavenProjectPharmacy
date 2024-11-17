package com.example.app.persistence;

import com.example.app.domain.*;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@QuarkusTest
class JPAQueriesTest {


    LocalDate now = LocalDate.of(2022, 11, 16);

    @Inject
    EntityManager em;


    //################   AUTHENTICATION ##########################
    @Test
    @TestTransaction
    public void _queryAuthentication() {
        Query query;
        query = em.createQuery("select u from Authentication u");
        List<Authentication> authentications = query.getResultList();

        assertEquals(3,authentications.size());

        Authentication test_user = new Authentication("john@doctor.com","d1111");
        assertTrue(authentications.contains(test_user));

        Authentication test_user2 = new Authentication("john@doctor.com","0");
        assertFalse(authentications.contains(test_user2));

    }

    //################   ACTIVE SUBSTANCE  ##########################
    @Test
    @TestTransaction
    void _queryActiveSubstance(){

        Query query = em.createQuery("select a from ActiveSubstance a");
        List<ActiveSubstance> result = query.getResultList();
        assertEquals(4, result.size());

        ActiveSubstance a = result.get(0);
        assertNotNull(a.getName());
        assertEquals("Paraketamol", a.getName());

        query = em.createQuery("select d from Drug d where d.drugName like '" + "Depon" + "'");
        List<Drug> result2 = query.getResultList();
        assertEquals(1, result2.size());



    }




    //################   DOCTOR  ##########################
    @Test
    @TestTransaction
    void _queryDoctor () {
        Query query = em.createQuery("select dr from Doctor dr");
        List<Doctor> result = query.getResultList();
        assertEquals(3, result.size());
    }


    @Test
    @TestTransaction
    void _queryPatient () {
        Query query = em.createQuery("select p from Patient p");
        List<Patient> result = query.getResultList();
        assertEquals(2, result.size());
    }

    @Test
    @TestTransaction
    void _queryPharmacist () {
        Query query = em.createQuery("select ph from Pharmacist ph");
        List<Pharmacist> result = query.getResultList();
        assertEquals(2, result.size());
    }

    @Test
    @TestTransaction
    void _queryQuantityPrescriptions () {
        Query query = em.createQuery("select qp from QuantityPrescription qp");
        List<QuantityPrescription> result = query.getResultList();
//        for (int i=0;i<result.size();i++){
//            System.out.println(result.get(i).getId());
//        }
        assertEquals(6, result.size());
    }
    @Test
    @TestTransaction
    void _queryPrescriptionExecutions () {
        Query query = em.createQuery("select pe from PrescriptionExecution pe");
        List<PrescriptionExecution> result = query.getResultList();
        assertEquals(2, result.size());
    }

    @Test
    @TestTransaction
    void _queryQuantityExecutions() {
        Query query = em.createQuery("select qe from QuantityExecution qe");
        List<QuantityExecution> result = query.getResultList();
        assertEquals(3, result.size());
    }

    @Test
    @TestTransaction
    void _queryPrescriptions() {
        Query query = em.createQuery("select p from Prescription p");
        List<Prescription> result = query.getResultList();
        assertEquals(5, result.size());
        //assertEquals("2023-01-03 10:52:26.059372", result.get(0).getCreationDate());

    }

    //################   DRUG  ##########################
    @Test
    @TestTransaction
    void queryDrug(){

        Query query = em.createQuery("select d from Drug d");
        List<Drug> drugs = query.getResultList();
        assertEquals(3, drugs.size());
        Drug drug1 = drugs.get(0);
        assertNotNull(drug1.getDrugName());
        assertNotNull(drug1.getActiveSubstance().getId());

        /*Δημιουργούμε ένα ίδιο φάρμακο και ελάγχουμε αν υπάρχει μέσα στην βάση
        * ώστε να μπορέσουμε να αποστέψουμε το ενδεχόμενο να εισάγει κάποιος 2 φορές
        * το ίδιο φάρμακο*/
        Drug drug2 = new Drug("Depon", 5.44, medicineCategory.ORIGINALS);
        assertTrue(drugs.contains(drug2));
        //Ελέγχω ότι δεν είναι κενό όταν τον φέρνει
        assertNotNull(drug1.getActiveSubstance());

        Drug d1 = drugs.get(1);
        assertNotNull(d1.getDrugName());
        Drug d2 = new Drug("Lourofen", 8.44, medicineCategory.GENERICS);
        assertTrue(drugs.contains(d2));
        //Ελέγχω ότι δεν είναι κενό όταν τον φέρνει
        assertNotNull(d1.getActiveSubstance());
        assertFalse(d2.drugDoesNotExist(d2, drugs));

        /*Αν ένα φάρμακο έχει ίσοα όλα τα άλλα και διαφορετικό όνομα
        * τότε θα επιστρέφει TRUE και θα αφήνει την καταχώρηση*/
        Drug d3 = new Drug("Lourofe", 8.44, medicineCategory.GENERICS);
        assertTrue(d3.drugDoesNotExist(d3, drugs));

        /*Παρακάτω ελέγχεται η drugDoesNotExist ότι αν δημιουργηθεί
         * φάραμκο που έχει το ίδιο όνομα με άλλο το οποίο υπάρχει θα
         * επιστρέφει FALSE και δεν θα αφήνει την εισαγωγή*/
        Drug d4 = new Drug("Lourofen", 8.0, medicineCategory.ORIGINALS);
        assertFalse(d4.drugDoesNotExist(d4, drugs));

    }

    @Test
    @TestTransaction
    public void insertNewDrug(){
       // EntityTransaction tx = em.getTransaction();

        Query query = em.createQuery("select d from Drug d");
        List<Drug> drug = query.getResultList();
        assertEquals(3, drug.size());

        Drug d14 = new Drug();
        d14.setDrugName("Lamisil");
        d14.setDrugPrice(12.12);
        d14.setMedicineCategory(medicineCategory.ORIGINALS);

        query = em.createQuery("select a from ActiveSubstance a");
        List<ActiveSubstance> activeSubstances = query.getResultList();
        assertEquals(4,activeSubstances.size());

        d14.setActiveSubstance(activeSubstances.get(0));

       // tx.begin();
        em.persist(d14);
        //tx.commit();

        assertEquals(10, d14.getActiveSubstance().getQuantity());
        //em.close();


        //em = JPAUtil.getCurrentEntityManager();
        query = em.createQuery("select d from Drug d");
        List<ActiveSubstance> d = query.getResultList();
        assertEquals(4,d.size());
        assertTrue(d.contains(d14));

    }

    //################   PATIENT  ##########################

    @Test
    @TestTransaction
    public void testPatientSeePrescription() {

        /*με την παρακάτω διαδικασία εμφανίζεται ο Ασθενής με τις συνταγές του
         *
         * Επιστρέφει όσες συνταγές το ΑΜΚΑ είναι το συγκεκριμένο Patient*/
        Query query = em.createQuery("select p from Prescription p where patientAMKA like '12121201112'");
        List<Prescription> p = query.getResultList(); //το βάζω σε λίστα
        assertEquals(3, p.size());

        int prescriptionId = p.get(0).getId(); //παίρνω το 1ο ID που αντιστοιχεί σε συνταγή

        /*Χρησιμοποιούμε το ID που βρήκαμε ώστε να κάνουμε ερώτημα για να βρουμε τις ποσότητες
         * και τα φάρμακα που είναι αναγραμμένα σε αυτό το ID του Prescription*/
        query = em.createQuery("select qp from QuantityPrescription qp where prescription_id ='" + prescriptionId + "'");
        List<QuantityPrescription> qp = query.getResultList();
        assertEquals(2, qp.size());//έχει 2 καταχωρήσεις σε φάρμακα
        List<Integer> QuantityTest= new ArrayList<>();
        QuantityTest.add(qp.get(0).getQuantityPrescription());
        QuantityTest.add(qp.get(1).getQuantityPrescription());

        assertTrue(QuantityTest.contains(4));
        int quantityId = qp.get(0).getDrug().getId(); //παιρνουμε το ID του φαρμάκου της συσχέτισης για το παρακάτω query

        /*Χρησιμοποιούμε το ID του Drug του ενδιάμεσου πίνακα για να βρούμε το φάρμακο που αντιστοιχεί*/
        query = em.createQuery("select d from Drug d where id ='" + quantityId + "'");
        List<Drug> drugs = query.getResultList();
        assertEquals(1, drugs.size());


        /*Τώρα θα βρούμε τις λοιπές πληροφορίες του Prescription που είναι το PrescriptionExecution*/
        int pe1 = p.get(0).getPrescriptionExecution().getId();//Παίρνω το ID του PrescriptionExecution
        query = em.createQuery("select pe from PrescriptionExecution pe where id ='" + pe1 + "'");
        List<PrescriptionExecution> pe = query.getResultList();
        assertEquals(1, pe.size());
        assertEquals("133331111", pe.get(0).getPharmacist().getAfm()); //ελέγξαμε και τον φαρμακοποιό.

    }

    //################   PHARMACIST ##########################

    @Test
    @TestTransaction
    void executePrescription(){

        /*Ο φαρμακοποιός θα βρει ποιά συνταγή δεν έχει γίνει EXECUTE και θα την εκτελέσει
        *
        * Εύρεση συνταγών που δεν έχουν γίνει EXECUTE*/
        Query query;
        query = em.createQuery("select p from Prescription p where prescription_execution is null order by doctorAMKA ");
        List<Prescription> prescriptions = query.getResultList();
        assertEquals(3, prescriptions.size());

        List<QuantityPrescription> qpCheck=null;
        List<PrescriptionExecution> peCheck = null;
        List<QuantityExecution> qeCheck = null;
        PrescriptionExecution prescriptionExecution = new PrescriptionExecution("11112222");
        QuantityExecution quantityExecution = new QuantityExecution();

        //System.out.println("************EXECUTE PRESCRIPTION FROM PHARMACIST*******************\n");
        for(Prescription prescription:prescriptions){
            // System.out.println("Id (prescription) = "+prescription.getId());
            // System.out.println("AMKA Doctor = "+prescription.getDoctorAMKA());
            // System.out.println("AMKA Patient = "+prescription.getPatientAMKA());
            // System.out.println("Diagnosi = "+prescription.getDiagnosis());


            /*Επιλογή της πρώτης συνταγής από τις 2 που δεν έχουν γίνει EXECUTE*/
            query = em.createQuery("select qp from QuantityPrescription qp where prescription_id = '"+ prescriptions.get(0).getId() +"'");
            List<QuantityPrescription> qps = query.getResultList();
            qpCheck = qps;
            assertEquals(1, qps.size());
            //Ελεγχος ότι η συνταγή που επέλεξα δεν έχει γίνει EXECUTE
            query = em.createQuery("select pe from PrescriptionExecution pe where prescription = '"+ prescriptions.get(0).getId() +"'");
            List<PrescriptionExecution> pes = query.getResultList();
            peCheck = pes;
            assertEquals(0, pes.size());


            /*System.out.println("\n*** DRUGS TO PRESCRIPTION***");
            for (QuantityPrescription qp : qps){
                System.out.println("\nId Quantity = "+qp.getId());
                System.out.println("Quantity = "+qp.getQuantityPrescription());
                System.out.println("Quantity pre_id = "+qp.getPrescription().getId());
                System.out.println("Quantity Drug_id= "+qp.getDrug().getId());
                System.out.println("Quantity Drug_name= "+qp.getDrug().getDrugName());
            }*/
        }

        //Ελέγχουμε ότι το αποτέλεσμα της πάθησης είναι το σωστό.
        Assertions.assertEquals("osfialgia", prescriptions.get(0).getDiagnosis());
        //Ελεγχος πόσα φάρμακα έργαψε ο Doctor στην Prescription
        Assertions.assertEquals(12, qpCheck.get(0).getQuantityPrescription());


        //Τώρα θα Κάνει ο Pharmacist -> PrescriptionExecution
        query = em.createQuery("select ph from Pharmacist ph where AFM = '133331111'");
        List<Pharmacist> pharmacists = query.getResultList();
        assertEquals(1, pharmacists.size());


        /*Δημιουργεί πρόβλημα καθώς τα έχω ενώσει με το Prescription με τέτοι
            τρόπο ώστε αν πάω να διαγράψω κάτι να κάνει LOOP και να βγάζει ERROR
        prescriptionExecution.setPrescription(prescriptions.get(0)); //!!!!!!!!Βάζω την Prescription που βρήκα πριν
         */

        //Quantity execution
        quantityExecution.setDrug(qpCheck.get(0).getDrug());
        quantityExecution.setPrescriptionExecution(prescriptionExecution);
        quantityExecution.setQuantityExecutionPieces(2);

        List<QuantityExecution> q_Check = new ArrayList<>();
        q_Check.add(quantityExecution);

        //System.out.println("############################"+prescriptions.get(0).getDoctorAMKA());
        //System.out.println("############################"+prescriptions.get(0).getId());

        prescriptionExecution.setPharmacist(pharmacists.get(0)); //Βάζω τον φαρμακοποιος
        prescriptionExecution.setExecutionDate(LocalDate.now().toString());
        prescriptionExecution.setDoctorAMKA(prescriptions.get(0).getDoctorAMKA());
        try {
            prescriptionExecution.setExecutionFlag(q_Check, qpCheck);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //System.out.println("\n############################"+prescriptionExecution.isExecutionFlag());
        //System.out.println("\n############################"+q_Check.get(0).getQuantityExecutionPieces());
        //System.out.println("\n############################"+qpCheck.get(0).getQuantityPrescription());

        //Εισαγωγή του quantityExecution στο Prescription
        prescriptionExecution.addQuantityExecution(quantityExecution);
        //Εισαγωγή της εκτέλεσης στην Prescription
        prescriptions.get(0).setPrescriptionExecution(prescriptionExecution);

        //PERSIST το EXECUTION
       // EntityTransaction tx = em.getTransaction();
        //tx.begin();

        em.persist(prescriptions.get(0));

        //tx.commit();
        //em.close();

        //em = JPAUtil.getCurrentEntityManager();
        //Ελέγχω ότι Η Flag είναι η True που σημαίνει ότι έχει γίναι εκτέλεση
        assertEquals(executionPrescriptionFlag.PARTIALLY,prescriptions.get(0).getPrescriptionExecution().isExecutionFlag());

    }


    //################   PRESCRIPTION  ##########################
    @Test
    @TestTransaction
    void queryPrescription () throws DomainException {
        Query query = em.createQuery("select p from Prescription p");
        List<Prescription> result = query.getResultList();
//        for (int i = 0; i < result.size(); i++){
//            System.out.println(result.get(i).getDoctorAMKA());
//        }
        assertEquals(5, result.size());


        Prescription p = result.get(0);
        assertNotNull(p.getDoctorAMKA());
        assertEquals("15020201111", p.getDoctorAMKA());
        assertEquals(2, p.getDrugs().size());
        //Ελέγχουμε  με τις 2 παρακάτω γραμμές ότι η συνταγή έχει ΠΟΣΟΤΗΤΑ
        Set<QuantityPrescription> quantityPrescriptions = p.getQuantityPrescriptions();
        assertEquals(2, quantityPrescriptions.size());
        //p.getPrescriptionExecution().setExecutionFlag((List<QuantityExecution>) p.getPrescriptionExecution().getQuantityExecutions(), (List<QuantityPrescription>) p.getQuantityPrescriptions());
        //PARTIALLY_EXECUTED καθώς δεν έχει δώσει ο Φαρμακοποιός τον πλήρη αριθμό φαρμάκων
        assertEquals(executionPrescriptionFlag.PARTIALLY, p.getPrescriptionExecution().isExecutionFlag());

        Prescription p1 = result.get(1);
        assertNotNull(p1.getDoctorAMKA());
        assertEquals("osfialgia", p1.getDiagnosis());
        assertEquals(1, p1.getDrugs().size());
        assertEquals(1 , p1.getDrugs().size());
        //ελέγχω ότι στην δεύτερη συνταγή το ΑΜΚΑ του ασθενή είναι το σωστό.
        assertEquals("12121202222", p1.getPatientToPrescription().getAmka());

    }


    //Δημιουργία από την αρχή μίας PRESCRIPTION
    @Test
    @TestTransaction
    void createNewPrescription(){
        //EntityTransaction tx = em.getTransaction();

        Query query;
        /*Με το παρακάτω θέλουμε να εισάγουμε μία καινούρια συνταγή*/
        //Κάνουμε ερώτηματα στην DB για να φέρουμε τα φάρμακα
        query = em.createQuery("select d from Drug d");
        List<Drug> drugs = query.getResultList();
        assertEquals(3,drugs.size()); //ελέγχω ότι φέρνει 3

        query = em.createQuery("select ip from Patient ip");
        List<Patient> patients = query.getResultList();
        assertEquals(2,patients.size()); //ελέγχω ότι φέρνει 2

        //Φέρνουμε όλους τους ιατρούς και διαλέγω τον 3ο
        query = em.createQuery("select dr from Doctor dr");
        List<Doctor> doctor = query.getResultList();
        assertEquals(3, doctor.size()); //ελέγχω ότι έρχεται 1 ιατρός
        Doctor dr3 = doctor.get(2);

        Drug d13 = drugs.get(0); //Βάζουμε το φάρμακο σε μία μεταβλητή για να το βάλω μετά σε συνταγή
        Drug d13_test = new Drug("Depon", 5.44, medicineCategory.ORIGINALS);
        assertTrue( drugs.contains(d13_test)); //ελέγχω ότι έφερε και το σωστό φάρμακο


        //Δημιουργία συνταγής
        query = em.createQuery("select p from Prescription p where id = 7003");
        List <Prescription> results = query.getResultList();
        Prescription p13 = results.get(0);
        assertEquals(1,results.size());
//        QuantityPrescription qp11 = new QuantityPrescription(p13, drugs.get(0), 1);
//        QuantityPrescription qp12 = new QuantityPrescription(p13, drugs.get(1), 1);
//        //p13.addDrug(drugs.get(0)); //βάζουμε 2 ΦΑΡΜΑΚΑ ΜΕΣΑ στην συνταγή αυτή
//        //p13.addDrug(drugs.get(2));
//        assertEquals(2, p13.getDrugs().size()); //ελέγχουμε ότι υπάρχουν 2 φάρμακα

        //Βάζουμε 2 ποσότητες αφού και τα φάρμακα είναι 2
        QuantityPrescription qp13 = new QuantityPrescription(p13, d13, 8); //Depon
        p13.addQuantityPrescription(qp13);

        QuantityPrescription qp14 = new QuantityPrescription(p13, drugs.get(2), 2); //Betedin
        p13.addQuantityPrescription(qp14);
        p13.setDoctorToPrescription(doctor.get(0));
        p13.setPatientToPrescription(patients.get(0));

        //tx.begin();

//        em.persist(p13);

        //tx.commit();
//        em.close();

        /*Ελέγχουμε ότι τελικά έχουν αποθηκευτεί 3 Prescription μετά το close του em
         * Ελέγχουμε ότι έχει αποθηκευτεί το φάρμακο και ότι τα φάρμακα μέσα στην συνταγή δεν είναι κενα*/
//        em = JPAUtil.getCurrentEntityManager();
        query = em.createQuery("select p from Prescription p order by doctorAMKA");
        List<Prescription> prescription = query.getResultList();
        assertEquals(5, prescription.size());
        assertEquals(8, qp13.getQuantityPrescription());
        // Εδώ έχω βάλει 2 φάρμακα και 2 ποσότητες
        assertEquals(3, p13.getQuantityPrescriptions().size());
        assertEquals("15020201111", p13.getDoctorToPrescription().getAmka());

    }

    /*Αν μία Prescription δεν έχει γίνει EXECUTE τότε θα μπορεί να πραγματοποιηθεί διαγραφή φαρμάκων*/
    @Test
    @TestTransaction
    void removeDrugFromPrescription() {
        //EntityTransaction tx = em.getTransaction();

        Query query;
        query = em.createQuery("select p from Prescription p where prescription_execution is null order by doctorAMKA ");
        List<Prescription> prescriptions = query.getResultList();
        assertEquals(3, prescriptions.size());

        query = em.createQuery("select qp from QuantityPrescription qp where prescription_id = '"+ prescriptions.get(0).getId() +"'");
        List<QuantityPrescription> qps = query.getResultList();
        assertEquals(1,qps.size());

        /*Βρίσκω και διαγράφω όλα τα QuantityPrescriptions τα οποία βρίσκονται μέσα στην Prescription*/
        for(int i=0;i< qps.size();i++){
            QuantityPrescription quantity = em.find(QuantityPrescription.class,qps.get(i).getId());
           // tx.begin();

            em.remove(quantity);

           // tx.commit();

        }


        //check if the drugs from the prescription changed
        query = em.createQuery("select qp from QuantityPrescription qp where prescription_id = '"+ prescriptions.get(0).getId() +"'");
        qps = query.getResultList();
        assertEquals(0,qps.size());

        //check if the Drugs haven't changed
        query = em.createQuery("select d from Drug d ");
        List<Drug> d = query.getResultList();
        assertEquals(3,d.size());

        //check if the Prescriptions haven't changed
        query = em.createQuery("select p from Prescription p");
        List<Prescription> p = query.getResultList();
        assertEquals(4,p.size());


        /*Παρακάτω θα κάνουμε χρήση της domainLogic setExecutionFlag(qes, qps); ότι δηλαδή ελέγχει
        τις τιμές του QuantityPrescription και QuantityExecution και ανάλογα τις τιμές τους
        καταχωρεί στην συνταγή EXECUTED, PARTIALLY, CANCELED

        Κενή λίστα μόνο για να εισάγουμε null τιμή μέσα στο setExecutionFlag(qes, qps);
        * ώστε να δει ότι δεν υπάρχει κάποια καταχώρηση φαρμάκων και να θέσει το Prescription ως CANCELED*/
        List<QuantityExecution> qes = new ArrayList<>();

        try {
            p.get(0).getPrescriptionExecution().setExecutionFlag(qes, qps);
        } catch (DomainException e) {
            assertEquals("QuantityExecution Pieces is Higher than Doctors QuantityPrescription", e.getMessage());
            //throw new RuntimeException(e);
        }
        //Ελέγχει ότι ένα Prescription το οποίο δεν έχει QuantityPrescription && QuantityExecution το θέτει η
        //setExecutionFlag(qes, qps); ως CANCELED
        assertEquals(executionPrescriptionFlag.CANCELED,p.get(0).getPrescriptionExecution().isExecutionFlag());
    }

    /*Υπόθεση ότι αν ένα PrescriptionExecution δεν είναι EXECUTED τότε θα μπορέσει να διαγραφεί*/
    @Test
    @TestTransaction
    void removeThePrescription() {
        //EntityTransaction tx = em.getTransaction();

        Query query;
        query = em.createQuery("select p from Prescription p where prescription_execution is null");
        List<Prescription> prescription = query.getResultList();
        assertEquals(3, prescription.size());

        //Αρχικα τα QuantityPrescription ήταν 4 και έπειτα θα γίνεουν 3
        Prescription prescription1 = em.find(Prescription.class, prescription.get(0).getId());
        query = em.createQuery("select qp from QuantityPrescription qp");
        List<QuantityPrescription> qpp = query.getResultList();
        assertEquals(6, qpp.size());

        //tx.begin();
        em.remove(prescription1);
       // tx.commit();

        query = em.createQuery("select qp from QuantityPrescription qp");
        qpp = query.getResultList();
        assertEquals(5, qpp.size());

        // tx.begin();
        // query = em.createNativeQuery("delete from prescriptions");
        // query.executeUpdate();
        //query = em.createQuery("delete from Prescription p where p.id = '"+prescription1.getId()+"'");
        // tx.commit();

        //Μετά την διαγραφή οι συνταγές θα γίνουν 1 από 2 που ήταν
        query = em.createQuery("select p from Prescription p where prescription_execution is null");
        List<Prescription> prescription2 = query.getResultList();
        assertEquals(2, prescription2.size());

    }



    //################   PRESCRIPTION EXECUTION  ##########################

    @Test
    @TestTransaction
    void queryPrescriptionExecution(){

        /*Επειδή έχουμε βάλει μόνο την 1η συνταγή να έχει γίνει EXECUTE με το παρακάτω ερώτημα ζητάω
        * να μου φέρουν όλες τις Prescriptions όπου έχει πραγματοποιηθεί EXECUTE
        * Παρακάτω ελέγχουμε με το 1ο Equal έχει περαστεί η Execute
        * και με το δεύτερο ότι ήρθε μόνο μία εγγραφή*/
        Query query = em.createQuery("select p from Prescription p where prescription_execution is not null");
        List<Prescription> result = query.getResultList();
        assertEquals(executionPrescriptionFlag.PARTIALLY, result.get(0).getPrescriptionExecution().isExecutionFlag());
        assertEquals(2, result.size());
        //Ελέγχουμε ότι έχει και Pharmacist και ότι ο AFM του έιναι το σωστό
        assertEquals("133331111", result.get(0).getPrescriptionExecution().getPharmacist().getAfm());
        //Ελέγχουμε ότι τελικά το QuantityExecution Εσηχθεί στο PrescriptionExecution
        assertEquals(2, result.get(0).getPrescriptionExecution().getQuantityExecutions().size());

    }

    @Test
    @TestTransaction
    void executeThePrescription() {

        //Επιστρεφει τις συνταγές οι οποίες έχουν γίνει EXECUTE
        Query query;
        query = em.createQuery("select p from Prescription p where prescription_execution is not null");
        List<Prescription> prescriptions = query.getResultList();
        assertEquals(2, prescriptions.size());
        //assertEquals(7000, prescriptions.get(0).getId());


        List<QuantityPrescription> qpCheck;
        List<PrescriptionExecution> peCheck = null;
        List<QuantityExecution> qeCheck = null;

        //System.out.println("************* PRESCRIPTION EXECUTION TEST in JPAQueries*****************");
        for(Prescription prescription:prescriptions){
            //System.out.println("Id (prescription) = "+prescription.getId());
            //System.out.println("AMKA Doctor = "+prescription.getDoctorAMKA());
            //System.out.println("AMKA Patient = "+prescription.getPatientAMKA());
            //System.out.println("Diagnosi = "+prescription.getDiagnosis());


            /*Είναι μεσα στην FOR και επιστρέφει για κάθε συνταγη που βρήκε τα QuantityPrescription της κάθε μίας*/
            query = em.createQuery("select qp from QuantityPrescription qp where prescription_id = '"+ prescription.getId() +"'");
            List<QuantityPrescription> qps = query.getResultList();
            qpCheck = qps;
            //assertEquals(2, qps.size());
            query = em.createQuery("select pe from PrescriptionExecution pe ");
            List<PrescriptionExecution> pes = query.getResultList();
            peCheck = pes;
            assertEquals(2, pes.size());


            for(PrescriptionExecution pe : pes){
                //System.out.println("\nΕΚΤΕΛΕΣΗ ΣΥΝΤΑΓΗΣ");
                //System.out.println("PrescriptionExecution ID "+pe.getId());
                //System.out.println("QuantityExecution size "+pe.getQuantityExecutions().size());

                query = em.createQuery("select qe from QuantityExecution qe where prescriptionExecution_id =  '"+ pe.getId() +"'");
                List<QuantityExecution> qes = query.getResultList();
                qeCheck = qes;

                /*for(QuantityExecution qe : qes){
                    System.out.println("Ποσότητα Που πραγματική λήφθηκε");
                    System.out.println("QuantityExecution ID "+qe.getId());
                    System.out.println("REAL Quantity "+qe.getQuantityExecutionPieces());
                }*/
            } //for PrescriptionExecution

           /* System.out.println("\nΣυσχετίσεις με φάρμακα");
            for (QuantityPrescription qp : qps){
                System.out.println("\nId Quantity = "+qp.getId());
                System.out.println("Quantity = "+qp.getQuantityPrescription());
                System.out.println("Quantity pre_id = "+qp.getPrescription().getId());
                System.out.println("Quantity Drug_id= "+qp.getDrug().getId());
                System.out.println("Quantity Drug_name= "+qp.getDrug().getDrugName());
            }*/
        } //for Prescription


        //Ελέγχουμε ότι από την συνταγή μπορουμε να έχουμε πρόσβαση στο κόστος
        assertEquals(10.88, prescriptions.get(0).getPrescriptionExecution().getSummaryCost(qeCheck));
        //Ελέγχουμε ότι από την συνταγή θα μπορουμε να έχουμε πρόσβαση στον φαρμακοποιό
        assertEquals("133331111", prescriptions.get(0).getPrescriptionExecution().getPharmacist().getAfm());
        //Ελέγχουμε ότι η συνταγή έχει μέσα στην πληροφορία της ποσότητας ΠΟΥ Πραγματικά παρακήφθηκαν
        assertEquals(2, peCheck.get(0).getQuantityExecutions().size());
        //Ελέγχω ότι από την Prescription από το 2ο Drug Λήφθηκαν Πραγματικά 7 Pieces
        //assertEquals(7, qeCheck.get(1).getQuantityExecutionPieces());
    }



    //################   QUANTITY PRESCRIPTION  ##########################

    @Test
    @TestTransaction
    void queryQuantityPrescription() {
        Query query;

        query = em.createQuery("select p from Prescription p where prescription_execution is not null");
        List<Prescription> prescriptions = query.getResultList();
        assertEquals(2, prescriptions.size());

        //Καθολική μεταβλητή για να λάβω μέσα από την FOR τις ποσότητες
        List<QuantityPrescription> qpCheck = new ArrayList<>();
        List<String> drug_test = new ArrayList<String>();

        //System.out.println("*********** PRESCRIPTION in JPAQueries*****************" );
        for (Prescription prescription : prescriptions) {
            //System.out.println("Id (prescription) = " + prescription.getId());
            //System.out.println("AMKA Doctor = " + prescription.getDoctorAMKA());
            //System.out.println("AMKA Patient = " + prescription.getPatientAMKA());
            //System.out.println("Diagnosi = " + prescription.getDiagnosis());

            query = em.createQuery("select qp from QuantityPrescription qp where prescription_id = '" + prescription.getId() + "'");
            List<QuantityPrescription> qps = query.getResultList();
            qpCheck = qps;
            //assertEquals(1, qps.size());

            /*(System.out.println("\n************ QUANTITY PRESCRIPTION *************");
            for (QuantityPrescription qp : qps) {
                System.out.println("\nId Quantity = " + qp.getId());
                System.out.println("Quantity = " + qp.getQuantityPrescription());
                System.out.println("Quantity pre_id = " + qp.getPrescription().getId());
                System.out.println("Quantity Drug_id= " + qp.getDrug().getId());
                System.out.println("Quantity Drug_name= " + qp.getDrug().getDrugName());
            }*/

            query = em.createQuery("select d from Drug d ");
            List<Drug> d = query.getResultList();
            Drug dCheck = null;

            /*System.out.println("\n***********DRUG*****************" );
            for (Drug ds : d) {
                System.out.println("\nId DRUG = " + ds.getId());
                System.out.println("DRUG name = " + ds.getDrugName());
            }*/

            drug_test.add(qpCheck.get(0).getDrug().getDrugName());
            //drug_test.add(qpCheck.get(1).getDrug().getDrugName());
        }

        assertEquals(true, drug_test.contains("Depon"));
        assertEquals(1, qpCheck.size());
    }


    //################   GuidedPrescription  ##########################
    @Test
    @TestTransaction
    void checkIfIsInsideLimits(){
        //EntityTransaction tx = em.getTransaction();

        //executed prescriptions
        Query query;
        query = em.createQuery("select qp from QuantityPrescription qp join fetch qp.prescription where prescription_execution is not null order by drug_id");
        List<QuantityPrescription> q_prescriptions = query.getResultList();
        assertEquals(3, q_prescriptions.size()); //1 prescription with 2 drugs

        query = em.createQuery("select d from Drug d join fetch d.activeSubstance");
        List<Drug> ac_sub = query.getResultList();
        assertEquals(3,ac_sub.size()); //3 drugs with different substances

        ArrayList<Boolean> qpFlag = new ArrayList<Boolean>();
        for ( int i=0; i<q_prescriptions.size(); i++){
            qpFlag.add(q_prescriptions.get(i).isInsideLimit());
            System.out.println("For Drug = "+ q_prescriptions.get(i).getDrug().getDrugName() +" FLAG is = "+ qpFlag.get(i));
        }

        assertTrue(qpFlag.get(0)!=qpFlag.get(1));

    }

}