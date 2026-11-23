package com.ProyectoFInalApiRest.backend.Service;


import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.ProyectoFInalApiRest.backend.Repository.ServicioRepository;
import com.ProyectoFInalApiRest.backend.Model.Servicio.ServicioModel;
import com.ProyectoFInalApiRest.backend.Model.ServicioReserva.ServicioCopia;
import com.ProyectoFInalApiRest.backend.Model.ServicioReserva.ServicioReservaModel;
import com.ProyectoFInalApiRest.backend.Model.Usuario.UsuarioModel;
import com.ProyectoFInalApiRest.backend.Repository.ServicioReservaRepository;
import com.ProyectoFInalApiRest.backend.Repository.UsuarioRepository;

// logica de las reservas el controller llama todos estos metodos
@Service
public class ServicioReservaServiceImp implements ServicioReservaService {

// spring nos pasa el repository ya conectado, no hay que crearlo
@Autowired ServicioReservaRepository servicioReservaRepository;
@Autowired ServicioRepository servicioRepository;
@Autowired UsuarioRepository usuarioRepository;

//Creacion de reserva
@Override
public ServicioReservaModel crearReserva(ServicioReservaModel newReserva, ObjectId idProveedor) {
    if (newReserva == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La reserva es requerida.");
    }
    ObjectId clienteId = newReserva.getIdCliente();
    if (clienteId == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El cliente es requerido.");
    }
    ObjectId servicioId = newReserva.getIdServicio();
    if (servicioId == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El servicio es requerido.");
    }
    if (newReserva.getFechaHora() == null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La fecha y hora son requeridas.");
    }
    if (newReserva.getDireccion() == null || newReserva.getDireccion().isEmpty()){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La direccion es requerida.");
    }

    UsuarioModel cliente = usuarioRepository.findById(clienteId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El cliente no existe."));
        if (!cliente.getTipoUsu().equals("Cliente")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no es un cliente.");
        }
    ServicioModel servicio = servicioRepository.findById(servicioId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio no existe."));
    if (!servicio.getIdProveedor().equals(idProveedor)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El servicio no pertenece al proveedor.");
    }
    // Se llama el metodo para la validacion de curces de horario
    validarSinCruce(idProveedor, newReserva.getFechaHora(), servicio.getDuracionServicio(), null);

    // se crean las copias de los servicios
    ServicioCopia copia = new ServicioCopia();
    copia.setNombreServicioCopia(servicio.getNombre());
    copia.setPrecioServicioCopia(servicio.getPrecio());
    copia.setDuracionServicioCopia(servicio.getDuracionServicio());
    newReserva.setServicioCopia(copia);

    // se le asigna el estado inicial de la reserva
    newReserva.setEstadoSolicitud("Pendiente");

    // se le asigina la fecha
    Date ahora = horaLocal();
    newReserva.setFechaCreacionSolicitud(ahora);
    newReserva.setFechaActualizacionSolicitud(ahora);
    newReserva.setId(null);
    return servicioReservaRepository.save(newReserva);
}
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
        
        if (estadoActualizado == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Falta el estado nuevo");
        }

        List<String> estadosValidos = switch (estadoActual) {
            case "Pendiente" -> List.of("Aceptada", "Rechazada", "Sugerida");
            case "Sugerida" -> List.of("Aceptada", "Cancelada");
            case "Aceptada" -> List.of("Completada", "Cancelada");
            default -> List.of(); // Con default ya sabamos que no puede a ver un cambio en Completada, Rechazada o Cancelada, ya que son estados finales
        };
        if (!estadosValidos.contains(estadoActualizado)){ // Si permitidos no tienea estadoActualizado dara error
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Estado actual no valido");
        }
        ServicioModel servicio = servicioRepository.findById(reservaActual.getIdServicio())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio no existe."));
            
        ObjectId idProveedor = servicio.getIdProveedor();

        // la duracion es la guardada en la reserva (la copia), no la del catalogo
        int duracion = reservaActual.getServicioCopia().getDuracionServicioCopia();
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
            validarSinCruce(idProveedor, reservaNueva.getFechaSugeridaSolicitud(), duracion, id);
            reservaActual.setFechaSugeridaSolicitud(reservaNueva.getFechaSugeridaSolicitud());
        }
        if (estadoActualizado.equals("Aceptada")) {
            // si viene de pendiente se acepta la fecha original, si viene de sugerida la que propuso el proveedor
            Date fechaAceptar = reservaActual.getFechaHora();
            if (estadoActual.equals("Sugerida")) {
                fechaAceptar = reservaActual.getFechaSugeridaSolicitud();
            }
            validarSinCruce(idProveedor, fechaAceptar, duracion, id);
            // aqui va la disponibilidad (regla 4)

            // si el cliente acepto la propuesta, la reserva queda en esa fecha
            reservaActual.setFechaHora(fechaAceptar);
        }
 
        reservaActual.setEstadoSolicitud(estadoActualizado);
        reservaActual.setFechaActualizacionSolicitud(horaLocal());
        return servicioReservaRepository.save(reservaActual);
    }

    //Validar que no hayan cruces de horario
    public  void validarSinCruce(ObjectId idProveedor, Date fechaHora, int duracion, ObjectId idIgnorar) {
        // pasamos la fecha a hora de local y le sumamos la duracion para saber a que hora termina
        LocalDateTime inicioNueva = LocalDateTime.ofInstant(fechaHora.toInstant(), ZoneId.of("UTC"));
        LocalDateTime finNueva = inicioNueva.plusMinutes(duracion);

        List<ServicioReservaModel> reservasActivas = servicioReservaRepository.listarReservasActivasPorProveedorList(idProveedor);

        for (ServicioReservaModel reserva : reservasActivas) {
            // solo revisamos las que no son la misma reserva
            if (!reserva.getId().equals(idIgnorar)) {
                LocalDateTime inicioActiva = LocalDateTime.ofInstant(reserva.getFechaHora().toInstant(), ZoneId.of("UTC"));
                LocalDateTime finActiva = inicioActiva.plusMinutes(reserva.getServicioCopia().getDuracionServicioCopia());

                // se cruzan si la nueva empieza antes de que termine la activa y la activa empieza antes de que termine la nueva
                if (inicioNueva.isBefore(finActiva) && inicioActiva.isBefore(finNueva)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El horario se cruza con otra reserva del proveedor");
                }
            }
        }
    }
    // da la fecha y hora actual de cali, guardada igual que las demas (la hora local tal cual)
    public Date horaLocal() {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Bogota"));   // mira el reloj de cali
        return Date.from(ahora.toInstant(ZoneOffset.UTC));                     // la guarda sin sumarle horas
    }
}
    