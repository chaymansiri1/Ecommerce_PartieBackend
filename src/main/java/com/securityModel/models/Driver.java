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

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity

@Table(name = "drivers")
public class Driver extends User {

    private String adress;

    @OneToMany(mappedBy = "driver")
    @JsonIgnoreProperties("driver")
    private Collection<Order>order;
}
