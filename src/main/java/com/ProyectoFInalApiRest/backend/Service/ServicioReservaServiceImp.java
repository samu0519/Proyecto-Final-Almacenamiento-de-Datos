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

    // Busqueda por Id
    @Override
    public ServicioReservaModel buscarReservaPorId(ObjectId id) { // usamos el metodo findById que ya tiene el repository para buscar por id, y si no encuentra nada devuelve error
        return servicioReservaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ERROR! No se encontro reserva"));
    }
    // llamamis a la agregacion del repository y le devolvemos la lista al controller, asi con cada metodo
    // LIstar reserva por cliente
    @Override
    public List<ServicioReservaModel> listarReservaPorClienteList(ObjectId idCliente) {
        return servicioReservaRepository.listarReservaPorClienteList(idCliente);    }
    
    // Listar reserva por proveedor
    @Override
    public List<ServicioReservaModel> listarReservaPorProveedorList(ObjectId idProveedor) {
        return servicioReservaRepository.listarReservaPorProveedorList(idProveedor);
    }

    //Listar reserva por estado
    @Override
    public List<ServicioReservaModel> listarReservaPorEstadoList(String estadoSolicitud) {
        return servicioReservaRepository.listarReservaPorEstadoList(estadoSolicitud);
    }
    
    //Eliminar reserva
    @Override
    public void eliminarReserva(ObjectId id) {
            ServicioReservaModel reserva = buscarReservaPorId(id); // llamamos al metodo de buscar la reserva por id y con esto obtenemos podemos armmar el condicional
         if (reserva.getEstadoSolicitud().equals("Aceptada") || reserva.getEstadoSolicitud().equals("Completada")) { // medainte un if anadimos la restriccion de aceptada o completada
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! no se puede eliminar una reserva Aceptada o Completada");
        }
        servicioReservaRepository.deleteById(id);
    }

    //Actualizar estado de la reserva
    @Override
    public ServicioReservaModel cambiarEstadoReserva(ObjectId id, ServicioReservaModel reservaNueva) {
  
        ServicioReservaModel reservaActual = buscarReservaPorId(id);
        String  estadoActual = reservaActual.getEstadoSolicitud();
        String estadoActualizado = reservaNueva.getEstadoSolicitud();

        List<String> estadosValidos = switch (estadoActual) {
            case "Pendiente" -> List.of("Aceptada", "Rechazada", "Sugerida");
            case "Sugerida" -> List.of("Aceptada", "Cancelada");
            case "Aceptada" -> List.of("Completada", "Cancelada");
            default -> List.of(); // Con default ya sabamos que no puede a ver un cambio en Completada, Rechazada o Cancelada, ya que son estados finales
        };
        if (!estadosValidos.contains(estadoActualizado)){ // Si permitidos no tienea estadoActualizado dara error
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Estado actual no valido");
        }

        // Si se pasa a un estado de cancelacion o rechazo se debe dar el motivo
        if (estadoActualizado.equals("Rechazada") || estadoActualizado.equals("Cancelada") ){
            if(reservaNueva.getMotivoRechazoCancelacion() == null || reservaNueva.getMotivoRechazoCancelacion().isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Debe haber un motivo para rechazo o de cancelacion de la reserva");
            }
            reservaActual.setMotivoRechazoCancelacion(reservaNueva.getMotivoRechazoCancelacion());
        }
        
        // La estado de sugerir nos pide la una fecha nueva 

        if (estadoActualizado.equals("Sugerida")){
            if(reservaNueva.getFechaSugeridaSolicitud() == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Falta la fecha menor");
            }
        }

        //  hace falta revisar cruces y disponibilidad

        reservaActual.setEstadoSolicitud(estadoActualizado);
        reservaActual.setFechaActualizacionSolicitud(new java.util.Date());
        return servicioReservaRepository.save(reservaActual);
    }
}