/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi.grupo5_empleado.legajo.Service;

import java.util.List;
import tpi.grupo5_empleado.legajo.Dao.EmpleadoDAO;
import tpi.grupo5_empleado.legajo.Models.Empleado;

/**
 *
 * @author gauta
 */
public class EmpleadoServiceImpl implements GenericService<Empleado>{

    /**
     * DAO para acceso a datos de personas.
     * Inyectado en el constructor (Dependency Injection).
     */
    private final EmpleadoDAO empleadoDAO;
    private final LegajoServiceImpl legajoServiceImpl;
    
    /**
     * Constructor con inyección de dependencias.
     * Valida que ambas dependencias no sean null (fail-fast).
     *
     * @param personaDAO DAO de personas (normalmente PersonaDAO)
     * @param domicilioServiceImpl Servicio de domicilios para operaciones coordinadas
     * @throws IllegalArgumentException si alguna dependencia es null
     */
    public EmpleadoServiceImpl(EmpleadoDAO empleadoDAO, LegajoServiceImpl legajoServiceImpl) {
        if (empleadoDAO == null) {
            throw new IllegalArgumentException("EmpleadoDAO no puede ser null");
        }
        if (legajoServiceImpl == null) {
            throw new IllegalArgumentException("LegajoServiceImpl no puede ser null");
        }
        this.empleadoDAO = empleadoDAO;
        this.legajoServiceImpl = legajoServiceImpl;
    }
    
    @Override
    public void insertar(Empleado empleado) throws Exception {
        validateEmpleado(empleado);
        validateDniUnique(empleado.getDni(), null);

        // Coordinación con DomicilioService (transaccional)
        if (empleado.getLegajo() != null) {
            if (empleado.getLegajo().getId() == 0) {
                // Domicilio nuevo: insertar primero para obtener ID autogenerado
                legajoServiceImpl.insertar(empleado.getLegajo());
            } else {
                // Domicilio existente: actualizar datos
                legajoServiceImpl.actualizar(empleado.getLegajo());
            }
        }

        empleadoDAO.insertar(empleado);
    }

    @Override
    public void actualizar(Empleado empleado) throws Exception {
        validateEmpleado(empleado);
        if (empleado.getId() <= 0) {
            throw new IllegalArgumentException("El ID del empleado debe ser mayor a 0 para actualizar");
        }
        validateDniUnique(empleado.getDni(), empleado.getId());
        empleadoDAO.actualizar(empleado);
    }

    /**
    * Elimina lógicamente un empleado (soft delete).
    * Marca el empleado como eliminado=TRUE sin borrarla físicamente.
    *
    * ⚠️ IMPORTANTE: Este método NO elimina el domicilio asociado (RN-037).
    * Si el empleado tiene un legajo, este quedará activo en la BD.
    * Esto es correcto porque un legajo puede exisitr independientemente de un empleado
    *
    * @param id ID del empleado a eliminar
    * @throws Exception Si id <= 0 o no existe la persona
    */
    
    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        empleadoDAO.eliminar(id);
    }

    @Override
    public Empleado getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return empleadoDAO.getById(id);
    }

    @Override
    public List<Empleado> getAll() throws Exception {
      return empleadoDAO.getAll();
    }
    
    public LegajoServiceImpl getLegajoService() {
        return this.legajoServiceImpl;
    }
    
    private void validateEmpleado(Empleado empleado) {
        if (empleado == null) {
            throw new IllegalArgumentException("La persona no puede ser null");
        }
        if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (empleado.getDni() == null || empleado.getDni().trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }
    }

    private void validateDniUnique(String dni, Integer empleadoId) throws Exception {
        Empleado existente = empleadoDAO.buscarPorDni(dni);
        if (existente != null) {
            // Existe una persona con ese DNI
            if (empleadoId == null || existente.getId() != empleadoId) {
                // Es INSERT (personaId == null) o es UPDATE pero el DNI pertenece a otra persona
                throw new IllegalArgumentException("Ya existe un empleado con el DNI: " + dni);
            }
            // Si llegamos aquí: es UPDATE y el DNI pertenece a la misma persona → OK
        }
    }    
}
