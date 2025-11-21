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
import java.util.ArrayList;
import java.util.List;
import tpi.grupo5_empleado.legajo.Config.DatabaseConnection;
import tpi.grupo5_empleado.legajo.Models.Legajo;

public class LegajoDAO implements GenericDAO<Legajo>{
    
    private static final String INSERT_SQL = "INSERT INTO legajo (nro_legajo, categoria, estado, fecha_alta, observaciones) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE legajo SET nro_legajo = ?, categoria = ?, estado = ?, fecha_alta = ?, observaciones = ? WHERE id = ?";
    private static final String DELETE_SQL = "UPDATE legajo SET eliminado = TRUE WHERE id = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM legajo WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM legajo";
    
    @Override
    public void insertar(Legajo legajo) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setLegajoParameters(stmt, legajo);
            stmt.executeUpdate();
            setGeneratedId(stmt, legajo);
        }
    }

    @Override
    public void insertTx(Legajo legajo, Connection conn) throws Exception {
        try(PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){
            setLegajoParameters(stmt, legajo);
            stmt.executeUpdate();
            setGeneratedId(stmt, legajo);
        }
    }

    @Override
    public void actualizar(Legajo legajo) throws Exception {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)){
            
            stmt.setString(1, legajo.getNroLegajo());
            stmt.setString(2, legajo.getCategoria());
            stmt.setString(3, legajo.getEstado());
            stmt.setObject(4, legajo.getFechaAlta());
            stmt.setString(5, legajo.getObservaciones());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar el legajo con ID: " + legajo.getId());
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
                throw new SQLException("No se encontró legajo con ID: " + id);
            }
        }
    }

    @Override
    public Legajo getById(int id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToLegajo(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener persona por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Legajo> getAll() throws Exception {
        
        List<Legajo> legajos = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {

            while (rs.next()) {
                legajos.add(mapResultSetToLegajo(rs));
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener todas las personas: " + e.getMessage(), e);
        }
        return legajos;
        
    }
    
    private void setLegajoParameters(PreparedStatement stmt, Legajo legajo) throws SQLException{
        
        stmt.setString(1, legajo.getNroLegajo());
        stmt.setString(2, legajo.getCategoria());
        stmt.setString(3, legajo.getEstado());
        stmt.setObject(4, legajo.getFechaAlta());
        stmt.setString(5, legajo.getObservaciones());
        
    }

    private void setGeneratedId(PreparedStatement stmt, Legajo legajo) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                legajo.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("La inserción del legajo falló, no se obtuvo ID generado");
            }
        }
    }
    
    private Legajo mapResultSetToLegajo(ResultSet rs) throws SQLException{
        
        Legajo legajo = new Legajo();
        
        legajo.setId(rs.getInt("id"));
        legajo.setNroLegajo(rs.getString("nro_legajo"));
        legajo.setCategoria(rs.getString("categoria"));
        legajo.setEstado(rs.getString("estado"));
        legajo.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());
        legajo.setObservaciones(rs.getString("observaciones"));
        
        return legajo;
        
    }
    
}
