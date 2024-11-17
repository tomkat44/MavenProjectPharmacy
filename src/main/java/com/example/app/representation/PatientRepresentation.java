package com.example.app.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PatientRepresentation {
    public Integer id;
    public String firstName;
    public String lastName;
    public String amka;
    public String email;

}
