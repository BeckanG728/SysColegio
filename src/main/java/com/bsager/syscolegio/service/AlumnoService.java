/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.bsager.syscolegio.service;

import com.bsager.syscolegio.controller.AlumnoJpaController;
import com.bsager.syscolegio.dto.response.AlumnoResponse;
import com.bsager.syscolegio.util.JpaUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

/**
 * @author
 */
public class AlumnoService {

    private final AlumnoJpaController alumnoCtrl;
    private final ObjectMapper mapper;
    
    public AlumnoService(){
        this.alumnoCtrl = new AlumnoJpaController(JpaUtil.getFactory());
        this.mapper = new ObjectMapper();
    }
    
    public AlumnoResponse findByDNI(String dni) {
        EntityManager em = alumnoCtrl.getEntityManager();
        try {
            StoredProcedureQuery sp = em.createStoredProcedureQuery("sp_buscar_alumno_dni")
                    .registerStoredProcedureParameter("_dni", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_resultado", String.class, ParameterMode.OUT)
                    .setParameter("_dni", dni);
            
            sp.execute();
            
            Object object = sp.getOutputParameterValue("_resultado");
            
            return mapper.readValue(object.toString(), AlumnoResponse.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AlumnoService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return null;
    }

}
