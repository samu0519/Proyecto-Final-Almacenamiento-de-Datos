package com.ProyectoFInalApiRest.backend.Model.Usuario;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor

// En la clase Proveedor vamos a hacer una replica de los atributos del proveedor que va embebido dentro del usuario
// No es una coleccion aparte solo existe en los usuarios que son TipoUsu Proveedor
// Aqui estan los horarios que usamos para validar la disponibilidad
public class Proveedor {
    
    private List<ZonaAtencion> ZonaAtencion;
    private List<HorarioDisponible> HorarioDisponible; // Los dias y horas en que atiende
    private List<HorarioNoDisponible> HorarioNoDisponible; // Las fechas en que no atiende
    private PerfilProfesional PerfilProfesional;

}
