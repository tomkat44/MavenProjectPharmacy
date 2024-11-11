package com.example.app.persistence;

import com.example.app.domain.Doctor;
import com.example.app.domain.Drug;
import com.example.app.domain.Patient;
import com.example.app.domain.Pharmacist;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class PatientRepository implements PanacheRepositoryBase<Patient, Integer> {

    public List<Patient> search(String amka) {
        System.out.println("PATIENT AMKA REPOSITORY SEARCH = " + amka);
        if (amka == null) {
            return listAll();
        }

        return find("select p from Patient p where p.amka like :pat" ,
                Parameters.with("pat", amka + "%").map())
                .list();
    }

}
