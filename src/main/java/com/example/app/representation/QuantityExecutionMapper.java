package com.example.app.representation;

import com.example.app.domain.QuantityExecution;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {DrugMapper.class})
public abstract class QuantityExecutionMapper {

    @Mapping(target = "drug", source = "drug")
    public abstract QuantityExecutionRepresentation toRepresentation(QuantityExecution qe);
    @Mapping(target = "drug", source = "drug")
    public abstract QuantityExecution toModel(QuantityExecutionRepresentation dto);

    public abstract List<QuantityExecution> toRepresentationList(List<QuantityExecution> qes);
}
