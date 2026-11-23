package com.ProyectoFInalApiRest.backend.Model.ServicioReserva;

import java.util.Date;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Document("ServicioReserva") // Ponemos el nombre de la coleccion que tenemos en mongoDB
@Data 
@AllArgsConstructor 
@NoArgsConstructor

// En la clase ServicioReservaModel vamos a hacer una replica de los atributos de la coleecion servicio reserva que tenemos en la base de datos de mongoDB para que sea como un molde para java 
// y asi poder guardar, leer y armar los json mas adelante


public class ServicioReservaModel {
    @Id
    private ObjectId id;
    private ObjectId idCliente;
    private ObjectId idServicio;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")    // Con esto la fecha se escribe y se muestra como "2026-11-30 10:00" en hora de cali
    private Date FechaHora;
    private String Direccion;
    private String Barrio;
    private String InfoLugar;
    private String ObservacionesAdicionales;
    private String EstadoSolicitud;
    private ServicioCopia ServicioCopia;     //Copia embebida del servicio conserva el nombre, precio y duracion aunque el proveedor los cambie despues
    private String MotivoRechazoCancelacion;
    private Date FechaCreacionSolicitud;   
    private Date FechaActualizacionSolicitud;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "UTC")  // Con esto la fecha se escribe y se muestra como "2026-11-30 10:00" en hora de cali
    private Date FechaSugeridaSolicitud;

////////////////////////////////////////////////////////////////////////////////////////////////

    //Creamis el metodo que me permite mostrar los id como un hexadecimal
    @JsonGetter ("id")
    public String getIdjson(){
        return id != null ? id.toHexString():null;   
    }
    @JsonGetter ("idCliente")
    public String getIdClienteJson(){
        return idCliente != null ? idCliente.toHexString():null;   
    }
  
    @JsonGetter ("idServicio")
    public String getIdServicioJson(){
        return idServicio != null ? idServicio.toHexString():null;   
    }
}

