package com.example.app.resource;


import com.example.app.domain.ActiveSubstance;
import com.example.app.domain.Pharmacist;
import com.example.app.persistence.PharmacistRepository;
import com.example.app.persistence.PrescriptionRepository;
import com.example.app.representation.DoctorRepresentation;
import com.example.app.representation.PharmacistMapper;
import com.example.app.representation.PharmacistRepresentation;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

import static com.example.app.resource.Uri.PHARMACIST;


@Path(PHARMACIST)
@RequestScoped
public class PharmacistResource {

    @Context
    UriInfo uriInfo;

    @Inject
    PharmacistMapper pharmacistMapper;

    @Inject
    PharmacistRepository pharmacistRepository;


    @GET
    @Path("/{pharmacistId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("pharmacistId") Integer pharmacistId) {
        // System.out.println("EMAIL RESOURCE ID= " + pharmacistId);
        Pharmacist ph = pharmacistRepository.findById(pharmacistId);
        if (ph == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(pharmacistMapper.toRepresentation(ph)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PharmacistRepresentation> searchByAfm(@QueryParam("afm") String afm) {
        System.out.println("PHARMACIST AFM RESOURCE SEARCH= " + afm);
        return pharmacistMapper.toRepresentationList(pharmacistRepository.searchByAfm(afm));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PharmacistRepresentation> listAll() {
        return pharmacistMapper.toRepresentationList(pharmacistRepository.listAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response submitPharmacist(PharmacistRepresentation pharmacistDto){

        Pharmacist entity = pharmacistMapper.toModel(pharmacistDto);


        List<Pharmacist> ph_afm = pharmacistRepository.searchByAfm(pharmacistDto.afm);

        if (ph_afm.size() == 0) {
            //pharmacistRepository.getEntityManager().merge(entity);
            pharmacistRepository.persist(entity);
        }
        else {return Response.status(404).build();}


        URI location = uriInfo.getAbsolutePathBuilder()
                .path(Integer.toString(entity.getId()))
                .build();
        return Response
                .created(location)
                .entity(pharmacistMapper.toRepresentation(entity))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePharmacist(@PathParam("id") Integer id, PharmacistRepresentation representation) {
        System.out.println("PUT Pharmacist id "+ id + " \t representation =  "+ representation.id);

        if (! id.equals(representation.id)) {
            throw new RuntimeException();
        }

        Pharmacist ph_Id = pharmacistRepository.findById(id);

        /*Σε περίπτωση που πάει να γίνει POST ή UPDATE του Prescription τότε θα
        * πρέπει να αποφύγω την διπλή  καταχώριση του φαρμακοποιού
        * Για να πετύχω την περίπτωση ελέγχω με την παραπάνω γραμμή ότι το id
        * που έρχεται είναι του φαρμακοποιού και αν είναι που σημαίνει ότι ο χρήστης
        * θέλει να κάνει Update Φαρμακοποιό τότε κάνω το UPDATE,
        * Διαφορετικά αν το UPDATE έρχεται από PrescriptionExecution τότε δεν θα
        * κάνει τίποτα*/
        if (ph_Id != null) {
            Pharmacist pharmacist = pharmacistMapper.toModel(representation);
            pharmacistRepository.getEntityManager().merge(pharmacist);
        }
        return Response.noContent().build();
    }


    @DELETE
    @Path("{id:[0-9]+}")
    @Transactional
    public Response removePharmacist(@PathParam("id") Integer id){
        Pharmacist ph = pharmacistRepository.find("id", id).firstResult();

        System.out.println("REMOVE Active Sybstance = "+ id);
        if (ph == null){
            return Response.status(404).build();
        }
        //pharmacistRepository.deleteRep(id);
        pharmacistRepository.delete("id", id);
        //pharmacistRepository.getEntityManager().remove(id);
        System.out.println("REMOVE Active Sybstance = "+ id);
        return Response.noContent().build();
    }






}
