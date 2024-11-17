package com.example.app.representation;

import com.example.app.domain.ActiveSubstance;
import com.example.app.domain.Pharmacist;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PharmacistMapper {

    public abstract PharmacistRepresentation toRepresentation(Pharmacist ph);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    public abstract Pharmacist toModel(PharmacistRepresentation dto);
    public abstract List<PharmacistRepresentation> toRepresentationList(List<Pharmacist> ph);
}
