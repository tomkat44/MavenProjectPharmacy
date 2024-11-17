package com.example.app.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PharmacistRepresentation {
    public Integer id;
    public String firstName;
    public String lastName;
    public String afm;
    public String email;

}
