
package com.bsager.syscolegio.dto.request;

public record AlumnoRegisterRequest(
        String dni,
        String appa,
        String apma,
        String nomb,
        String corr,
        String cel,
        String sexo
) {}
