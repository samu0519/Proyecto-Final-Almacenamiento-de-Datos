package com.ProyectoFInalApiRest.backend.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ProyectoFInalApiRest.backend.Model.Usuario.UsuarioModel;

public interface UsuarioRepository extends MongoRepository<UsuarioModel, ObjectId> {

}