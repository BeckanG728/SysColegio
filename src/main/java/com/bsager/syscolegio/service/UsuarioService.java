package com.bsager.syscolegio.service;

import com.bsager.syscolegio.controller.UsuarioJpaController;
import com.bsager.syscolegio.dto.response.LoginResponse;
import com.bsager.syscolegio.util.JpaUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

public class UsuarioService {

    private final UsuarioJpaController usuarioCtrl;
    private final ObjectMapper mapper;

    public UsuarioService() {
        this.usuarioCtrl = new UsuarioJpaController(JpaUtil.getFactory());
        this.mapper = new ObjectMapper();
    }

    public LoginResponse login(String user, String password) {
        EntityManager em = usuarioCtrl.getEntityManager();
        try {
            StoredProcedureQuery sp = em.createStoredProcedureQuery("sp_login_usuario")
                    .registerStoredProcedureParameter("_login", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_pass", String.class, ParameterMode.IN)
                    .registerStoredProcedureParameter("_resultado", String.class, ParameterMode.OUT)
                    .setParameter("_login", user)
                    .setParameter("_pass", password);

            sp.execute();
            Object objecto = sp.getOutputParameterValue("_resultado");
            
            return mapper.readValue(objecto.toString(), LoginResponse.class);
            
        } catch (JsonProcessingException ex) {
            Logger.getLogger(UsuarioService.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            em.close();
        }
        return null;
    }
}
