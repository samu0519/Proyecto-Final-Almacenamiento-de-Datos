package com.ProyectoFInalApiRest.backend.Controller;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ProyectoFInalApiRest.backend.Model.ServicioReserva.ServicioReservaModel;
import com.ProyectoFInalApiRest.backend.Service.ServicioReservaService;
 
@RestController
@RequestMapping ("/Proyecto/reservas")
public class ServicioReservaController {
    @Autowired ServicioReservaService servicioReservaService;

// Bucar por id
    @GetMapping ("/{id}")
    public ResponseEntity<ServicioReservaModel> buscarReservaPorId(@PathVariable ObjectId id) {
        return new ResponseEntity<ServicioReservaModel>(servicioReservaService.buscarReservaPorId(id), HttpStatus.OK);
    }
}
