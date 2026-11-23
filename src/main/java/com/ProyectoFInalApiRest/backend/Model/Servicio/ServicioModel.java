package com.ProyectoFInalApiRest.backend.Model.Servicio;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Servicio") // Ponemos el nombre de la coleccion que tenemos en mongoDB
@Data 
@AllArgsConstructor 
@NoArgsConstructor

// En la clase ServicioModel vamos a hacer una replica de los atributos de la coleccion Servicio que tenemos en la base de datos de mongoDB para que sea como un molde para java
// En las reservas lo usamos para revisar que el servicio exista, que sea del proveedor y para sacar los datos de la copia

public class ServicioModel {
    
    @Id
    private ObjectId id;
    private ObjectId idProveedor; 
    private String Nombre;
    private String Categoria;
    private String Descripcion;
    private Integer DuracionServicio; // Lo tenemos en minutos esto se copia a la reserva al crearla
    private Integer Precio;
    private String Estado;
    private List<ServicioEvidencias> ServicioEvidencias; // Lista embebida de las evidencias no se usa en el proeyecto pero lo dejamos para una entrega completa del modelo
    private List<ServicioRecurso> ServicioRecurso;
    
}
