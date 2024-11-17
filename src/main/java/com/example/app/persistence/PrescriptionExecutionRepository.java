package com.example.app.persistence;


import com.example.app.domain.PrescriptionExecution;
import com.example.app.domain.QuantityPrescription;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@RequestScoped
public class PrescriptionExecutionRepository implements PanacheRepositoryBase<PrescriptionExecution, Integer> {


}
