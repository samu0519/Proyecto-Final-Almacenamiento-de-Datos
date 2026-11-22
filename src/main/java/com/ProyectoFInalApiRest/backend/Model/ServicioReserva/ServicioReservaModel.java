package com.ProyectoFInalApiRest.backend.Model.ServicioReserva;

import java.util.Date;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("ServicioReserva")
@Data 
@AllArgsConstructor 
@NoArgsConstructor

public class ServicioReservaModel {
    @Id
    private ObjectId id;
    private ObjectId idCliente;
    private ObjectId idServicio;
     private Date FechaHora;
    private String Direccion;
    private String Barrio;
    private String InfoLugar;
    private String ObservacionesAdicionales;
    private String EstadoSolicitud;
    //Copia embebida del servicio: conserva nombre, precio y duración aunque el proveedor los cambie después
    private ServicioCopia ServicioCopia; 
    private String MotivoRechazoCancelacion;
    private Date FechaCreacionSolicitud;   
    private Date FechaActualizacionSolicitud;
    private Date FechaSugeridaSolicitud;
    //Creamis el metodo que me permite mostrar id del usuario como un hexadecimal
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

