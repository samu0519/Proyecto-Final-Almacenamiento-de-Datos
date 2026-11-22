package com.ProyectoFInalApiRest.backend.Service;

import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.ProyectoFInalApiRest.backend.Model.ServicioReserva.ServicioReservaModel;
import com.ProyectoFInalApiRest.backend.Repository.ServicioReservaRepository;

// logica de las reservas el controller llama todos estos metodos
@Service
public class ServicioReservaServiceImp implements ServicioReservaService {

    // spring nos pasa el repository ya conectado, no hay que crearlo
    @Autowired ServicioReservaRepository servicioReservaRepository;

    // llamamis a la agregacion del repository y le devolvemos la lista al controller, asi con cada metodo
    @Override
    public List<ServicioReservaModel> listarReservaPorClienteList(ObjectId idCliente) {
        return servicioReservaRepository.findAll();
    }

    @Override
    public List<ServicioReservaModel> listarReservaPorProveedorList(ObjectId idProveedor) {
        return servicioReservaRepository.listarReservaPorProveedorList(idProveedor);
    }

    @Override
    public List<ServicioReservaModel> listarReservaPorEstadoList(String estadoSolicitud) {
        return servicioReservaRepository.listarReservaPorEstadoList(estadoSolicitud);
    }

    @Override
    public ServicioReservaModel buscarReservaPorId(ObjectId id) { // usamos el metodo findById que ya tiene el repository para buscar por id, y si no encuentra nada devuelve error
        return servicioReservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR! No se encontro reserva"));
    }

    @Override
    public void eliminarReserva(ObjectId id) {
            ServicioReservaModel reserva = buscarReservaPorId(id); // llamamos al metodo de buscar la reserva por id y con esto obtenemos podemos armmar el condicional
         if (reserva.getEstadoSolicitud().equals("Aceptada") || reserva.getEstadoSolicitud().equals("Completada")) { // medainte un if anadimos la restriccion de aceptada o completada
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! no se puede eliminar una reserva Aceptada o Completada.");
        }
        servicioReservaRepository.deleteById(id);
    }
}