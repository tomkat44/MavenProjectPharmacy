package com.example.app.resource;

import com.example.app.EopyyException;
import com.example.app.domain.ActiveSubstance;
import com.example.app.domain.Authentication;
import com.example.app.domain.Drug;
import com.example.app.domain.ToHexString;
import com.example.app.persistence.ActiveSubstanceRepository;
import com.example.app.persistence.AuthenticationRepository;
import com.example.app.representation.ActiveSubstanceMapper;
import com.example.app.representation.ActiveSubstanceRepresentation;
import com.example.app.representation.DrugRepresentation;
import jdk.jfr.ContentType;

import static com.example.app.domain.ToHexString.getSHA;
import static com.example.app.resource.Uri.SUBSTANCE;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


@Path(SUBSTANCE)
@RequestScoped
public class ActiveSubstanceResource {

    @Context
    UriInfo uriInfo;

    @Inject
    ActiveSubstanceRepository activeSubstanceRepository;

    @Inject
    ActiveSubstanceMapper activeSubstanceMapper;

    @Inject
    AuthenticationRepository authenticationRepository;


    @GET
    @Path("/{substanceId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("substanceId") Integer substanceId) {
        ActiveSubstance as = activeSubstanceRepository.findById(substanceId);
        if (as == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }


        return Response.ok().entity(activeSubstanceMapper.toRepresentation(as)).build();
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<ActiveSubstanceRepresentation> searchActiveSubstanceByName(@QueryParam("name") String substanceName) {

        return activeSubstanceMapper.toRepresentationList(activeSubstanceRepository.search(substanceName));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<ActiveSubstanceRepresentation> listAll() {
        return activeSubstanceMapper.toRepresentationList(activeSubstanceRepository.listAll());
    }


    /*Στο παρακάτω παράδειγμα κάνω έναν κύκλο Δηλαδή:
     * Μου στέλνουν μέσω στο Representation ένα αντικείμενο,
     * το κάνω Model δηλαδή το μετατρέπω σε domai Logic ώστε να μπορώ να το διαβάσω
     * το κάνω PERSIST για να αποθηκευθεί στην DB
     * έπειτα παίρνω το URL του
     * το αποθηκεύω σε μία μεταβλητή ώστε
     * να το κάνω RETURN  και να το εμφανίσω πίσω στον πελάτη*/
    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @Transactional
    public Response addNewActiveSubstance(ActiveSubstanceRepresentation dto) {

        ActiveSubstance entity = activeSubstanceMapper.toModel(dto);

        List<ActiveSubstance> asName = activeSubstanceRepository.search(dto.substanceName);

        //Ελέγχω ότι δεν υπάρχει κάτι με το ίδιο όνομα δραστικής ουσίας
        if ( asName.size() == 0){
            //activeSubstanceRepository.getEntityManager().merge(entity);
            activeSubstanceRepository.persist((entity));

        } else {
            return Response.status(404).build();
            //throw new EopyyException("Active Substance is Already Exist.");
        }

        URI locationUri = uriInfo.getAbsolutePathBuilder()
                .path(Integer.toString(entity.getId()))
                .build();

        ActiveSubstanceRepresentation savedActiveSubstance = activeSubstanceMapper.toRepresentation(entity);
        return Response.created(locationUri).entity(savedActiveSubstance).build();
    }


    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateActiveSubstance(@PathParam("id") Integer id, ActiveSubstanceRepresentation representation) {

        /*Ελέγχει ότι το id του URL είναι το ίδιο με το id του AS Representation
        * καθώς και ότι παρακάτω ότι το id του AS είναι υπαρκότ στην DB*/

        if (! id.equals(representation.id)) {
            throw new RuntimeException();
        }

        ActiveSubstance activeSubstance = activeSubstanceMapper.toModel(representation);

        ActiveSubstance as = activeSubstanceRepository.findById(id);

        //Ελέγχω ότι το activeSubstance που πάω να αλλάξω υπάρχει και είναι μοναδικό
        if ( as != null){
            activeSubstanceRepository.getEntityManager().merge(activeSubstance);
        } else {
            new EopyyException("Wrong Active Substance");
        }

        return Response.noContent().build();
    }

    @DELETE
    @Path("{id:[0-9]+}")
    @Transactional
    public Response removeActiveSubstance(@PathParam("id") Integer substanceId, String password){
        ActiveSubstance as = activeSubstanceRepository.findById(substanceId);
        Authentication a = authenticationRepository.findById(9999);

        String hash = null;
        try {
            hash = ToHexString.toHexString(getSHA(password));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }


        if (as == null){
            return Response.status(404).build();
        }
        if(hash.equals(a.getPassword())){
            activeSubstanceRepository.delete("id", as.getId());
        } else {
            System.out.println("PERMISSION DENIED wrong password");
            new EopyyException("PERMISSION DENIED wrong password");
        }


        return Response.noContent().build();
    }

}
