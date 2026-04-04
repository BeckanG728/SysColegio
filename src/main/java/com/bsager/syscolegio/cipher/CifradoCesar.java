package com.bsager.syscolegio.cipher;

public class CifradoCesar {

    
    private static final int MIN_PRINTABLE = 32;      // Espacio
    private static final int MAX_PRINTABLE = 126;     // Tilde '~'
    private static final int RANGE = MAX_PRINTABLE - MIN_PRINTABLE + 1; // 95 caracteres

    public static String cifrar(String textoPlano) {
        if (textoPlano == null) return null;
        
        StringBuilder textoCifrado = new StringBuilder();

        for (char c : textoPlano.toCharArray()) {
            if (c >= MIN_PRINTABLE && c <= MAX_PRINTABLE) {
                // Solo cifrar caracteres imprimibles
                int nuevoCaracter = c + 3;
                if (nuevoCaracter > MAX_PRINTABLE) {
                    nuevoCaracter = MIN_PRINTABLE + (nuevoCaracter - MAX_PRINTABLE - 1);
                }
                textoCifrado.append((char) nuevoCaracter);
            } else {
                // Mantener caracteres no imprimibles como están
                textoCifrado.append(c);
            }
        }

        return textoCifrado.toString();
    }
    
    public static String descifrar(String textoCifrado) {
        if (textoCifrado == null) return null;
        
        StringBuilder textoPlano = new StringBuilder();

        for (char c : textoCifrado.toCharArray()) {
            if (c >= MIN_PRINTABLE && c <= MAX_PRINTABLE) {
                // Solo descifrar caracteres imprimibles
                int nuevoCaracter = c - 3;
                if (nuevoCaracter < MIN_PRINTABLE) {
                    nuevoCaracter = MAX_PRINTABLE - (MIN_PRINTABLE - nuevoCaracter - 1);
                }
                textoPlano.append((char) nuevoCaracter);
            } else {
                // Filtrar/ignorar caracteres no imprimibles
                // No se agregan al resultado
                // textoPlano.append(c); // Descomentar si quieres mantenerlos
            }
        }

        return textoPlano.toString();
    }
}
