/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi.grupo5_empleado.legajo.Models;

import java.time.LocalDate;

public class Empleado extends Base{

    private String nombre;
    private String dni;
    private String email;
    private LocalDate fechaIngreso;
    private String area;
    private Legajo legajo;

    public Empleado(String nombre, String dni, String email, LocalDate fechaIngreso, String area, Legajo legajo, int id) {
        super(id, false);
        this.nombre = nombre;
        this.dni = dni;
        this.email = email;
        this.fechaIngreso = fechaIngreso;
        this.area = area;
        this.legajo = legajo;
    }

    public Empleado() {
        super();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Legajo getLegajo() {
        return legajo;
    }

    public void setLegajo(Legajo legajo) {
        this.legajo = legajo;
    }

    @Override
    public String toString() {
        return "Empleado{" + "nombre=" + nombre 
                + ", dni=" + dni 
                + ", email=" + email 
                + ", fechaIngreso=" + fechaIngreso
                + ", area=" + area 
                + ", legajo=" + legajo + '}';
    }

}
