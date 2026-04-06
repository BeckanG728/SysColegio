package com.bsager.syscolegio.servlet;

import com.bsager.syscolegio.cipher.CifradoCesar;
import com.bsager.syscolegio.dto.request.LoginRequest;
import com.bsager.syscolegio.dto.response.ErrorResponse;
import com.bsager.syscolegio.dto.response.LoginResponse;
import com.bsager.syscolegio.service.UsuarioService;
import com.bsager.syscolegio.util.HashUtil;
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
import javax.servlet.http.HttpSession;

/**
 *
 * @author chila
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final UsuarioService service = new UsuarioService();
    private static final Logger LOG = Logger.getLogger(LoginServlet.class.getName());

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/octet-stream");

        String ip = request.getRemoteAddr();
        String metodo = request.getMethod();
        LOG.info(String.format("[REQUEST] %s %s desde %s", metodo, request.getRequestURI(), ip));
        try {

            // 1. Leer bytes del request
            byte[] raw = request.getInputStream().readAllBytes();

            // 2. Descifrar
            String decrypted = CifradoCesar.decrypt(raw);
            
            LOG.info(String.format("[PROCESS] data: %s - encrypted: %s", Arrays.toString(raw), decrypted));

            // 3. Parsear JSON
            LoginRequest objeto = mapper.readValue(decrypted, LoginRequest.class);
            String user = objeto.username();
            String pass = objeto.password();

            // 4. Hash
            String passHash = HashUtil.sha256(pass);

            // 5. Lógica negocio
            LoginResponse resp = service.login(user, passHash);

            String jsonRespuesta;

            if (resp == null || "error".equals(resp.resultado())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                ErrorResponse error = new ErrorResponse(
                        "error",
                        (resp != null) ? "Credenciales incorrectas" : "Error interno"
                );
                jsonRespuesta = mapper.writeValueAsString(error);

            } else {
                HttpSession sesion = request.getSession(true);
                sesion.setAttribute("codiUsua", resp.codiUsua());
                sesion.setMaxInactiveInterval(30 * 60);
                response.setStatus(HttpServletResponse.SC_OK);
                jsonRespuesta = mapper.writeValueAsString(resp);
            }

            // 6. CIFRAR → BYTES
            byte[] encrypted = CifradoCesar.encrypt(jsonRespuesta);

            // 7. ESCRIBIR COMO BYTES
            ServletOutputStream out = response.getOutputStream();
            out.write(encrypted);
            
            LOG.info(String.format("[RESPONSE] data: %s - encrypted: %s", jsonRespuesta, Arrays.toString(encrypted)));
            
            out.flush();

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ErrorResponse error = new ErrorResponse(
                    "error",
                    "Error procesando la solicitud"
            );
            
            String jsonError = mapper.writeValueAsString(error);
            byte[] encrypted = CifradoCesar.encrypt(jsonError);
            
            ServletOutputStream out = response.getOutputStream();
            out.write(encrypted);
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
