package com.example.app.representation;


import com.example.app.domain.Prescription;
import com.example.app.domain.QuantityPrescription;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {QuantityPrescriptionMapper.class, PrescriptionExecutionMapper.class,
        QuantityExecutionMapper.class, PharmacistMapper.class, DrugMapper.class})
public abstract class PrescriptionMapper {

   //@Mapping(target = "quantityPrescriptions", source = "quantityPrescriptions")
   //@Mapping(target = "prescriptionExecution", ignore = true)
   @Mapping(target = "quantityPrescriptions", source = "quantityPrescriptions")
    public abstract PrescriptionRepresentation toRepresentation(Prescription prescription);


    //@Mapping(target = "quantityPrescriptions", source = "quantityPrescriptions")
    //@Mapping(target = "prescriptionExecution", ignore = true)
    @Mapping(target = "quantityPrescriptions", source = "quantityPrescriptions")
    public abstract Prescription toModel(PrescriptionRepresentation dto);

    public abstract List<PrescriptionRepresentation> toRepresentationList(List<Prescription> prescriptions);
}
