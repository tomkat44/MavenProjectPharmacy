package com.example.app.persistence;


import com.example.app.domain.Pharmacist;
import com.example.app.domain.Prescription;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@RequestScoped
public class PrescriptionRepositoryBusted implements PanacheRepositoryBase<Prescription, Integer> {

    @Inject
    EntityManager em;

    public List<Prescription> searchPrescriptionThroughQP2(Integer qp_id) {
        System.out.println("QP for BUSTED= " + qp_id);
        if (qp_id == null) {
            return listAll();
        }

        return find("select p from Prescription p inner join QuantityPrescription qp " +
                        "on p.id = qp.prescription where qp.id = :qpId",
                Parameters.with("qpId", qp_id).map())
                .firstResult();
    }

    public List<Integer> searchPrescriptionInQP2(Integer qp_id) {

        System.out.println("QP for BUSTED= " + qp_id);

        return find("select qp.prescription_id from QuantityPrescription qp where qp.prescription = :qpId" ,
                Parameters.with("qpId", qp_id).map())
                .firstResult();
    }

    public List<Prescription> searchById(Integer p_id) {

        if (p_id == null) {
            return listAll();
        }

        return find("select p from Prescription p where p.id like :pId" ,
                Parameters.with("pId", p_id).map())
                .firstResult();
    }

}
