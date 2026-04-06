
package com.bsager.syscolegio.service;

import com.bsager.syscolegio.dto.response.AulaResponse;
import com.bsager.syscolegio.controller.AulaJpaController;
import com.bsager.syscolegio.model.Aula;
import com.bsager.syscolegio.util.JpaUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class AulaService{
    
    private final AulaJpaController aulaCtrl;
    private final ObjectMapper mapper;
    
    public AulaService(){
        this.aulaCtrl = new AulaJpaController(JpaUtil.getFactory());
        this.mapper = new ObjectMapper();
    }
}
