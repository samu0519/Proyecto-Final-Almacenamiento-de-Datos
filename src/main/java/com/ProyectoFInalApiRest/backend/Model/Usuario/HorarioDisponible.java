package com.ProyectoFInalApiRest.backend.Model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor  

// En la clase HorarioDisponible vamos a hacer una replica de los atributos del horario general del proveedor
// Va como lista embebida dentro de Proveedor, cada uno es un dia con su hora de inicio y fin
// Lo usamos al aceptar una reserva para revisar que el proveedor atienda ese dia y a esa hora

public class HorarioDisponible {
    private String HoraInicio; // Se guarda como texto "08:00" y en el imp lo pasamos a hora con LocalTime
    private String HoraFin;
    private String DiasSemana;
    
}
