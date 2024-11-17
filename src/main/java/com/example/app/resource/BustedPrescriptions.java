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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

import static com.example.app.resource.Uri.BUSTEDPRESCRIPTIONS;



@Path(BUSTEDPRESCRIPTIONS)
@RequestScoped
public class BustedPrescriptions {

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
        List<Prescription> prescriptions3 = new ArrayList<Prescription>();



        System.out.println("quantityPrescriptionRepository.listAll()).size() = "+ quantityPrescriptionRepository.listAll().size());
        int i=0;
        //for (int i=0; i<quantityPrescriptionMapper.toRepresentationList(quantityPrescriptionRepository.listAll()).size(); i++){
        for(QuantityPrescriptionRepresentation qp_rep : quantityPrescriptionMapper.toRepresentationList(quantityPrescriptionRepository.listAll())) {

            QuantityPrescription qp = (quantityPrescriptionMapper.toModel(qp_rep));

            Integer pre = prescriptionRepository.searchPrescriptionInQP(qp.getId()).getId();

            prescriptions.add(prescriptionRepository.findById(pre));
        }

            Integer pre_check = 0;
            for(Prescription p : prescriptions){
                Integer quantity = 0;
                for(QuantityPrescription q_p : p.getQuantityPrescriptions()){
                    quantity = q_p.getQuantityPrescription();

                    if(!q_p.isInsideLimit2(quantity)){
                        Integer pre2 = prescriptionRepository.searchPrescriptionInQP(q_p.getId()).getId();

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
