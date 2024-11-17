package com.example.app.representation;

import com.example.app.domain.Prescription;
import com.example.app.domain.executionPrescriptionFlag;

import javax.inject.Inject;

public class PrintPrescriptionRepresentation {

    @Inject
    PrescriptionMapper prescriptionMapper;
    @Inject
    ActiveSubstanceRepresentation activeSubstanceRepresentation;
    @Inject
    DoctorRepresentation doctorRepresentation;

    public void print(PrescriptionRepresentation prescriptionRepresentation){

        System.out.println();
        System.out.println("PRINT THE PRESCRIPTION's DETAILS");
        System.out.println("Id of prescription: "+prescriptionRepresentation.id);
        System.out.println();

        System.out.println("AMKA of the Doctor of the prescription: "+prescriptionRepresentation.doctorAMKA);
        System.out.println();

        System.out.println("Doctor of the prescription - first_name, last_name");
        System.out.println("First Name: "+prescriptionRepresentation.doctorToPrescription.firstName);
        System.out.println("Last Name: "+prescriptionRepresentation.doctorToPrescription.lastName);
        System.out.println();

        System.out.println("AMKA of the Patient of the prescription: "+prescriptionRepresentation.patientAMKA);
        System.out.println();

        System.out.println("Diagnosis of the patient: "+prescriptionRepresentation.diagnosis);
        System.out.println();

        System.out.println("Quantities of drugs in the prescription written by the doctor - id, drugName, quantity");
        for (int i=0;i<prescriptionRepresentation.quantityPrescriptions.size();i++){
            System.out.println(i+1+".");
            System.out.println("Id: "+prescriptionRepresentation.quantityPrescriptions.get(i).id);
            System.out.println("Drug Name: "+prescriptionRepresentation.quantityPrescriptions.get(i).drug.drugName);
            System.out.println("Drug Quantity: "+prescriptionRepresentation.quantityPrescriptions.get(i).quantityPrescription);
        }

        System.out.println();
        System.out.println("Pharmacist of the execution of the prescription - first_name, last_name");
        System.out.println("First Name: "+prescriptionRepresentation.prescriptionExecution.pharmacist.lastName);
        System.out.println("Last Name: "+prescriptionRepresentation.prescriptionExecution.pharmacist.firstName);

        if(prescriptionRepresentation.prescriptionExecution!=null){
            System.out.println();
            System.out.println("Quantities of drugs that patient end up buying from the prescription - id, quantity execution");
            System.out.println("Id: "+prescriptionRepresentation.prescriptionExecution.id);
            for (int i=0;i<prescriptionRepresentation.prescriptionExecution.quantityExecutions.size();i++){
                System.out.println(i+1+".");
                System.out.println("Quantity Executed: "+prescriptionRepresentation.prescriptionExecution.quantityExecutions.get(i).quantityExecutionPieces);
            }

            System.out.println();
            if(prescriptionRepresentation.prescriptionExecution.executionFlag.equals(executionPrescriptionFlag.PARTIALLY)){
                System.out.println("Type of execution: "+prescriptionRepresentation.prescriptionExecution.executionFlag+" EXECUTED");
            }else {
                System.out.println("Type of execution: " + prescriptionRepresentation.prescriptionExecution.executionFlag);
            }
            System.out.println();
            System.out.println("Total cost of the drugs that the patient bought: "+prescriptionRepresentation.prescriptionExecution.summaryCost+"€");
            System.out.println();

        }

        System.out.println("--------END OF PRINT--------");


    }

}
