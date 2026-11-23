package com.ProyectoFInalApiRest.backend.Model.Usuario;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("Usuario") //Ponemos el nombre de la coleccion de mongoDB que vamos a usar
@Data 
@AllArgsConstructor 
@NoArgsConstructor

// En la clase UsuarioModel vamos a hacer una replica de los atributos de la coleccion Usuario que tenemos en la base de datos de mongoDB para que sea como un molde para java
// Clientes y proveedores estan en la misma coleccion los diferenciamos con TipoUsu
// En las reservas lo usamos para revisar que el cliente exista y para traer los horarios del proveedor


public class UsuarioModel {

    @Id
    private ObjectId id;
    private String Nombre;
    private String Apellido;
    private String Contrasena;
    private String Correo;
    private Integer Edad;
    private String TipoUsu; // Cliente o Proveedor con esto sabemos que tipo de usuario es
    private Proveedor Proveedor; // Solo lo tienen los proveedores va embebido con sus horarios y perfil
    private Cliente Cliente; // Solo lo tienen los clientes, va embebido con su direccion
}
