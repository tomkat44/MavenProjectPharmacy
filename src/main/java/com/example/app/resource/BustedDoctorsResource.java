package com.example.app.resource;

import com.example.app.domain.Prescription;
import com.example.app.domain.QuantityPrescription;
import com.example.app.persistence.PrescriptionRepository;
import com.example.app.persistence.PrescriptionRepositoryBusted;
import com.example.app.persistence.QuantityPrescriptionRepository;
import com.example.app.representation.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

import static com.example.app.resource.Uri.BUSTEDDOCTORS;



@Path(BUSTEDDOCTORS)
@RequestScoped
public class BustedDoctorsResource {

    @Context
    UriInfo uriInfo;

    @Inject
    QuantityPrescriptionRepository quantityPrescriptionRepository;

    @Inject
    QuantityPrescriptionMapper quantityPrescriptionMapper;

    @Inject
    PrescriptionMapperBusted prescriptionMapperBusted;

    @Inject
    PrescriptionRepositoryBusted prescriptionRepositoryBusted;

    @Inject
    PrescriptionMapper prescriptionMapper;

    @Inject
    PrescriptionRepository prescriptionRepository;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public  List<PrescriptionRepresentation> list() {

        List<Prescription> prescriptions = new ArrayList<Prescription>();
        List<Prescription> prescriptions2 = new ArrayList<Prescription>();


        System.out.println("quantityPrescriptionRepository.listAll()).size() = "+ quantityPrescriptionRepository.listAll().size());
        int i=0;
        //for (int i=0; i<quantityPrescriptionMapper.toRepresentationList(quantityPrescriptionRepository.listAll()).size(); i++){
        for(QuantityPrescriptionRepresentation qp_rep : quantityPrescriptionMapper.toRepresentationList(quantityPrescriptionRepository.listAll())) {
            System.out.println("quantityPrescriptionRepository.listAll()).size() = " + i);
            //QuantityPrescriptionRepresentation qp_rep = (quantityPrescriptionMapper.toRepresentationList(quantityPrescriptionRepository.listAll()).get(i));

            QuantityPrescription qp = (quantityPrescriptionMapper.toModel(qp_rep));
            System.out.println("prescriptionRepositoryBusted.searchPrescriptionThroughQP 2");


            System.out.println("prescriptionRepositoryBusted.searchPrescriptionThroughQP 3 ");
            Integer pre = prescriptionRepository.searchPrescriptionInQP(qp.getId()).getId();
            System.out.println("prescriptionRepositoryBusted.searchPrescriptionThroughQP 4 " + pre);
            prescriptions.add(prescriptionRepository.findById(pre));
        }


            Integer pre_check = 0;
            for(Prescription p : prescriptions){
                Integer quantity = 0; //Κρατάει την ποσότητα των φαρμάκων του QP για κάθε ένα για να τα προσθέτει
                for(QuantityPrescription q_p : p.getQuantityPrescriptions()){
                    quantity = q_p.getQuantityPrescription();

                    for(Prescription p2: prescriptions) {
                        if (p2.getDoctorAMKA() == p.getDoctorAMKA() && p2.getPatientAMKA() == p.getPatientAMKA() && p.getId() != p2.getId()
                        && p.getCreationDate().substring(5,7).equals(p2.getCreationDate().substring(5,7))) {
                            for (QuantityPrescription q_p2 : p2.getQuantityPrescriptions()) {
                                if (q_p.getDrug().getId() == q_p2.getDrug().getId() && q_p.getId() != q_p2.getId()) {
                                    quantity = quantity + q_p2.getQuantityPrescription();
                                }
                            }
                        }
                    }

                    if(!q_p.isInsideLimit2(quantity)){
                        System.out.println("prescriptionRepositoryBusted.searchPrescriptionThroughQP 5 " );
                        Integer pre2 = prescriptionRepository.searchPrescriptionInQP(q_p.getId()).getId();

                        System.out.println("prescriptionRepositoryBusted.searchPrescriptionThroughQP 6 " + p);
                        if(pre2 != pre_check){
                            prescriptions2.add(prescriptionRepository.findById(pre2));
                        }
                        pre_check = pre2;
                    }
                }
            }

        return prescriptionMapper.toRepresentationList(prescriptions2);
    }
}
