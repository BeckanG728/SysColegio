/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bsager.syscolegio.servlet;

import com.bsager.syscolegio.cipher.CifradoCesar;
import com.bsager.syscolegio.dto.request.AlumnoRegisterRequest;
import com.bsager.syscolegio.dto.response.AlumnoRegisterResponse;
import com.bsager.syscolegio.dto.response.ErrorResponse;
import com.bsager.syscolegio.service.AlumnoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author chila
 */
@WebServlet(name = "RegistrarAlumnoServlet", urlPatterns = {"/app/alumno/registrar"})
public class RegistrarAlumnoServlet extends HttpServlet {
    
    private final AlumnoService service = new AlumnoService();
    private final Logger LOG = Logger.getLogger(RegistrarAlumnoServlet.class.getName());
    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/octet-stream");
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            byte [] raw = request.getInputStream().readAllBytes();
            
            String jsonPlano = CifradoCesar.decrypt(raw);
            
            LOG.info(String.format("[REQUEST] data: %s - encrypted: %s", jsonPlano, Arrays.toString(raw)));
            
            AlumnoRegisterRequest req = mapper.readValue(jsonPlano, AlumnoRegisterRequest.class);
            
            AlumnoRegisterResponse resp = service.register(req);
            
            String jsonResponse;
            
            if(resp == null || "error".equals(resp.resultado())){
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                ErrorResponse error = new ErrorResponse("error", "DNI ya registrado");
                jsonResponse = mapper.writeValueAsString(error);
            } else {
                response.setStatus(HttpServletResponse.SC_CREATED);
                jsonResponse = mapper.writeValueAsString(resp);
            }
            
            byte [] encrypted = CifradoCesar.encrypt(jsonResponse);
            LOG.info(String.format("[RESPONSE] data: %s - encrypted: %s", jsonResponse, Arrays.toString(encrypted)));
            
            ServletOutputStream out = response.getOutputStream();
            out.write(encrypted);
            out.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ErrorResponse error = new ErrorResponse("error", "Error procesando la solicitud");
            String jsonError = mapper.writeValueAsString(error);
            byte [] dataEncrypted = CifradoCesar.encrypt(jsonError);
            
            ServletOutputStream out = response.getOutputStream();
            out.write(dataEncrypted);
            out.flush();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
