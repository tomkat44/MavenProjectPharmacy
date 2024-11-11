package com.example.app.representation;

import com.example.app.domain.PrescriptionExecution;
import com.example.app.domain.QuantityExecution;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {QuantityExecutionMapper.class, PharmacistMapper.class})
public abstract class PrescriptionExecutionMapper {

    @Mapping(target = "quantityExecutions", source = "quantityExecutions")
    public abstract PrescriptionExecutionRepresentation toRepresentation(PrescriptionExecution pe);

    @Mapping(target = "quantityExecutions", source = "quantityExecutions")
    public abstract PrescriptionExecution toModel(PrescriptionExecutionRepresentation dto);

    public abstract List<PrescriptionExecutionRepresentation> toRepresentationList(List<PrescriptionExecution> qes);
}
