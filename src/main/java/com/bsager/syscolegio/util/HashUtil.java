package com.bsager.syscolegio.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    private HashUtil() {
    }

    /**
     * Convierte un String a su hash SHA-256 en formato hexadecimal.Ejemplo: sha256("1234") 
     * -> "03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4"
     *
     * @param input
     * @return 
     */
    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
            // & 0xff trata el byte como sin signo (0-255)
                String parte = Integer.toHexString(0xff & b);
                if (parte.length() == 1) {
                    hex.append('0'); // padding a 2 chars
                }
                hex.append(parte);
            }
            return hex.toString(); // siempre 64 caracteres
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al calcular SHA-256", e);
        }
    }
}
