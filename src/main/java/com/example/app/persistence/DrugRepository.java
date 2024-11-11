package com.example.app.persistence;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.NoResultException;


import com.example.app.domain.Drug;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import static io.quarkus.hibernate.orm.panache.PanacheEntityBase.listAll;

/*Εδώ καλώ domain Logic και την μετατρέπω σε κατάλληλα ερωτήματα*/
@RequestScoped
public class DrugRepository implements PanacheRepositoryBase<Drug, Integer>{

    public List<Drug> search(String drugName) {
        System.out.println("DRUG NAME = "+ drugName);
        if (drugName == null) {
            return listAll();
        }

        return find("select d from Drug d where d.drugName like :drugN" ,
                Parameters.with("drugN", drugName + "%").map())
                .list();
    }



}
