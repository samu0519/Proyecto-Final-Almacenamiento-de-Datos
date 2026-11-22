package com.ProyectoFInalApiRest.backend.Model.Usuario;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  
public class HorarioNoDisponible {
    private Date FechaInicio;
    private Date FechaFin;
    private String Motivo;
    
}
