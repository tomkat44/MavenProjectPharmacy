package com.example.app.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DoctorRepresentation {
    public Integer id;
    public String firstName;
    public String lastName;
    public String amka;
    public String afm;
    public String email;
}
