package com.example.app.representation;

import java.util.ArrayList;
import java.util.List;
import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ActiveSubstanceRepresentation {
    public Integer id;
    public String substanceName;
    public  Integer quantity;
}
