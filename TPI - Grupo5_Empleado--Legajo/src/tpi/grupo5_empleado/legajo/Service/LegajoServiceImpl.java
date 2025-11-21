/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi.grupo5_empleado.legajo.Service;

import java.util.List;
import tpi.grupo5_empleado.legajo.Dao.GenericDAO;
import tpi.grupo5_empleado.legajo.Models.Legajo;

public class LegajoServiceImpl implements GenericService<Legajo>{

    private final GenericDAO<Legajo> legajoDAO;
    
    public LegajoServiceImpl(GenericDAO<Legajo> legajoDAO) {
        if (legajoDAO == null) {
            throw new IllegalArgumentException("DomicilioDAO no puede ser null");
        }
        this.legajoDAO = legajoDAO;
    }
    
    @Override
    public void insertar(Legajo legajo) throws Exception {
        validateLegajo(legajo);
        legajoDAO.insertar(legajo);
    }

    @Override
    public void actualizar(Legajo legajo) throws Exception {
        validateLegajo(legajo);
        if (legajo.getId() <= 0) {
            throw new IllegalArgumentException("El ID del legajo debe ser mayor a 0 para actualizar");
        }
        legajoDAO.actualizar(legajo);
    }

    @Override
    public void eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        legajoDAO.eliminar(id);
    }

    @Override
    public Legajo getById(int id) throws Exception {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser mayor a 0");
        }
        return legajoDAO.getById(id);    
    }

    @Override
    public List<Legajo> getAll() throws Exception {
        return legajoDAO.getAll();
    }
    
    private void validateLegajo(Legajo legajo) {
        if (legajo == null) {
            throw new IllegalArgumentException("El legajo no puede ser null");
        }
        if (legajo.getNroLegajo()== null || legajo.getNroLegajo().trim().isEmpty()) {
            throw new IllegalArgumentException("El nro_legajo no puede estar vacio");
        }
        if (legajo.getEstado() == null || legajo.getEstado().trim().isEmpty()) {
            throw new IllegalArgumentException("El estado no puede estar vacio");
        }
    }
}
