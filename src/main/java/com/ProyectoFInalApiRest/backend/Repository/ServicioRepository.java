package com.ProyectoFInalApiRest.backend.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ProyectoFInalApiRest.backend.Model.Servicio.ServicioModel;


public interface ServicioRepository extends MongoRepository<ServicioModel, ObjectId> {
 
}
