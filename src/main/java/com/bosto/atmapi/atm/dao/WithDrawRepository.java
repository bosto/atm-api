package com.bosto.atmapi.atm.dao;

import com.bosto.atmapi.atm.domain.WithDraw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WithDrawRepository extends CrudRepository<WithDraw, Long> {
    Page<WithDraw> findAll(Pageable pageable);

    Page<WithDraw> findAll(Specification specification, Pageable pageable);

    List<WithDraw> findAll(Specification specification);

    List<WithDraw> findWithDrawsByStatus(WithDraw.Status status);

    List<WithDraw> findAll();

}
