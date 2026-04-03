package com.bsager.syscolegio.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author
 */
public class Base64Util {

    // Evita instanciación
    private Base64Util() {
    }

    public static String encode(String textoPlano) {

        try {
            return Base64.getEncoder()
                    .encodeToString(textoPlano.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public static String decode(String textoBase64) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(textoBase64);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            return null;
        }

    }
}
