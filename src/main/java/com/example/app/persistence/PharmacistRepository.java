package com.example.app.persistence;

import com.example.app.domain.Pharmacist;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@RequestScoped
public class PharmacistRepository implements PanacheRepositoryBase<Pharmacist, Integer> {

    @Inject
    EntityManager em;

    public List<Pharmacist> search(String email) {
        System.out.println("EMAIL SEARCHE REPOSITORY = " + email);
        if (email == null) {
            return listAll();
        }

        return find("select ph from Pharmacist ph where ph.email like :phEmail" ,
                Parameters.with("phEmail", email + "%").map())
                .list();
    }

    //-------------------------------DIMITRIS----------------------------------------
    //--------search by pharmacist afm----
    public List<Pharmacist> searchByAfm(String afm) {
        System.out.println("PHARMACIST AFM REPOSITORY SEARCH = " + afm);
        if (afm == null) {
            return listAll();
        }

        return find("select ph from Pharmacist ph where ph.afm like :ph_afm" ,
                Parameters.with("ph_afm", afm + "%").map())
                .list();
    }
    //-------------------------------DIMITRIS----------------------------------------

    public List<Pharmacist> findByName(String lastName) {
        return em.createQuery("FROM Pharmacist WHERE lastName = :lastName", Pharmacist.class).setParameter("lastName", lastName).getResultList();
    }

    public List<Pharmacist> pharmasictfind() {
        return (List<Pharmacist>) find("select ph from Pharmacist ph where ph.email ='maria@pharmacist.com'").list();
    }

    public List<Pharmacist> findByLastName(String lastName){
        System.out.println("EMAIL findByEmail REPOSITORY = " + lastName);
        return list("lastName", lastName);
    }

    public void deleteRep(Integer id) {
        System.out.println("EMAIL SEARCE REPOSITORY = " + id);


        find("delete ph from Pharmacist ph where id = '"+ id+"'" );

    }

}
