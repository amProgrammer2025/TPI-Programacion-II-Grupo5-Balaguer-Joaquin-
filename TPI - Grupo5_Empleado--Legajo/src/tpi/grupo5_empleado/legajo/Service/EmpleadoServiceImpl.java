/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi.grupo5_empleado.legajo.Service;

import java.util.List;
import tpi.grupo5_empleado.legajo.Dao.EmpleadoDAO;
import tpi.grupo5_empleado.legajo.Models.Empleado;

public class EmpleadoServiceImpl implements GenericService<Empleado>{

    private final EmpleadoDAO empleadoDAO;
    private final LegajoServiceImpl legajoServiceImpl;
    
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
        
        if (empleado.getLegajo() != null) {
            if (empleado.getLegajo().getId() == 0) {
                legajoServiceImpl.insertar(empleado.getLegajo());
            } else {
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
            if (empleadoId == null || existente.getId() != empleadoId) {
                throw new IllegalArgumentException("Ya existe un empleado con el DNI: " + dni);
            }
        }
    }    
}
