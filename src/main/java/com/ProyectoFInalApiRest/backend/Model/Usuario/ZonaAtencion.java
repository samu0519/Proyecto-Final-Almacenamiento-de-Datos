package com.ProyectoFInalApiRest.backend.Model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor   
public class ZonaAtencion {
    private String Ciudad;
    private String Barrio;
}
