package com.bsager.syscolegio.filter;

import com.bsager.syscolegio.cipher.CifradoCesar;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author
 */
@WebFilter("/app/*")
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        
        // false = no crear sesion nueva si no existe
        HttpSession sesion = req.getSession(false);
        boolean autenticado = sesion != null && sesion.getAttribute("codiUsua") != null;
        
        if (autenticado) {
        // Sesion valida -> dejar pasar al servlet
            chain.doFilter(request, response);
        } else {
        // Sin sesion -> responder con JSON de error cifrado
            String jsonError
                    = "{\"resultado\":\"error\"," + "\"mensaje\":\"Sesion expirada o no iniciada\"}";
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            resp.setContentType("text/plain;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.print(CifradoCesar.cifrar(jsonError));
            }
        // No llamar chain.doFilter -> peticion bloqueada
        }
    }

    @Override
    public void destroy() {}

}
