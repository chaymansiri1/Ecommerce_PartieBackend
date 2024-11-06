package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Entity
@Setter
@Getter
@Table(name = "customers")
public class Customer extends User{


    private String localisation;

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties("customer")
    private Collection<Order> order;

    public Customer() {

    }
    public Customer(String username, String email, String password, String localisation) {
        super(username, email, password);
        this.localisation = localisation;

    }
    public String getLocalisation() {
        return localisation;
    }
    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }



  }
