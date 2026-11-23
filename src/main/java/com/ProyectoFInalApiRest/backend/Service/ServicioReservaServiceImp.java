package com.ProyectoFInalApiRest.backend.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
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
import com.ProyectoFInalApiRest.backend.Model.Usuario.HorarioDisponible;       
import com.ProyectoFInalApiRest.backend.Model.Usuario.HorarioNoDisponible;
import com.ProyectoFInalApiRest.backend.Repository.UsuarioRepository;

// En el ServicioReservaServiceImp esta toda la logica de las reservas 
// Aqui se hace el codigo de cada metodo que pusimos en el ServicioReservaService  
// El controller llama estos metodos y aqui revisamos las reglas antes de ir a mongo con el repository

@Service
public class ServicioReservaServiceImp implements ServicioReservaService {

// Spring nos pasa el repository ya conectado no hay que crearlo
@Autowired ServicioReservaRepository servicioReservaRepository;
@Autowired ServicioRepository servicioRepository;// Para buscar el servicio y sacar la copia y el proveedor
@Autowired UsuarioRepository usuarioRepository;// Para buscar al cliente y al proveedor con sus horarios


///////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Creacion de reserva
//Recibe la reserva que se pone en el body y el idProveedor que llega en el url
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

    // Se crean las copias de los servicios
    ServicioCopia copia = new ServicioCopia();
    copia.setNombreServicioCopia(servicio.getNombre());
    copia.setPrecioServicioCopia(servicio.getPrecio());
    copia.setDuracionServicioCopia(servicio.getDuracionServicio());
    newReserva.setServicioCopia(copia);

    // Se le asigna el estado inicial de la reserva
    newReserva.setEstadoSolicitud("Pendiente");

    // Se le asigina la fecha local
    Date ahora = horaLocal();
    newReserva.setFechaCreacionSolicitud(ahora);
    newReserva.setFechaActualizacionSolicitud(ahora);

    // Si alguien manda un id en el body el save reemplazaria esa reserva en vez de crear otra
    newReserva.setId(null);
    return servicioReservaRepository.save(newReserva); 
}


///////////////////////////////////////////////////////////////////////////////////////////////
// Consultas
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
    

////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Eliminar reserva
    @Override
    public void eliminarReserva(ObjectId id) {
            ServicioReservaModel reserva = buscarReservaPorId(id); // Llamamos al metodo de buscar la reserva por id y con esto obtenemos podemos armmar el condicional
         if (reserva.getEstadoSolicitud().equals("Aceptada") || reserva.getEstadoSolicitud().equals("Completada")) { // medainte un if anadimos la restriccion de aceptada o completada
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! no se puede eliminar una reserva Aceptada o Completada");
        }
        servicioReservaRepository.deleteById(id);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Actualizar estado de la reserva
    @Override
    public ServicioReservaModel cambiarEstadoReserva(ObjectId id, ServicioReservaModel reservaNueva) {
        
        ServicioReservaModel reservaActual = buscarReservaPorId(id); //Traemos la reserva de mongo y sacamos el estado que tiene y el estado al que quiere pasar
        String estadoActual = reservaActual.getEstadoSolicitud();
        String estadoActualizado = reservaNueva.getEstadoSolicitud();
        
        if (estadoActualizado == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Falta el estado nuevo");
        }

        List<String> estadosValidos = switch (estadoActual) {// Segun el estado actual armamos la lista de estados a los que si se puede pasar
            case "Pendiente" -> List.of("Aceptada", "Rechazada", "Sugerida");
            case "Sugerida" -> List.of("Aceptada", "Cancelada");
            case "Aceptada" -> List.of("Completada", "Cancelada");
            default -> List.of(); // Con default ya sabamos que no puede a ver un cambio en Completada, Rechazada o Cancelada, ya que son estados finales
        };

        if (!estadosValidos.contains(estadoActualizado)){ // Si permitidos no tienea estadoActualizado dara error
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Estado actual no valido");
        }

        ServicioModel servicio = servicioRepository.findById(reservaActual.getIdServicio()) // Buscamos el servicio para sacar el idProveedor porque la reserva no lo tiene, lo usamos en cruces y disponibilidad // Se crea este metodo para revisar los cruces y la disponibilidad del proveedo
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El servicio no existe."));
        ObjectId idProveedor = servicio.getIdProveedor();

        int duracion = reservaActual.getServicioCopia().getDuracionServicioCopia(); //La duracion es la guardada en la copia

         if (estadoActualizado.equals("Rechazada") || estadoActualizado.equals("Cancelada") ){ // Si se pasa a un estadp de cancelacion o rechazo se debe dar el motivo
            if(reservaNueva.getMotivoRechazoCancelacion() == null || reservaNueva.getMotivoRechazoCancelacion().isEmpty()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Debe haber un motivo para rechazo o de cancelacion de la reserva");
            }
            reservaActual.setMotivoRechazoCancelacion(reservaNueva.getMotivoRechazoCancelacion());
        }
        

        // El estado de sugerida nos pide la fecha nueva 
        if (estadoActualizado.equals("Sugerida")){
            if(reservaNueva.getFechaSugeridaSolicitud() == null){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! Falta la fecha menor");
            }
            validarSinCruce(idProveedor, reservaNueva.getFechaSugeridaSolicitud(), duracion, id);
            reservaActual.setFechaSugeridaSolicitud(reservaNueva.getFechaSugeridaSolicitud());
        }
        if (estadoActualizado.equals("Aceptada")) {
           
            Date fechaAceptar = reservaActual.getFechaHora(); // si viene de pendiente se acepta la fecha original si viene de sugerida la que propuso el proveedor

            if (estadoActual.equals("Sugerida")) {
                fechaAceptar = reservaActual.getFechaSugeridaSolicitud();
            }
            validarSinCruce(idProveedor, fechaAceptar, duracion, id); // Se valida que no haya cruces de horario con otras reservas activas
            validarDisponibilidad(idProveedor, fechaAceptar, duracion);// Se valida que el proveedor este disponible en ese horario
            reservaActual.setFechaHora(fechaAceptar);
        }
 
        reservaActual.setEstadoSolicitud(estadoActualizado);
        reservaActual.setFechaActualizacionSolicitud(horaLocal());
        return servicioReservaRepository.save(reservaActual);
    }


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Validar que no hayan cruces de horario

    // Revisamos que la reserva nueva no se choque con otra reserva activa del mismo proveedor 
    // Lo llamamos en crear sugerir y aceptar, si hay cruce lanza error y no se guarda nada

    public void validarSinCruce(ObjectId idProveedor, Date fechaHora, int duracion, ObjectId idIgnorar) {

        LocalDateTime inicioNueva = LocalDateTime.ofInstant(fechaHora.toInstant(), ZoneId.of("UTC"));// Usamos UTC asi la hora queda como esta en mongo
        LocalDateTime finNueva = inicioNueva.plusMinutes(duracion);

        List<ServicioReservaModel> reservasActivas = servicioReservaRepository.listarReservasActivasPorProveedorList(idProveedor);

        for (ServicioReservaModel reserva : reservasActivas) {
 
            if (!reserva.getId().equals(idIgnorar)) { // Solo revisamos las que no son la misma reserva

                LocalDateTime inicioActiva = LocalDateTime.ofInstant(reserva.getFechaHora().toInstant(), ZoneId.of("UTC"));
                LocalDateTime finActiva = inicioActiva.plusMinutes(reserva.getServicioCopia().getDuracionServicioCopia());

                if (inicioNueva.isBefore(finActiva) && inicioActiva.isBefore(finNueva)) { // Se cruzan si la nueva empieza antes de que termine la activa y la activa empieza antes de que termine la nueva

                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El horario se cruza con otra reserva del proveedor");
                }
            }
        }
            
    }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Validar disponibilidad del proveedor
    public void validarDisponibilidad(ObjectId idProveedor, Date fecha, int duracion) {

        UsuarioModel proveedor = usuarioRepository.findById(idProveedor)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Error! Proveedor no encontrado"));

        LocalDateTime inicio = LocalDateTime.ofInstant(fecha.toInstant(), ZoneId.of("UTC"));// UTC igual que en cruces asi la hora queda como esta en mongo
        LocalDateTime fin = inicio.plusMinutes(duracion);

        String dia = switch (inicio.getDayOfWeek()) {
            case MONDAY -> "Lunes";
            case TUESDAY -> "Martes";
            case WEDNESDAY -> "Miercoles";
            case THURSDAY -> "Jueves";
            case FRIDAY -> "Viernes";
            case SATURDAY -> "Sabado";
            case SUNDAY -> "Domingo";
        };

        boolean disponible = false;

        if (proveedor.getProveedor().getHorarioDisponible() != null) { // Se revisa si algun horario de ese dia cubre toda la reserva

            for (HorarioDisponible horario : proveedor.getProveedor().getHorarioDisponible()) {
                LocalTime horaInicio = LocalTime.parse(horario.getHoraInicio());
                LocalTime horaFin = LocalTime.parse(horario.getHoraFin());
                if (horario.getDiasSemana().equals(dia) &&
                    !inicio.toLocalTime().isBefore(horaInicio) &&
                    !fin.toLocalTime().isAfter(horaFin)) {
                    disponible = true;
                }
            }
        }

        if (!disponible) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El proveedor no trabaja en ese horario");
        }

        if (proveedor.getProveedor().getHorarioNoDisponible() != null) {// Si no tiene periodos bloqueados no hay nada que revisar

            for (HorarioNoDisponible horario : proveedor.getProveedor().getHorarioNoDisponible()) {
                LocalDateTime bloqueoInicio = LocalDateTime.ofInstant(horario.getFechaInicio().toInstant(), ZoneId.of("UTC"));
                LocalDateTime bloqueoFin = LocalDateTime.ofInstant(horario.getFechaFin().toInstant(), ZoneId.of("UTC"));

                if (inicio.isBefore(bloqueoFin) && fin.isAfter(bloqueoInicio)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error! El proveedor no esta disponible en ese periodo");
                }
            }
        }
    }

    public Date horaLocal() {     // Da la fecha y hora actual de cali, guardada igual que las demas (la hora local tal cual)
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Bogota"));   
        return Date.from(ahora.toInstant(ZoneOffset.UTC));                     
    }
}
    