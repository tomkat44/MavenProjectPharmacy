package com.example.app.representation;

import java.util.List;

import com.example.app.domain.ActiveSubstance;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)

public abstract class ActiveSubstanceMapper {

    @Mapping(target = "substanceName", source = "name")
    public abstract ActiveSubstanceRepresentation toRepresentation(ActiveSubstance as);

    //Για να μετατρέψει από Representation σε domain
    @Mapping(target = "name", source = "substanceName")
    public abstract ActiveSubstance toModel(ActiveSubstanceRepresentation dto);

    public abstract List<ActiveSubstanceRepresentation> toRepresentationList(List<ActiveSubstance> as);
}
