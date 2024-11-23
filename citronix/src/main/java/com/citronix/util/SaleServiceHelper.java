package com.citronix.util;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.citronix.model.Sale;

@Service
public class SaleServiceHelper {
    @Autowired
    private EntityManager entityManager;

    @Transactional
    public void refreshEntity(Sale sale) {
        entityManager.refresh(sale);
    }
}