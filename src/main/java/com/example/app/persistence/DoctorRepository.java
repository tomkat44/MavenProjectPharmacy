package com.example.app.persistence;

import com.example.app.domain.Doctor;
import com.example.app.domain.Patient;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class DoctorRepository implements PanacheRepositoryBase<Doctor, Integer> {

    public List<Doctor> search(String afm) {
        System.out.println("PATIENT AMKA REPOSITORY SEARCH = " + afm);
        if (afm == null) {
            return listAll();
        }

        return find("select d from Doctor d where d.afm like :dafm" ,
                Parameters.with("dafm", afm + "%").map())
                .list();
    }

}
