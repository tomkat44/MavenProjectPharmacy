package com.example.app.resource;

import com.example.app.EopyyException;
import com.example.app.domain.ActiveSubstance;
import com.example.app.domain.Doctor;
import com.example.app.domain.Patient;
import com.example.app.domain.Pharmacist;
import com.example.app.persistence.DoctorRepository;
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
import java.util.Collections;
import java.util.List;

import static com.example.app.resource.Uri.DOCTOR;

@Path(DOCTOR)
@RequestScoped
public class DoctorResource {
    @Context
    UriInfo uriInfo;

    @Inject
    DoctorMapper doctorMapper;

    @Inject
    DoctorRepository doctorRepository;

    @GET
    @Path("/{doctorId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("doctorId") Integer doctorId){

        Doctor doc = doctorRepository.findById(doctorId);
        if (doc == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(doctorMapper.toRepresentation(doc)).build();
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<DoctorRepresentation> listAll() {
        return doctorMapper.toRepresentationList(doctorRepository.listAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addNewPDoctor(DoctorRepresentation dto){

        Doctor doctor = doctorMapper.toModel(dto);
        //έλεγχοι ακερααιότητας , πληρότητας και αποθήκευση

        //Αποθήκευση
        List<Doctor> d = doctorRepository.search(dto.afm);



        //έλεγχος  όοτι δεν θα υπάρξει είσοδος ιατρού με το ίδιο AFM
        if (d.size() == 0) {
            //doctorRepository.getEntityManager().merge(doctor);
            doctorRepository.persist(doctor);
        }
        else {return Response.status(404).build();}


        URI locationUri = uriInfo.getAbsolutePathBuilder()
                .path(doctor.getId().toString())
                .build();

        DoctorRepresentation savedDoc = doctorMapper.toRepresentation(doctor);
        return Response.created(locationUri).entity(savedDoc).build();

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<DoctorRepresentation> findByAfm(@QueryParam("doctorAfm") String doctorAfm) {
        return doctorMapper.toRepresentationList(doctorRepository.search(doctorAfm));
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateDoctor(@PathParam("id") Integer id, DoctorRepresentation representation) {


        if (!id.equals(representation.id)) {
            throw new RuntimeException();
        }

        Doctor updDoc = doctorMapper.toModel(representation);

        List<Doctor> d1 = doctorRepository.search(representation.afm);
        Doctor d2 = doctorRepository.findById(id);

        Doctor d11 = doctorRepository.findById(id);
        Doctor d22 = doctorRepository.findById(representation.id);

        //Ελέγχω ότι το άτομο του οποίου τα στοιχεία κοιτάω να αλλάξω υπάρχει και είναι ΜΟΝΑΔΙΚΟ
        if (d1.size() == 1 && d11.getAmka() == d22.getAmka() && d1.get(0).getAfm() == d2.getAfm()) {
            doctorRepository.getEntityManager().merge(updDoc);
        } else {
            System.out.println("CHANGE of AMKA or AFM is not permitted.");
            new EopyyException("CHANGE of AMKA or AFM is not permitted.");
        }

        return Response.noContent().build();
    }


    @DELETE
    @Path("{id:[0-9]+}")
    @Transactional
    public Response removeDoctor(@PathParam("id") Integer id){
        Doctor doctor = doctorRepository.findById(id);

        if (id == null){
            return Response.status(404).build();
        }
        doctorRepository.delete("id", doctor.getId());

        return Response.noContent().build();
    }

}
