
package com.bsager.syscolegio.service;

import com.bsager.syscolegio.controller.AlumnoJpaController;
import com.bsager.syscolegio.dto.request.AlumnoRegisterRequest;
import com.bsager.syscolegio.dto.response.AlumnoRegisterResponse;
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
    
    public AlumnoRegisterResponse register(AlumnoRegisterRequest request){
        EntityManager em = alumnoCtrl.getEntityManager();
        try {
            StoredProcedureQuery sp = em.createStoredProcedureQuery("sp_registrar_alumno")
                    .registerStoredProcedureParameter("_dni", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_appa", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_apma", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_nomb", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_corr", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_cel", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_sexo", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_resultado", String.class, ParameterMode.OUT)
                    .setParameter("_dni", request.dni())
                    .setParameter("_appa", request.appa())
                    .setParameter("_apma", request.apma())
                    .setParameter("_nomb", request.nomb())
                    .setParameter("_corr", request.corr())
                    .setParameter("_cel", request.cel())
                    .setParameter("_sexo", request.sexo());
            
            sp.execute();
            
            Object object = sp.getOutputParameterValue("_resultado");
            
            return mapper.readValue(object.toString(), AlumnoRegisterResponse.class);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(AlumnoService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return null;
    }
    

}
