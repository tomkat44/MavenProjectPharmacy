package com.example.app.representation;

import com.example.app.domain.QuantityExecution;
import com.example.app.domain.executionPrescriptionFlag;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RegisterForReflection
public class PrescriptionExecutionRepresentation {
    public Integer id;
    public String executionDate;
    public Double summaryCost;
    public executionPrescriptionFlag executionFlag;
    public PharmacistRepresentation pharmacist;
    public List<QuantityExecutionRepresentation> quantityExecutions = new ArrayList<QuantityExecutionRepresentation>();

}
