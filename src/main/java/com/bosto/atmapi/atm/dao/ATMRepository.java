package com.bosto.atmapi.atm.dao;

import com.bosto.atmapi.atm.domain.ATM;
import org.springframework.data.repository.CrudRepository;

public interface ATMRepository extends CrudRepository<ATM, String> {
    ATM findByExternalId(String externalId);
    boolean existsATMByExternalId(String externalId);
}
