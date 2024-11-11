package com.example.app.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class PrescriptionRepresentationBusted {
    public Integer id;
    public String doctorAMKA;
    public String patientAMKA;
    public String diagnosis;
    public DoctorRepresentation doctorToPrescription;
    public PatientRepresentation patientToPrescription;



}
