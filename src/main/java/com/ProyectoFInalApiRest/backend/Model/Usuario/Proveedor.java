package com.ProyectoFInalApiRest.backend.Model.Usuario;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@AllArgsConstructor 
@NoArgsConstructor
public class Proveedor {
    
    private List<ZonaAtencion> ZonaAtencion;
    private List<HorarioDisponible> HorarioDisponible;
    private List<HorarioNoDisponible> HorarioNoDisponible;
    private PerfilProfesional PerfilProfesional;

}
