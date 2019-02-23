package com.bosto.atmapi.atm.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Data
@Entity
public class ATM {
    @Id
    @SequenceGenerator(name = "atm_generator", sequenceName = "atm_seq")
    @GeneratedValue(generator = "atm_generator")
    @JsonIgnore
    Long id;
    String externalId;
    String bankName;
    String bankCode;
    String address;

}
