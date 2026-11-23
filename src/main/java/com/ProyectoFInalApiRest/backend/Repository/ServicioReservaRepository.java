    package com.ProyectoFInalApiRest.backend.Repository;

    import java.util.List;
    import org.bson.types.ObjectId;
    import org.springframework.data.mongodb.repository.Aggregation;
    import org.springframework.data.mongodb.repository.MongoRepository;
    import com.ProyectoFInalApiRest.backend.Model.ServicioReserva.ServicioReservaModel;

// En el ServicioReservaRepository es el que va a mongoDB a buscar y guardar las reservas de la coleccion ServicioReserva
// Con MongoRepository<ServicioReservaModel, ObjectId> le decimos que use el molde de la reserva y que el id es un ObjectId
// MongoRepository ya nos trae hechos findById, save y deleteById, aqui solo ponemos las agregaciones que no vienen incluidas

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
            "{ '$sort': { 'FechaHora': -1 }}"
        })
        List<ServicioReservaModel> listarReservaPorEstadoList(String estadoSolicitud);

        // Agregacion para reservas activas de cada proveedor   
        @Aggregation(pipeline = {
            "{'$lookup': {'from': 'Servicio', 'localField': 'idServicio', 'foreignField': '_id', 'as': 'servicio'}}",
            "{'$unwind': {'path': '$servicio'}}",
            "{'$match': {'servicio.idProveedor': ?0, 'EstadoSolicitud': {'$in': ['Pendiente', 'Sugerida', 'Aceptada']}}}", // con el $in filtramos solo las reservas que estan en Pendiente, Sugerida o Aceptada, que son las que ocupan horario en este caso
            "{'$project': {'servicio': 0}}",
            "{'$sort': {'FechaHora': 1}}"
        })
        List<ServicioReservaModel> listarReservasActivasPorProveedorList(ObjectId idProveedor);

    }