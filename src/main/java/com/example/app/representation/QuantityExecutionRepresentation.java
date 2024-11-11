package com.example.app.representation;

import com.example.app.domain.*;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class QuantityExecutionRepresentation {
    public Integer id;
    public Integer quantityExecutionPieces;

    public DrugRepresentation drug;





    //public PrescriptionExecutionRepresentation prescriptionExecutionRepresentation;
}
