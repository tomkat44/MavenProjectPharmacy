package com.example.app.persistence;

import com.example.app.domain.ActiveSubstance;
import com.example.app.domain.Authentication;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class AuthenticationRepository implements PanacheRepositoryBase<Authentication, Integer>{


}
