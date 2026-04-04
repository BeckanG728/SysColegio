/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Record.java to edit this template
 */
package com.bsager.syscolegio.dto.response;

/**
 *
 * @author chila
 */
public record AlumnoResponse(
        String resultado,
        String nombre,
        String correo,
        String celular,
        String sexo
) {
    
}
