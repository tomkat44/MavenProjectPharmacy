package com.example.app.persistence;

import java.util.List;

import javax.enterprise.context.RequestScoped;

import com.example.app.domain.ActiveSubstance;
import com.example.app.domain.Pharmacist;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

@RequestScoped
public class ActiveSubstanceRepository implements PanacheRepositoryBase<ActiveSubstance, Integer>{

    public List<ActiveSubstance> search(String substanceName) {
        if (substanceName == null) {
            return listAll();
        }

        return find("select a from ActiveSubstance a where a.name like :substanceN" ,
                Parameters.with("substanceN", substanceName + "%").map())
                .list();
    }

    public void deleteActiveSubstance(Integer id) {

         find("delete from ActiveSubstance where id = :asId" ,
                Parameters.with("asId", id + "%").map())
                .list();
    }


    public List<ActiveSubstance> findByName(String substanceN){
        return list("substanceName", substanceN);
    }
}
