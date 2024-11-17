package com.example.app.resource;


import com.example.app.EopyyException;
import com.example.app.domain.Doctor;
import com.example.app.domain.Drug;
import com.example.app.domain.Patient;
import com.example.app.domain.Pharmacist;
import com.example.app.persistence.DrugRepository;
import com.example.app.persistence.PatientRepository;
import com.example.app.representation.*;

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

import static com.example.app.resource.Uri.DRUG;
import static com.example.app.resource.Uri.PATIENT;

@Path(PATIENT)
@RequestScoped
public class PatientResource {

    @Context
    UriInfo uriInfo;

    @Inject
    PatientRepository patientRepository;

    @Inject
    PatientsMapper patientMapper;

    @GET
    @Path("/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response findById(@PathParam("patientId") Integer patientId) {
        Patient pat = patientRepository.findById(patientId);
        if (pat == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(patientMapper.toRepresentation(pat)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PatientRepresentation> findByAmka(@QueryParam("patientAmka") String patientAmka) {
        return patientMapper.toRepresentationList(patientRepository.search(patientAmka));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PatientRepresentation> listAll() {
        return patientMapper.toRepresentationList(patientRepository.listAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response submitPatient(PatientRepresentation pat){

        Patient entity = patientMapper.toModel(pat);

        List<Patient> p = patientRepository.search(pat.amka);

        if (p.size() == 0) {
            //patientRepository.getEntityManager().merge(entity);
            patientRepository.persist(entity);
        }
        else {return Response.status(404).build();}

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(Integer.toString(entity.getId()))
                .build();

        return Response
                .created(location)
                .entity(patientMapper.toRepresentation(entity))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePatient(@PathParam("id") Integer id, PatientRepresentation representation) {


        if (!id.equals(representation.id)) {
            throw new RuntimeException();
        }

        Patient updPat = patientMapper.toModel(representation);
        List<Patient> p = patientRepository.search(representation.amka);

        //Θα ελέγχει ότι ο υπάρχων ασθενής με αυτόν που πάει να μεταβληθεί έχουν το ίδιο AMKA
        Patient p1 = patientRepository.findById(id);
        Patient p2 = patientRepository.findById(representation.id);

        //Ελέγχει ότι η τροποποίηση θα πραγματοποιηθεί με το ίδιο AFM και θα είναι μοναδικό για να μην το τροποποιήσει κάποιος
        if (p.size() == 1 && p1.getAmka()==p2.getAmka()) {
            patientRepository.getEntityManager().merge(updPat);
        } else {
            System.out.println("CHANGE of AMKA  is not permitted.");
            new EopyyException("CHANGE of AMKA  is not permitted.");
        }

        return Response.noContent().build();
    }

    @DELETE
    @Path("{id:[0-9]+}")
    @Transactional
    public Response removePatient(@PathParam("id") Integer id){
        Patient patient = patientRepository.findById(id);

        if (id == null){
            return Response.status(404).build();
        }
        patientRepository.delete("id", patient.getId());

        return Response.noContent().build();
    }

}
