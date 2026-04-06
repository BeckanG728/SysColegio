/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.bsager.syscolegio.servlet;

import com.bsager.syscolegio.cipher.CifradoCesar;
import com.bsager.syscolegio.dto.request.AlumnoRequest;
import com.bsager.syscolegio.dto.response.AlumnoResponse;
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
@WebServlet(name = "BuscarAlumnoServlet", urlPatterns = {"/app/alumno/buscar"})
public class BuscarAlumnoServlet extends HttpServlet {

    
    private final AlumnoService service = new AlumnoService();
    private static final Logger LOG = Logger.getLogger(BuscarAlumnoServlet.class.getName());
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String ip = request.getRemoteAddr();
        String metodo = request.getMethod();
        LOG.info(String.format("[REQUEST] %s %s desde %s", metodo, request.getRequestURI(), ip));
        
        response.setContentType("application/octet-stream");
        ObjectMapper mapper = new ObjectMapper();
        
        
        try {
            
            // Recuperar bytes encriptados
            byte [] raw = request.getInputStream().readAllBytes();
            
            // Desencripta y transforma a json plano
            String jsonPlano = CifradoCesar.decrypt(raw);
            LOG.info(String.format("[PROCESS] encrypted: %s - data: %s", Arrays.toString(raw), jsonPlano));
            
            // Mapea a POJO
            AlumnoRequest object = mapper.readValue(jsonPlano, AlumnoRequest.class);
            
            AlumnoResponse resp = service.findByDNI(object.dni());
            
            
            String jsonResponse;
            if(resp == null || "error".equals(resp.resultado())){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 401
                ErrorResponse error = new ErrorResponse("error", "Alumno no encontrado");
                jsonResponse = mapper.writeValueAsString(error);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse = mapper.writeValueAsString(resp);
            }
            
            
            ServletOutputStream out = response.getOutputStream();
            byte [] dataEncrypted = CifradoCesar.encrypt(jsonResponse);
            out.write(dataEncrypted);
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
