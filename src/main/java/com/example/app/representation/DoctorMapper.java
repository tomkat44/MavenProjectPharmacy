package com.example.app.representation;

import com.example.app.domain.Doctor;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class DoctorMapper {

    public abstract DoctorRepresentation toRepresentation(Doctor d);

    public abstract Doctor toModel(DoctorRepresentation dto);
    public abstract List<DoctorRepresentation> toRepresentationList(List<Doctor> doctors);
}
