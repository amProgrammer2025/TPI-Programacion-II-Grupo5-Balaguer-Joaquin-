/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi.grupo5_empleado.legajo.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import tpi.grupo5_empleado.legajo.Config.DatabaseConnection;
import tpi.grupo5_empleado.legajo.Models.Empleado;

/**
 *
 * @author gauta
 */
public class EmpleadoDAO implements GenericDAO<Empleado>{

    private static final String INSERT_SQL = "INSERT INTO empleado (nombre, dni, email, fecha_ingreso, area) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE empleado SET nombre = ?, dni = ?, email = ?, fecha_ingreso = ?, area = ? WHERE id = ?";
    private static final String DELETE_SQL = "UPDATE empleado SET eliminado = TRUE WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM empleado WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM empleado";
    private static final String SEARCH_BY_DNI_SQL = "SELECT * FROM empleado WHERE eliminado = FALSE AND dni = ?";
    
    
    private final LegajoDAO legajoDAO;
    
    public EmpleadoDAO(LegajoDAO legajoDAO) {
        if (legajoDAO == null) {
            throw new IllegalArgumentException("DomicilioDAO no puede ser null");
        }
        this.legajoDAO = legajoDAO;
    }
    
    @Override
    public void insertar(Empleado empleado) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setEmpleadoParameters(stmt, empleado);
            stmt.executeUpdate();
            setGeneratedId(stmt, empleado);
        }
    }

    @Override
    public void insertTx(Empleado empleado, Connection conn) throws Exception {
        try(PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){
            setEmpleadoParameters(stmt, empleado);
            stmt.executeUpdate();
            setGeneratedId(stmt, empleado);
        }
    }
    
    @Override
    public void actualizar(Empleado empleado) throws Exception {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)){
            
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getDni());
            stmt.setString(3, empleado.getEmail());
            stmt.setObject(4, empleado.getFechaIngreso());
            stmt.setString(5, empleado.getArea());
            stmt.setInt(6, empleado.getId());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar el empleado con ID: " + empleado.getId());
            }
            
        }
    }

    @Override
    public void eliminar(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {

            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                throw new SQLException("No se encontró empleado con ID: " + id);
            }
        }
    }

    @Override
    public Empleado getById(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmpleado(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener persona por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Empleado> getAll() throws Exception {
        
        List<Empleado> empleados = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                empleados.add(mapResultSetToEmpleado(rs));
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener todas las personas: " + e.getMessage(), e);
        }
        return empleados;
    }
    
    public Empleado buscarPorDni(String dni) throws SQLException {
        if (dni == null || dni.trim().isEmpty()) {
            throw new IllegalArgumentException("El DNI no puede estar vacío");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_DNI_SQL)) {

            stmt.setString(1, dni.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmpleado(rs);
                }
            }
        }
        return null;
    }
    
    private void setEmpleadoParameters(PreparedStatement stmt, Empleado empleado) throws SQLException{
        
        stmt.setString(1, empleado.getNombre());
        stmt.setString(2, empleado.getDni());
        stmt.setString(3, empleado.getEmail());
        stmt.setObject(4, empleado.getFechaIngreso());
        stmt.setString(5, empleado.getArea());
        
    }

    private void setGeneratedId(PreparedStatement stmt, Empleado empleado) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                empleado.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La inserción del empleado falló, no se obtuvo ID generado");
            }
        }
    }
    
    private Empleado mapResultSetToEmpleado(ResultSet rs) throws SQLException{
        
        Empleado empleado = new Empleado();
        empleado.setId(rs.getInt("id"));
        empleado.setNombre(rs.getString("nombre"));
        empleado.setDni(rs.getString("dni"));
        empleado.setEmail(rs.getString("email"));
        empleado.setFechaIngreso(rs.getDate("fecha_ingreso").toLocalDate());
        empleado.setArea(rs.getString("area"));
        
        return empleado;
        
    }
}
