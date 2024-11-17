package com.example.app.representation;

import com.example.app.domain.Drug;
import com.example.app.domain.Pharmacist;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ActiveSubstanceMapper.class})
public abstract class DrugMapper {

    public abstract DrugRepresentation toRepresentation(Drug d);

    @Mapping(target = "activeSubstance", source = "activeSubstance")
    public abstract Drug toModel(DrugRepresentation dto);

    public abstract List<DrugRepresentation> toRepresentationList(List<Drug> d);
}
