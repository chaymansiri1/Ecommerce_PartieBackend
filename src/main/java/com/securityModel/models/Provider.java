package com.securityModel.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Entity
@Table(name = "providers")
@Setter
@Getter

@AllArgsConstructor
public class Provider extends User{

    private String matricule;
    private String company;

    @OneToMany(mappedBy = "provider")
    @JsonIgnoreProperties("provider")
    private Collection<Product> product;

    public Provider(String username, String email, String password, String matricule,  String company) {
        super(username, email, password);
        this.matricule = matricule;

        this.company = company;
    }
    public Provider() {

    }


}
