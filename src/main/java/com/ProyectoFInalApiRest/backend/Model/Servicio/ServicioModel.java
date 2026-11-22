package com.ProyectoFInalApiRest.backend.Model.Servicio;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Servicio")
@Data 
@AllArgsConstructor 
@NoArgsConstructor

public class ServicioModel {
    
    @Id
    private ObjectId id;
    private ObjectId idProveedor;
    private String Nombre;
    private String Categoria;
    private String Descripcion;
    private Integer DuracionServicio;
    private Integer Precio;
    private String Estado;
    private List<ServicioEvidencias> ServicioEvidencias;
    private List<ServicioRecurso> ServicioRecurso;
    


}
