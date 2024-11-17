package com.example.app.representation;

import com.example.app.domain.Doctor;
import com.example.app.domain.QuantityPrescription;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {DrugMapper.class, PrescriptionMapper.class})
public abstract class QuantityPrescriptionMapper {

    @Mapping(target = "drug", source = "drug")
   // @Mapping(target = "prescription", ignore = true)
    public abstract QuantityPrescriptionRepresentation toRepresentation(QuantityPrescription qp);

    @Mapping(target = "drug", source = "drug")
    //@Mapping(target = "prescription", source = "prescription")
    //@Mapping(target = "prescription", ignore = true)
    public abstract QuantityPrescription toModel(QuantityPrescriptionRepresentation dto);

    public abstract List<QuantityPrescriptionRepresentation> toRepresentationList(List<QuantityPrescription> qps);
}
