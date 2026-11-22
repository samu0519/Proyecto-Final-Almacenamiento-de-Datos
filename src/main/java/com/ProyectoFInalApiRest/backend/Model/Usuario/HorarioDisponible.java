package com.ProyectoFInalApiRest.backend.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  
public class HorarioDisponible {
    private String HoraInicio;
    private String HoraFin;
    private String DiasSemana;
    
}
