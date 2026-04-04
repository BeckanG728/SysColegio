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
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import javax.servlet.ServletException;
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
        
        response.setContentType("text/plain;charset=UTF-8");
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse;
        
        try (PrintWriter out = response.getWriter()) {
            String bodyCifrado = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            
            String bodyPlano = CifradoCesar.descifrar(bodyCifrado);
            
            AlumnoRequest object = mapper.readValue(bodyPlano, AlumnoRequest.class);
            
            AlumnoResponse resp = service.findByDNI(object.dni());
            
            if(resp == null || "error".equals(resp.resultado())){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND); // 401
                String mensaje = "Alumno no encontrado";
                ErrorResponse error = new ErrorResponse("error", mensaje);
                jsonResponse = mapper.writeValueAsString(error);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                jsonResponse = mapper.writeValueAsString(resp);
            }
            
            // Cifrar respuesta con César y envia al frontend
            out.print(CifradoCesar.cifrar(jsonResponse));
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try (PrintWriter out = response.getWriter()) {
                ErrorResponse error = new ErrorResponse("error", "Error procesando la solicitud");
                out.print(CifradoCesar.cifrar(mapper.writeValueAsString(error)));
            }

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
