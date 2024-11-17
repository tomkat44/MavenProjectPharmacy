package com.example.app.resource;


import com.example.app.EopyyException;
import com.example.app.domain.Doctor;
import com.example.app.domain.Drug;
import com.example.app.domain.Pharmacist;
import com.example.app.persistence.DrugRepository;
import com.example.app.representation.ActiveSubstanceRepresentation;
import com.example.app.representation.DrugMapper;
import com.example.app.representation.DrugRepresentation;
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

import static com.example.app.resource.Uri.DRUG;



@Path(DRUG)
@RequestScoped
public class DrugResource {

    @Context
    UriInfo uriInfo;

    @Inject
    DrugRepository drugRepository;

    @Inject
    DrugMapper drugMapper;



    @GET
    @Path("/{drugId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("drugId") Integer drugId) {
        Drug d = drugRepository.findById(drugId);
        if (d == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(drugMapper.toRepresentation(d)).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<DrugRepresentation> searchDrugByName(@QueryParam("drugName") String drugName) {
        return drugMapper.toRepresentationList(drugRepository.search(drugName));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<DrugRepresentation> listAll() {
        return drugMapper.toRepresentationList(drugRepository.listAll());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response submitDrug(DrugRepresentation drugDto){

        Drug entity = drugMapper.toModel(drugDto);

        List<Drug> d = drugRepository.search(drugDto.drugName);

        if (d.size() == 0) {
            //drugRepository.getEntityManager().merge(entity);
            drugRepository.persist((entity));
        }
        else {return Response.status(404).build();}


        URI location = uriInfo.getAbsolutePathBuilder()
                .path(Integer.toString(entity.getId()))
                .build();


        return Response
                .created(location)
                .entity(drugMapper.toRepresentation(entity))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateDrug(@PathParam("id") Integer id, DrugRepresentation representation) {


        if (! id.equals(representation.id)) {
            throw new RuntimeException();
        }


        Drug drug = drugMapper.toModel(representation);

        Drug as = drugRepository.findById(id);

        //Ελέγχω ότι το drug που πάω να αλλάξω υπάρχει και είναι μοναδικό
        if(as !=null) {
            drugRepository.getEntityManager().merge(drug);
        }else{
            new EopyyException("Wrong Active Substance");
        }

        return Response.noContent().build();
    }


}
