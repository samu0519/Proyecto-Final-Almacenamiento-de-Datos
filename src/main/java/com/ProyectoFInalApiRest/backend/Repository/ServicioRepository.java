package com.ProyectoFInalApiRest.backend.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ProyectoFInalApiRest.backend.Model.Servicio.ServicioModel;

// En el ServicioRepository es el que va a mongoDB a buscar los servicios de la coleccion Servicio
// Esta vacio porque solo usamos el findById que ya trae MongoRepository no necesitamos agregaciones
// En el imp lo usamos para revisar que el servicio exista que sea del proveedor y sacar los datos de la copia
public interface ServicioRepository extends MongoRepository<ServicioModel, ObjectId> {
 
}
