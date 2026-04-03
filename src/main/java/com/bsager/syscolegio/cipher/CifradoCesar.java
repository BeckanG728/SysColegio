package com.bsager.syscolegio.cipher;

public class CifradoCesar {

    
    public static String cifrar(String textoPlano) {
        StringBuilder textoCifrado = new StringBuilder();

        for (char c : textoPlano.toCharArray()) {
            textoCifrado.append((char) (c + 3));
        }

        return textoCifrado.toString();
    }
    
    public static String descifrar(String textoCifrado) {
        StringBuilder textoPlano = new StringBuilder();

        for (char c : textoCifrado.toCharArray()) {
            textoPlano.append((char) (c - 3));
        }

        return textoPlano.toString();
    }
}
