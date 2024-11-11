package com.example.app.representation;

import com.example.app.domain.*;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class PrescriptionRepresentation {
    public Integer id;
    public String doctorAMKA;
    public String patientAMKA;
    public String diagnosis;
    public String creationDate;
    public DoctorRepresentation doctorToPrescription;
    public PatientRepresentation patientToPrescription;

    public List<QuantityPrescriptionRepresentation> quantityPrescriptions = new ArrayList<QuantityPrescriptionRepresentation>();

    //DrugRepresentation drug = quantityPrescriptions.get(0).drugRepresentation;

    //public List<DrugRepresentation> drugRepresentations = new ArrayList<DrugRepresentation>();

    public PrescriptionExecutionRepresentation prescriptionExecution;

    //public List<QuantityExecutionRepresentation> quantityExecutions = new ArrayList<QuantityExecutionRepresentation>();

}
