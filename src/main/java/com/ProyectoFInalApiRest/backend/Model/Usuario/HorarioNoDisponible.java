package com.ProyectoFInalApiRest.backend.Model.Usuario;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  

// En la clase HorarioNoDisponible vamos a hacer una replica de los atributos de los periodos en que el proveedor no atiende
// Va como lista embebida dentro de Proveedor por ejemplo vacaciones o una capacitacion
// Lo usamos al aceptar una reserva para revisar que no caiga en uno de estos periodos

public class HorarioNoDisponible {
    private Date FechaInicio;
    private Date FechaFin;
    private String Motivo;
    
}
