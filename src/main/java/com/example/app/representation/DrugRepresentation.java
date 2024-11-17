package com.example.app.representation;

import com.example.app.domain.medicineCategory;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DrugRepresentation {
    public Integer id;
    public String drugName;
    public  double drugPrice;
    public com.example.app.domain.medicineCategory medicineCategory;
    public ActiveSubstanceRepresentation activeSubstance;
}
