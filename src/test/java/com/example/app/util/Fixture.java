package com.example.app.util;

import com.example.app.domain.*;
import com.example.app.persistence.ActiveSubstanceRepository;
import com.example.app.representation.*;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.example.app.domain.executionPrescriptionFlag.PENDING;
import static com.example.app.domain.medicineCategory.ORIGINALS;
import static com.example.app.domain.executionPrescriptionFlag.EXECUTED;

public class Fixture {


    public static class ActiveSubstances {
        public static int AS_ID = 5000;
        public static String AS_NAME = "Paraketamol";
    }

    public static ActiveSubstanceRepresentation getNewActiveSubstanceRepresentation() {
        ActiveSubstanceRepresentation dto = new ActiveSubstanceRepresentation();
        //dto.id = 5004;
        dto.substanceName = "alpinok";
        dto.quantity = 10;

        return dto;
    }

    public static ActiveSubstanceRepresentation getExistingActiveSubstanceRepresentation() {
        ActiveSubstanceRepresentation dto = new ActiveSubstanceRepresentation();
        dto.id = 5006;
        dto.substanceName = "Nabilon";
        dto.quantity = 10;

        return dto;
    }

    public static PharmacistRepresentation getExistingPharmacistRepresentation2() {
        PharmacistRepresentation dto = new PharmacistRepresentation();
        dto.id = 4001;
        dto.firstName = "Don";
        dto.lastName = "Volikas";
        dto.afm = "133332222";
        dto.email = "don@pharmacist.com";

        return dto;
    }

    public static PharmacistRepresentation getPharmacistRepresentation(){
        PharmacistRepresentation dto = new PharmacistRepresentation();
        //dto.id = 4008;
        dto.firstName = "Dim";
        dto.lastName = "Lack";
        dto.afm = "1284";
        dto.email = "ph@pd.ph";

        return  dto;
    }



    //αυτός είναι για το prescription
    public static PharmacistRepresentation getExistingPharmacistRepresentation(){
        PharmacistRepresentation dto = new PharmacistRepresentation();
        dto.id = 4000;
        dto.firstName = "Maria";
        dto.lastName = "Damiges";
        dto.afm = "133331111";
        dto.email = "maria@pharmacist.com";

        return  dto;
    }
    public static class Drugs {
        public static int DRUG_ID = 6000;
        public static String DRUG_NAME = "Depon";
    }

    public static DoctorRepresentation getDoctorRepresentation(){
        DoctorRepresentation dto = new DoctorRepresentation();
        //dto.id = 2005;
        dto.firstName = "Dim";
        dto.lastName = "Lack";
        dto.amka="12345";
        dto.afm = "1234";
        dto.email = "ph@pd.ph";

        return  dto;
    }

    public static DoctorRepresentation getExistingDoctorRepresentation(){
        DoctorRepresentation dto = new DoctorRepresentation();
        dto.id = 2002;
        dto.firstName = "Christina";
        dto.lastName = "Xoleva";
        dto.amka="15020203333";
        dto.afm = "144443333";
        dto.email = "chris@doctor.com";

        return  dto;
    }

    public static PatientRepresentation getExistingPatientRepresentation(){
        PatientRepresentation dto = new PatientRepresentation();
        dto.id = 3001;
        dto.firstName = "Laoura";
        dto.lastName = "Narges";
        dto.amka="12121202222";
        dto.email = "laoura@patient.com";

        return  dto;
    }

    public static PatientRepresentation getNewPatientRepresentation(){
        PatientRepresentation dto = new PatientRepresentation();
        //dto.id = 3099;
        dto.firstName = "Claudio";
        dto.lastName = "Maldini";
        dto.amka="12121202223";
        dto.email = "maldidni@patient.com";

        return  dto;
    }

    public static PatientRepresentation getExistingPatientRepresentation3000(){
        PatientRepresentation dto = new PatientRepresentation();
        dto.id = 3000;
        dto.firstName = "Spyros";
        dto.lastName = "Alexandrou";
        dto.amka="12121201112";
        dto.email = "spy@patient.com";

        return  dto;
    }


    public static DrugRepresentation getNewDrugRepresentation(){
        DrugRepresentation dto = new DrugRepresentation();
        ActiveSubstanceRepresentation a = new ActiveSubstanceRepresentation();
        a.id = 5000;
        a.substanceName = "Paraketamol";
        a.quantity = 10;

        dto.id = 6010;
        dto.drugName = "Asvestio";
        dto.drugPrice = 8.88;
        dto.medicineCategory = ORIGINALS;
        dto.activeSubstance = a;

        return dto;
    }

    public static DrugRepresentation getExistingDrugRepresentation(){
        DrugRepresentation dto = new DrugRepresentation();
        ActiveSubstanceRepresentation a = new ActiveSubstanceRepresentation();
        a.id = 5000;
        a.substanceName = "Paraketamol";
        a.quantity = 10;

        dto.id = 6000;
        dto.drugName = "Depon";
        dto.drugPrice = 5.44;
        dto.medicineCategory = ORIGINALS;
        dto.activeSubstance = a;

        return dto;
    }

    public static QuantityPrescriptionRepresentation getQuantityPrescription(){

        QuantityPrescriptionRepresentation dto = new QuantityPrescriptionRepresentation();
        dto.id = 8010;
        dto.quantityPrescription = 11;
        dto.drug = Fixture.getNewDrugRepresentation();
        return dto;
    }

    public static QuantityPrescriptionRepresentation getNewQuantityPrescriptionWithExistingDrugPUT(){

        QuantityPrescriptionRepresentation dto = new QuantityPrescriptionRepresentation();
        //dto.id = 8003;
        dto.quantityPrescription = 20;
        dto.drug = Fixture.getExistingDrugRepresentation();
        return dto;
    }


    public static QuantityPrescriptionRepresentation getExistingQuantityPrescription(){

        QuantityPrescriptionRepresentation dto = new QuantityPrescriptionRepresentation();
        dto.id = 8001;
        dto.quantityPrescription = 11;
        dto.drug = Fixture.getExistingDrugRepresentation();
        //dto.quantityPrescription = Fixture.
        return dto;
    }

    public static QuantityExecutionRepresentation getNewQuantityExecution(){
        QuantityExecutionRepresentation dto = new QuantityExecutionRepresentation();
        dto.id = 1110;
        dto.quantityExecutionPieces = 11;
        dto.drug = Fixture.getNewDrugRepresentation();

        return dto;
    }

    public static QuantityExecutionRepresentation getNewQuantityExecutionWithExistingDrug(){
        QuantityExecutionRepresentation dto = new QuantityExecutionRepresentation();
        //dto.id = 1111;
        dto.quantityExecutionPieces = 2;
        dto.drug = Fixture.getExistingDrugRepresentation();

        return dto;
    }


    public static PrescriptionExecutionRepresentation getPrescriptionExecution(){
        PrescriptionExecutionRepresentation dto = new PrescriptionExecutionRepresentation();
        //dto.id = 9010;
        dto.executionDate = LocalDate.now().toString();
        //Τα έβγαλα για να δείξω όι δεν χρειάζονται και ότι λαμβάνει τιμές αυτόματα
        //dto.executionFlag = PENDING;
        //dto.summaryCost = 8.88;
        dto.pharmacist = Fixture.getExistingPharmacistRepresentation();
        dto.quantityExecutions = new ArrayList<>();
        dto.quantityExecutions.add(Fixture.getNewQuantityExecutionWithExistingDrug());

        return dto;
    }

    public static DrugRepresentation getNewDrugRepresentationForPost(){

        ActiveSubstanceRepresentation a = new ActiveSubstanceRepresentation();
        a.id = 5000;
        a.substanceName = "Paraketamol";
        a.quantity = 10;

        DrugRepresentation dto = new DrugRepresentation();
        //dto.id = 6005;
        dto.activeSubstance=a;

        dto.drugName = "Panadol";
        dto.drugPrice = 9.9;
        dto.medicineCategory = ORIGINALS;

        return dto;
    }

    public static PrescriptionRepresentation getPrescriptionRepresentation(){

        PrescriptionRepresentation dto = new PrescriptionRepresentation();
        //dto.id = 7004;
        dto.doctorAMKA = "12345678";
        dto.patientAMKA = "23456789";
        dto.diagnosis = "Bored";
        //dto.creationDate = null;
        dto.doctorToPrescription = Fixture.getExistingDoctorRepresentation();
        dto.patientToPrescription = Fixture.getExistingPatientRepresentation();

        //dto.drugRepresentations.add(Fixture.getNewDrugRepresentation());
        dto.quantityPrescriptions = new ArrayList<>();
        //dto.quantityPrescriptions.add(Fixture.getExistingQuantityPrescription());
        dto.quantityPrescriptions.add(Fixture.getNewQuantityPrescriptionWithExistingDrugPUT());

        //dto.quantityExecutions.add(Fixture.getQuantityExecution());

        dto.prescriptionExecution = Fixture.getPrescriptionExecution();

        //dto.prescriptionExecution = null;


        return dto;
    }

    public static PrescriptionRepresentation getPrescriptionRepresentationWithoutPrescriptionExecution(){

        PrescriptionRepresentation dto = new PrescriptionRepresentation();
        //dto.id = 7004;
        dto.doctorAMKA = "12345678";
        dto.patientAMKA = "23456789";
        dto.diagnosis = "Bored";
        //dto.creationDate = null;
        dto.doctorToPrescription = Fixture.getExistingDoctorRepresentation();
        dto.patientToPrescription = Fixture.getExistingPatientRepresentation();

        //dto.drugRepresentations.add(Fixture.getNewDrugRepresentation());
        dto.quantityPrescriptions = new ArrayList<>();
        //dto.quantityPrescriptions.add(Fixture.getExistingQuantityPrescription());
        dto.quantityPrescriptions.add(Fixture.getNewQuantityPrescriptionWithExistingDrugPUT());

        dto.prescriptionExecution = null;


        return dto;
    }

    public static PrescriptionRepresentation getExistingPrescriptionRepresentation(){

        PrescriptionRepresentation dto = new PrescriptionRepresentation();
        dto.id = 7002;
        dto.doctorAMKA = "15020203333";
        dto.patientAMKA = "12121201112";
        dto.diagnosis = "brokenLeg";
        //dto.creationDate = null;
        dto.doctorToPrescription = Fixture.getExistingDoctorRepresentation();
        dto.patientToPrescription = Fixture.getExistingPatientRepresentation3000();

        //dto.drugRepresentations.add(Fixture.getNewDrugRepresentation());
        dto.quantityPrescriptions = new ArrayList<>();
        dto.quantityPrescriptions.add(getNewQuantityPrescriptionWithExistingDrugPUT());

        //dto.quantityExecutions.add(Fixture.getQuantityExecution());

        //dto.prescriptionExecution = getPrescriptionExecution();
        dto.prescriptionExecution = null;


        return dto;
    }

    public static String API_ROOT  = "http://localhost:8081";
    //  ./mvnw compile quarkus:dev
}
