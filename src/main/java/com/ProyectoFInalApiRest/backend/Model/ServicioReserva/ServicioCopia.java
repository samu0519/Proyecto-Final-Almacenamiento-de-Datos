package com.ProyectoFInalApiRest.backend.Model.ServicioReserva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor

// En la clase ServicioCopia vamos a hacer una replica de los atributos de la copia del servicio que va embebida dentro de la reserva
// No es una coleccion aparte esta vive adentro de cada documento de ServicioReserva
// Se hace como copia para que si el proveedor cambia el nombre, precio o duracion en su catalogo la reserva no cambie
public class ServicioCopia {

    private String NombreServicioCopia;
    private Integer PrecioServicioCopia;
    private Integer DuracionServicioCopia; //De aqui sale la duracion que usamos para calcular los cruces de horario

}
