package com.example.app.representation;

import com.example.app.domain.Patient;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class PatientsMapper {

    public abstract PatientRepresentation toRepresentation(Patient p);

    public abstract Patient toModel(PatientRepresentation dto);
    public abstract List<PatientRepresentation> toRepresentationList(List<Patient> doctors);
}
