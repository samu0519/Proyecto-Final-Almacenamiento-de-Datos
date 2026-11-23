package com.ProyectoFInalApiRest.backend.Repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.ProyectoFInalApiRest.backend.Model.Usuario.UsuarioModel;

// En el UsuarioRepository es el que va a mongoDB a buscar los usuarios de la coleccion Usuario
// Esta vacio porque solo usamos el findById que ya trae MongoRepository, no necesitamos agregaciones
// En el imp lo usamos para revisar que el cliente exista y para traer los horarios del proveedor
public interface UsuarioRepository extends MongoRepository<UsuarioModel, ObjectId> {

}