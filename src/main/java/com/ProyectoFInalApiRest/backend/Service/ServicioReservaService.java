package com.ProyectoFInalApiRest.backend.Service;

import java.util.List;
import org.bson.types.ObjectId;
import com.ProyectoFInalApiRest.backend.Model.ServicioReserva.ServicioReservaModel;

// En el ServicioReservaService ponemos la lista de lo que se puede hacer con las reservas
// Aqui solo van los nombres de los metodos sin el codigo 
// El controller llama es el qe llama a estos metodoso
public interface ServicioReservaService {
    public ServicioReservaModel crearReserva(ServicioReservaModel newReserva, ObjectId idProveedor);
    public List<ServicioReservaModel> listarReservaPorClienteList(ObjectId idCliente);
    public List<ServicioReservaModel> listarReservaPorProveedorList(ObjectId idProveedor);
    public List<ServicioReservaModel> listarReservaPorEstadoList(String estadoSolicitud);
    public ServicioReservaModel buscarReservaPorId(ObjectId id); // No hay ningun metodo buscar en el repository dado que el extends mongorepository ya tiene un metodo findById que hace lo mismo, pero lo hacemos aqui para que sea mas facil de usar en el controller
    public void eliminarReserva(ObjectId id);
    public ServicioReservaModel cambiarEstadoReserva(ObjectId id, ServicioReservaModel reservaNueva);
   
}
