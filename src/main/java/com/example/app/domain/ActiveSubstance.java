package com.example.app.domain;

import com.example.app.util.SystemDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "activeSubstances")
public class ActiveSubstance {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name="substanceName", length=200, nullable=false)
    private String name;

    @Column(name="expectedQuantity", length=10, nullable=false)
    private int quantity;


    public ActiveSubstance(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }
    public ActiveSubstance(){
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActiveSubstance)) return false;
        ActiveSubstance that = (ActiveSubstance) o;
        return quantity == that.quantity && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, quantity);
    }
}
