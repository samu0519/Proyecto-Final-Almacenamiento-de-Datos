package com.ProyectoFInalApiRest.backend.Model.ServicioReserva;

 import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor

// Anadimos el modelo de servicio copia y poder hacer la reserva de este tipo de servicio, se hace aparte puesto que es un
public class ServicioCopia {

    private String NombreServicioCopia;
    private Integer PrecioServicioCopia;
    private Integer DuracionServicioCopia;


}
