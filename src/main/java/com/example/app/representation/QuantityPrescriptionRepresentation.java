package com.example.app.representation;

import com.example.app.domain.Drug;
import com.example.app.domain.Prescription;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class QuantityPrescriptionRepresentation {
    public Integer id;
    public Integer quantityPrescription;
    public DrugRepresentation drug;
    //public Prescription prescription;

}
