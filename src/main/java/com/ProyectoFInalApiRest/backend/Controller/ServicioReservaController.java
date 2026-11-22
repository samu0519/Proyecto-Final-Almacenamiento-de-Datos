package com.ProyectoFInalApiRest.backend.Controller;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

// Listar resorvas por cliente
    @GetMapping ("/cliente/{idCliente}")
    public ResponseEntity<List<ServicioReservaModel>> listarReservaPorClienteList(@PathVariable ObjectId idCliente) {
        return new ResponseEntity<>(servicioReservaService.listarReservaPorClienteList(idCliente), HttpStatus.OK);
    }
// Listar reservas por proveedor
    @GetMapping ("/proveedor/{idProveedor}")
    public ResponseEntity<List<ServicioReservaModel>> listarReservaPorProveedorList(@PathVariable ObjectId idProveedor) {
        return new ResponseEntity<>(servicioReservaService.listarReservaPorProveedorList(idProveedor), HttpStatus.OK);
    }
// Listar reservas por estado Activo
    @GetMapping ("/estado/{estadoSolicitud}")
    public ResponseEntity<List<ServicioReservaModel>> listarReservaPorEstadoList(@PathVariable String estadoSolicitud) {
        return new ResponseEntity<>(servicioReservaService.listarReservaPorEstadoList(estadoSolicitud), HttpStatus.OK);
    }
// Eliminar reserva
    @DeleteMapping("/eliminar/{idR}")
    public ResponseEntity<String> eliminarReserva(@PathVariable("idR") ObjectId idReserva) {
        servicioReservaService.eliminarReserva(idReserva);
        return new ResponseEntity<String>("Reserva eliminada", HttpStatus.OK);
    }
// Cambiar estado de la reserva
    @PutMapping("/{id}/estado")
    public ResponseEntity<ServicioReservaModel> cambiarEstadoReserva(@PathVariable ObjectId id, @RequestBody ServicioReservaModel reservaNueva) {
        return new ResponseEntity<>(servicioReservaService.cambiarEstadoReserva(id, reservaNueva), HttpStatus.OK);
    }
}   
