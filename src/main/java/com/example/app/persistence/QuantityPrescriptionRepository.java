package com.example.app.persistence;


import com.example.app.domain.Pharmacist;
import com.example.app.domain.Prescription;
import com.example.app.domain.QuantityPrescription;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@RequestScoped
public class QuantityPrescriptionRepository implements PanacheRepositoryBase<QuantityPrescription, Integer> {

    @Inject
    EntityManager em;

    public List<QuantityPrescription> searchByPrescriptionId(int prescriptionId) {
        System.out.println("Prescription ID = " + prescriptionId);
        if (prescriptionId == -1) {
            return listAll();
        }

        return find("select qp from QuantityPrescription qp where qp.prescription like :preId" ,
                Parameters.with("preId", prescriptionId ).map())
                .list();
    }

    public List<QuantityPrescription> searchByPrescriptionId2(int prescriptionId){
        System.out.println("Prescription ID = " + prescriptionId);
        return list("prescription", prescriptionId);
    }
}
