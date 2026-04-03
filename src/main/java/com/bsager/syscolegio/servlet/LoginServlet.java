
package com.bsager.syscolegio.servlet;

import com.bsager.syscolegio.cipher.CifradoCesar;
import com.bsager.syscolegio.dto.request.LoginRequest;
import com.bsager.syscolegio.dto.response.ErrorResponse;
import com.bsager.syscolegio.dto.response.LoginResponse;
import com.bsager.syscolegio.service.UsuarioService;
import com.bsager.syscolegio.util.Base64Util;
import com.bsager.syscolegio.util.HashUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author chila
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    
    private final UsuarioService service = new UsuarioService();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        try (PrintWriter out = response.getWriter()) {
            
            // 1. Leer el JSON del cuerpo { username, password }
            LoginRequest objeto = mapper.readValue(request.getInputStream(), LoginRequest.class);
 
            // 2. Decodificar: Base64 -> César -> texto plano
            String user = CifradoCesar.descifrar(Base64Util.decode(objeto.username()));
            String pass = CifradoCesar.descifrar(Base64Util.decode(objeto.password()));
 
            // 3. Hashear la contraseña en SHA-256 (el SP compara con el hash)
            String passHash = HashUtil.sha256(pass);
 
            // 4. Llamar al servicio
            LoginResponse resp = service.login(user, passHash);
 
            String jsonRespuesta;
 
            if (resp == null || "error".equals(resp.resultado())) {
                // LoginRequest fallido
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                String mensaje = (resp != null) ? "Credenciales incorrectas" : "Error interno";
                ErrorResponse error = new ErrorResponse("error", mensaje);
                jsonRespuesta = mapper.writeValueAsString(error);
            } else {
                // LoginRequest exitoso -> crear sesión
                HttpSession sesion = request.getSession(true);
                sesion.setAttribute("codiUsua", resp.codiUsua());
                sesion.setMaxInactiveInterval(30 * 60); // 30 minutos
 
                response.setStatus(HttpServletResponse.SC_OK); // 200
                jsonRespuesta = mapper.writeValueAsString(resp);
            }
 
            // 5. Cifrar respuesta con César y enviar
            out.print(CifradoCesar.cifrar(jsonRespuesta));
 
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
