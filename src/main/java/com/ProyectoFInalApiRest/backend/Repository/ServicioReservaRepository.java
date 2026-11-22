package com.ProyectoFInalApiRest.backend.Repository;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ProyectoFInalApiRest.backend.Model.ServicioReserva.ServicioReservaModel;

public interface ServicioReservaRepository extends MongoRepository<ServicioReservaModel, ObjectId>{
 
    // Hacemos la agregacion de listar id de reserva por cliente
    @Aggregation(pipeline = {
        "{'$match': {'idCliente': ?0 }}",
        "{'$sort': {'FechaHora': -1 }}"
    })
    List<ServicioReservaModel> listarReservaPorClienteList(ObjectId idCliente);

    //  Agregacion de lisatr por id Proveedor
    @Aggregation (pipeline = {
        "{'$lookup': {'from': 'Servicio', 'localField': 'idServicio', 'foreignField': '_id', 'as': 'servicio'}}",
        "{'$unwind': {'path': '$servicio'}}",
        "{'$match': {'servicio.idProveedor': ?0 }}",
        "{'$project': {'servicio': 0}}",
        "{'$sort': {'FechaHora': -1}}"
    })
    List<ServicioReservaModel> listarReservaPorProveedorList(ObjectId idProveedor);

    //  Agregacion de listar por Estado Solicitud
    @Aggregation(pipeline = {
        "{ '$match': { 'EstadoSolicitud': ?0 } }",
        "{ '$sort': { 'FechaHora': -1 } }"
    })
    List<ServicioReservaModel> listarReservaPorEstadoList(String estadosolicitud);
}
