package com.example.app.persistence;


import com.example.app.domain.Pharmacist;
import com.example.app.domain.Prescription;
import com.example.app.representation.PrescriptionRepresentation;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@RequestScoped
public class PrescriptionRepository implements PanacheRepositoryBase<Prescription, Integer> {

    @Inject
    EntityManager em;

    public Prescription searchPrescriptionThroughQP(Integer qp_id) {

//        if (qp_id == null) {
//            return listAll();
//        }
        System.out.println("QP for BUSTED= " + qp_id);

        return find("select p from Prescription p, QuantityPrescription qp " +
                        "where p.id = qp.prescription AND qp.id = :qpId" ,
                Parameters.with("qpId", qp_id).map())
                .firstResult();
    }

    public Prescription searchPrescriptionInQP(Integer qp_id) {

        System.out.println("QP for BUSTED= " + qp_id);

        return find("select qp.prescription from QuantityPrescription qp where qp.id = :qpId" ,
                Parameters.with("qpId", qp_id).map())
                .firstResult();
    }
    public List<Prescription> searchById(Integer p_id) {


        return find("select p from Prescription p where p.id like :pId" ,
                Parameters.with("pId", p_id).map())
                .list();
    }


    /*Για την κατευθυνόμενη συνταγογράφιση*/
    public List<Prescription> searchPrescriptionByDoctorPatient() {

        System.out.println("QP for BUSTED= ");

        return find("select p.doctorAMKA from Prescription p group by p.doctorAMKA" )
                .list();

//        return find("select p from Prescription p, QuantityPrescription qp " +
//                        "where p.id = qp.prescription group by p.doctorAMKA, p.patientAMKA" )
//                .list();

    }

    public List<Prescription> searchByPatientAMKA(String patientAMKA) {

        if (patientAMKA == null) {
            return listAll();
        }

        return find("select p from Prescription p where p.patientAMKA like :pAmka" ,
                Parameters.with("pAmka", patientAMKA).map())
                .list();
    }


}
