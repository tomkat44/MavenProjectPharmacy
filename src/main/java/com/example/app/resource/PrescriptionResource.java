package com.example.app.resource;

import com.example.app.EopyyException;
import com.example.app.domain.*;
import com.example.app.persistence.*;
import com.example.app.representation.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


import static com.example.app.resource.Uri.DOCTOR;
import static com.example.app.resource.Uri.PRESCRIPTION;

@Path(PRESCRIPTION)
@RequestScoped
public class PrescriptionResource {
    @Context
    UriInfo uriInfo;

    @Inject
    EntityManager em;

    @Inject
    PrescriptionMapper prescriptionMapper;

    @Inject
    PrescriptionRepository prescriptionRepository;

    @Inject
    QuantityPrescriptionMapper quantityPrescriptionMapper;
    @Inject
    QuantityPrescriptionRepository quantityPrescriptionRepository;

    @Inject
    PrescriptionExecutionMapper prescriptionExecutionMapper;

    @Inject
    PrescriptionExecutionRepository prescriptionExecutionRepository;

    @Inject
    QuantityExecutionRepository quantityExecutionRepository;

    @Inject
    QuantityExecutionMapper quantityExecutionMapper;

    @Inject
    DoctorRepository doctorRepository;

    @GET
    @Path("/{prescriptionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("prescriptionId") Integer prescriptionId){

        Prescription prescription = prescriptionRepository.findById(prescriptionId);
        if (prescription == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        System.out.println("PRESCRIPTION Representation GET= "+ prescriptionId);
        return Response.ok().entity(prescriptionMapper.toRepresentation(prescription)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PrescriptionRepresentation> searchByPatientsAMKA(@QueryParam("amka") String amka) {
        System.out.println("Patient AMKA  RESOURCE SEARCH= " + amka);
        return prescriptionMapper.toRepresentationList(prescriptionRepository.searchByPatientAMKA(amka));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PrescriptionRepresentation> listAll() {
        return prescriptionMapper.toRepresentationList(prescriptionRepository.listAll());
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addNewPrescription(PrescriptionRepresentation dto) {

        Prescription prescription = prescriptionMapper.toModel(dto);

        //Δεν θέλω να μπορεί να καταχωρηθεί κάτι που υπάρχει ήδη στην DB
        List<Prescription> pre = prescriptionRepository.searchById(dto.id);
        System.out.print("\n pre = prescriptionRepository.searchById(dto.id) = "+ dto.id + "\n");

        if(pre.size() == 0 ) {

            /*Τα 2 παρακάτω τα χρειάζομαι για να ελέγξω με την παρακάτω if ότι τα
             * φάρμακα που έγραψε ο φαρμακοποιός έινια τα ίδια με αυτά που έγραψε
             * ο γιατρός*/
            List<QuantityPrescription> qps = new ArrayList<>();
            for (QuantityPrescriptionRepresentation qp_rep : dto.quantityPrescriptions) {
                QuantityPrescription qp = quantityPrescriptionMapper.toModel(qp_rep);
                qps.add(qp);
                quantityPrescriptionRepository.persist(qp);
                qp.setPrescription(prescription);
            }
            List<QuantityExecution> qes = null;
            System.out.print("QuantityExecution qe = " + dto.quantityPrescriptions.size() + "\n\n");

            try {
                for (QuantityExecutionRepresentation qe_rep : dto.prescriptionExecution.quantityExecutions) {
                    QuantityExecution qe = quantityExecutionMapper.toModel(qe_rep);
                    qes.add(qe);

                    //quantityExecutionRepository.getEntityManager().merge(qe);
                    //prescription.getPrescriptionExecution()
                }
            } catch (NullPointerException e) {
                //throw new EopyyException();
                System.out.print("Caught NullPointerException");
            }


            //MONO ο γιατρός θα μπορεί να κάνει POST μία συνταγή και όχι κάποιςο άλλος
            Doctor doctor = doctorRepository.findById(dto.doctorToPrescription.id);
            System.out.print("\n doctor = doctorRepository.findById(dto.doctorToPrescription.id) = " + doctor.getId() + "\n");

            System.out.print("\n pre.size() == 0 && doctor != null = " + pre.size() + "\n");
            /*Με το qes == null ελέγχω ότι όταν δημιουργείται μία prescription τότε θα  πρέπει
             * να έχει το Quantity Execution κενό καθώς δεν γίνεται να είναι και γιατρός και φαρμακοποιός.
             * Ο γιατρός ανοίγει την συνταγή και ο φαρμακοποιός εκτελέι αυτή*/
            if(doctor != null) { //το θέλω απ έξω γιατί βγάζει θέμα με το UPDATE
                System.out.print("\n qes == null = " + qes + "\n");
                try {
                    if (qes == null) {
    //                   prescriptionRepository.getEntityManager().clear();
    //                   prescriptionRepository.getEntityManager().merge(prescription);

                        prescriptionRepository.persist(prescription);
    //                   prescriptionRepository.getEntityManager().persist(prescription);

                        System.out.print("\nprescriptionRepository.isPersistent(prescription); = " + prescriptionRepository.isPersistent(prescription));
                        System.out.print("\nprescriptionRepository.getEntityManager().merge(prescription) = " + prescription.getId());
                    }
                } catch (NullPointerException e) {
                    return Response.status(404).build();
                }
            }

       }else {
           return Response.status(404).build();
       }


        URI locationUri = uriInfo.getAbsolutePathBuilder()
                .path(Integer.toString(prescription.getId()))
                .build();

        PrescriptionRepresentation savedDoc = prescriptionMapper.toRepresentation(prescription);
        return Response.created(locationUri).entity(savedDoc).build();

    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePrescription(@PathParam("id") Integer id, PrescriptionRepresentation pre_rep) {
        Double summaryCost = 0.0;

        System.out.println("SUMMURY COST = "+ summaryCost);
        if (! id.equals(pre_rep.id)) {
            throw new RuntimeException();
        }

        Prescription prescription = prescriptionMapper.toModel(pre_rep);

        /*Φτιάχνα αυτό και το παρακάτω LIst με σκοπό να μπορέσψ παρακάτω να τα δώσω σαν ορίσματα
        * ώστε να εξάγω το SummaryCost και το FLAG για το αν μία συνταγή είναι PENDING, EXESUTED κλπ*/
        List<QuantityPrescription> qps = new ArrayList<>();
        for(QuantityPrescriptionRepresentation qp_rep : pre_rep.quantityPrescriptions) {
            QuantityPrescription qp = quantityPrescriptionMapper.toModel(qp_rep);
            qps.add(qp);
            System.out.println("quantityPrescriptionMapper.toModel(qp_rep) = "+ qp.getId());

        }

        /*Ενα από τα ΜΕΓΑΛΥΤΕΡΑ ΠΡΟΒΛΗΜΑΤΑ ήταν να μπορέσω έχωντας μία prescription στην οποία δεν έχει
        * πραγματοποιηθεί ακόμη Execution να μπορέσω να εντάξω την EXECUTION και ταυτόχρονα να εμφανίζει
        * και τα quantity Execution
        * Για να μπορέσει να γίενι αυτό θα πρέπει να είναι το QE = cascade.ALL καιώς και το PE cascadeALL
        * επιπρόσθετα για να δουλέψει ΔΕΝ πρέπει να φτιάξω το PE και μετά να βάλω μέσα του το SET<QE> διό΄τι
        * είναι SET ακι από την πλευρά του QE δεν θα δημιουργηθούν με αυτό τον τρόπο όλες οι QE που θα δείχνουν
        * προς το ίδιο PE.
        * ΠΡΕΠΕΙ να ενεργησω όποως παρακάτω. Δηλαδή να φτιάξω ή να εισάγω το PE.
        * Να το κάνω Persist ώστε να υπάρχει στην DB
        * Μετά να φτιάξω μια for που σε κάθε μία επανάληψη θα κάνει σε κάθε QE.set(PE) και με αυτόν τον τρόπο
        * δημιουργούναι τα απαιτούμενα QE για το PE.
        * ΤΕΛΟΣ εισάγω το PE μέσα στο PRESCRIPTION και κάνω merge(prescription)*/
        PrescriptionExecution pe = prescriptionExecutionMapper.toModel(pre_rep.prescriptionExecution);
        prescriptionExecutionRepository.persist(pe);

        /*Το χειάζομαι και για το SummaryCost kai γαι το FLAG*/
        List<QuantityExecution> qes = new ArrayList<>();
        for(QuantityExecutionRepresentation qe_rep : pre_rep.prescriptionExecution.quantityExecutions){
            QuantityExecution qe = quantityExecutionMapper.toModel(qe_rep);
            qes.add(qe); //το θέλω γαι το summary COST

            qe.setPrescriptionExecution(pe);
            System.out.println("quantityExecutionMapper.toModel(qp_rep) = "+ qe.getId());
            quantityExecutionRepository.persist(qe);

       }

        //Κάνω το Quantity Execution SET και μετά το εισάγω στο Prescription Execution αλλά δεν δουλέυει
//        prescriptionExecutionRepository.persist(pe);
//        System.out.println("PrescriptionExecution ID = "+ pe.getId());
//        Set<QuantityExecution> qeSet = new HashSet<QuantityExecution>(qes);
//        qeSet.addAll(qes);
//        pe.setQuantityExecutions(qeSet);


        prescription.setPrescriptionExecution(pe);


        //ΥΠολογισμός του Summary Cost
        summaryCost = prescription.getPrescriptionExecution().getSummaryCost(qes);
        prescription.getPrescriptionExecution().setSummaryCost(summaryCost);
        System.out.println("SUMMURY COST = "+ summaryCost);

        //Υπολογισμός του FLAG EXECUTED - PARTIALLY
        try {
           //prescription.getPrescriptionExecution().setExecutionFlag(qes, qps);
           prescription.setExecutionFlag2();
            System.out.println("EXECUTION FLAG = "+prescription.getPrescriptionExecution().getExecutionFlag());

        } catch (DomainException e) {
            throw new RuntimeException(e);
        }


        /*Θέλω να ελέγξω ότι δεν υπάρχει περίπτωση να γίνει εκτέλεση και τα φάρμακα που έδωσε ο γιατρός
        * να μην είαυτά που πρέπει. */
        try{
            if ( prescription.checkPharmacistGiveCorrectDrugs(qps, qes) && prescription.getPrescriptionExecution().getExecutionFlag() != executionPrescriptionFlag.CANCELED) {
                prescriptionRepository.getEntityManager().merge(prescription);


            }
        } catch (NullPointerException e) {
            return Response.status(404).build();
        }

        //ήταν μόνο του από έξω χωρίς δικλείδες ασφάλειας
        //prescriptionRepository.getEntityManager().merge(prescription);

        return Response.noContent().build();
    }

    /*Για την ακρίβεια αυτή η DELETE δεν διαγράφει αλλά ακυρώνει μια συνταγή ΜΟΝΟ αν δεν είναι EXECUTED*/
    @DELETE
    @Path("{id:[0-9]+}")
    @Transactional
    public Response cancelPrescription(@PathParam("id") Integer id){
        Prescription prescription = prescriptionRepository.findById(id);

        if (prescription == null){
            return Response.status(404).build();
        } else if(prescription.getPrescriptionExecution()==null){
            PrescriptionExecution pe = new PrescriptionExecution();
            pe.setExecutionFlag(executionPrescriptionFlag.CANCELED);
            pe.setExecutionDate(LocalDate.now().toString());
            prescription.setPrescriptionExecution(pe);

            prescriptionRepository.getEntityManager().merge(prescription);
        } else {
            return Response.status(401).build();
        }



        return Response.noContent().build();
    }
    
}
