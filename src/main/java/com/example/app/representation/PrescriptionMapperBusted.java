package com.example.app.representation;


import com.example.app.domain.Prescription;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {QuantityPrescriptionMapper.class, PrescriptionExecutionMapper.class,
        QuantityExecutionMapper.class, PharmacistMapper.class, DrugMapper.class})
public abstract class PrescriptionMapperBusted {

    public abstract PrescriptionRepresentation toRepresentation(Prescription prescription);

    public abstract Prescription toModel(PrescriptionRepresentation dto);

    public abstract List<PrescriptionRepresentation> toRepresentationList(List<Prescription> prescriptions);
}
