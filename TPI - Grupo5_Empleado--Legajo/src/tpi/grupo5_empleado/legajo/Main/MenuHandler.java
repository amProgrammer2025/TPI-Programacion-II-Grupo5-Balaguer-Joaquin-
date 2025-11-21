/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi.grupo5_empleado.legajo.Main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import tpi.grupo5_empleado.legajo.Models.Empleado;
import tpi.grupo5_empleado.legajo.Models.Legajo;
import tpi.grupo5_empleado.legajo.Service.EmpleadoServiceImpl;

public class MenuHandler {
 
    private final Scanner scanner;

    private final EmpleadoServiceImpl empleadoService;

    public MenuHandler(Scanner scanner, EmpleadoServiceImpl empleadoService) {
        if (scanner == null) {
            throw new IllegalArgumentException("Scanner no puede ser null");
        }
        if (empleadoService == null) {
            throw new IllegalArgumentException("EmpleadoService no puede ser null");
        }
        this.scanner = scanner;
        this.empleadoService = empleadoService;
    }

    public void crearEmpleado() {
        
        try {
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            System.out.print("DNI: ");
            String dni = scanner.nextLine().trim();
            System.out.print("Email: ");
            String email = scanner.nextLine().trim();
            
            System.out.print("Fecha ingreso: ");
            String fechaIngresada = scanner.nextLine().trim();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaIngreso = LocalDate.parse(fechaIngresada, formatter);
            
            System.out.print("Area: ");
            String area = scanner.nextLine().trim();

            Legajo legajo = crearLegajo();

            Empleado empleado = new Empleado(nombre, dni, email, fechaIngreso, area, legajo, 0);
            empleadoService.insertar(empleado);
            System.out.println("Empleado creado exitosamente con ID: " + empleado.getId());
        } catch (Exception e) {
            System.err.println("Error al crear empleado: " + e.getMessage());
        }
    }

    public void listarEmpleados() {
        try {
            System.out.print("¿Desea (1) listar todos o (2) buscar por ID? Ingrese opcion: ");
            int subopcion = Integer.parseInt(scanner.nextLine());

            List<Empleado> empleados = null;
            if (subopcion == 1) {
                empleados = empleadoService.getAll();
            } else if (subopcion == 2) {
                System.out.print("Ingrese ID a buscar: ");
                int filtro = Integer.parseInt(scanner.nextLine().trim());
                empleados.add(empleadoService.getById(filtro));
            } else {
                System.out.println("Opcion invalida.");
                return;
            }

            if (empleados.isEmpty()) {
                System.out.println("No se encontraron empleados.");
                return;
            }
            
            for (Empleado e: empleados) {
                System.out.println("ID: " + e.getId() + ", Nombre: " + e.getNombre() +
                        ", DNI: " + e.getDni() + ", Email: " + e.getEmail() + 
                        ", Fecha de ingreso: " + e.getFechaIngreso() + ", Area: " + e.getArea() +
                        ", Legajo: " + e.getLegajo());
            }
        } catch (Exception e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
        }
    }

    public void actualizarEmpleado() {
        try {
            System.out.print("ID del empleado a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Empleado e = empleadoService.getById(id);

            if (e == null) {
                System.out.println("Empleado no encontrada.");
                return;
            }

            System.out.print("Nuevo nombre (actual: " + e.getNombre() + ", Enter para mantener): ");
            String nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) {
                e.setNombre(nombre);
            }

            System.out.print("Nuevo DNI (actual: " + e.getDni() + ", Enter para mantener): ");
            String dni = scanner.nextLine().trim();
            if (!dni.isEmpty()) {
                e.setDni(dni);
            }

            System.out.print("Nuevo email (actual: " + e.getEmail() + ", Enter para mantener): ");
            String email = scanner.nextLine().trim();
            if (!email.isEmpty()) {
                e.setEmail(email);
            }

            System.out.print("Nueva fecha de ingreso (actual: " + e.getFechaIngreso() + ", Enter para mantener): ");
            String fechaIngresada = scanner.nextLine().trim();
            
            if (!fechaIngresada.isEmpty()) {
                            
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaIngreso = LocalDate.parse(fechaIngresada, formatter);
                e.setFechaIngreso(fechaIngreso);
                
            }

            System.out.print("Nueva area (actual: " + e.getArea() + ", Enter para mantener): ");
            String area = scanner.nextLine().trim();
            if (!area.isEmpty()) {
                e.setArea(area);
            }

            actualizarLegajoDeEmpleado(e);
            empleadoService.actualizar(e);
            
            System.out.println("Persona actualizada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar persona: " + e.getMessage());
        }
    }

    public void eliminarEmpleado() {
        try {
            System.out.print("ID del empleado a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            empleadoService.eliminar(id);
            System.out.println("Persona eliminada exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar persona: " + e.getMessage());
        }
    }

    public void crearLegajoIndependiente() {
        try {
            Legajo legajo = crearLegajo();
            empleadoService.getLegajoService().insertar(legajo);
            System.out.println("Legakp creado exitosamente con ID: " + legajo.getId());
        } catch (Exception e) {
            System.err.println("Error al crear legajo: " + e.getMessage());
        }
    }

    public void listarLegajos() {
        try {
            List<Legajo> legajos = empleadoService.getLegajoService().getAll();
            if (legajos.isEmpty()) {
                System.out.println("No se encontraron domicilios.");
                return;
            }
            for (Legajo l : legajos) {
                System.out.println(l);
            }
        } catch (Exception e) {
            System.err.println("Error al listar legajos: " + e.getMessage());
        }
    }

    public void actualizarLegajoPorId() {
        try {
            System.out.print("ID del legajo a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Legajo l = empleadoService.getLegajoService().getById(id);

            if (l == null) {
                System.out.println("Legajo no encontrado.");
                return;
            }
            
            /*
                enum Estado{
                    ACTIVO,
                    INACTIVO
                }
                private String nroLegajo;
                private String categoria;
                private String estado;
                private LocalDate fechaAlta;
                private String observaciones;
            */

            System.out.print("Nuevo nro_legajo (actual: " + l.getNroLegajo() + ", Enter para mantener): ");
            String nroLegajo = scanner.nextLine().trim();
            if (!nroLegajo.isEmpty()) {
                l.setNroLegajo(nroLegajo);
            }

            System.out.print("Nueva categoria (actual: " + l.getCategoria() + ", Enter para mantener): ");
            String categoria = scanner.nextLine().trim();
            if (!categoria.isEmpty()) {
                l.setCategoria(categoria);
            }
            
            System.out.print("Nuevo estado (actual: " + l.getEstado() + ", Enter para mantener): ");
            String estado = scanner.nextLine().trim();
            if (!estado.isEmpty()) {
                l.setEstado(estado);
            }
            
            System.out.print("Nueva fecha de alta (actual: " + l.getFechaAlta()+ ", Enter para mantener): ");
            String fechaIngresada = scanner.nextLine().trim();
            
            if (!fechaIngresada.isEmpty()) {
                            
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaAlta = LocalDate.parse(fechaIngresada, formatter);
                l.setFechaAlta(fechaAlta);
                
            }

            empleadoService.getLegajoService().actualizar(l);
            System.out.println("Legajo actualizado exitosamente.");
            
        } catch (Exception e) {
            System.err.println("Error al actualizar legajo: " + e.getMessage());
        }
    }

    public void eliminarLegajoPorId() {
        try {
            System.out.print("ID del legajo a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());
            empleadoService.getLegajoService().eliminar(id);
            System.out.println("Legajo eliminado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar legajo: " + e.getMessage());
        }
    }

    public void actualizarLegajoPorEmpleado() {
        try {
            System.out.print("ID del empleado cuyo legajo desea actualizar: ");
            int empleadoId = Integer.parseInt(scanner.nextLine());
            Empleado e = empleadoService.getById(empleadoId);

            if (e == null) {
                System.out.println("Empleado no encontrada.");
                return;
            }

            if (e.getLegajo() == null) {
                System.out.println("La persona no tiene legajo asociado.");
                return;
            }

            Legajo l = e.getLegajo();
            System.out.print("Nuevo nro_legajo (actual: " + l.getNroLegajo() + ", Enter para mantener): ");
            String nroLegajo = scanner.nextLine().trim();
            if (!nroLegajo.isEmpty()) {
                l.setNroLegajo(nroLegajo);
            }

            System.out.print("Nueva categoria (actual: " + l.getCategoria() + ", Enter para mantener): ");
            String categoria = scanner.nextLine().trim();
            if (!categoria.isEmpty()) {
                l.setCategoria(categoria);
            }
            
            System.out.print("Nuevo estado (actual: " + l.getEstado() + ", Enter para mantener): ");
            String estado = scanner.nextLine().trim();
            if (!estado.isEmpty()) {
                l.setEstado(estado);
            }
            
            System.out.print("Nueva fecha de alta (actual: " + l.getFechaAlta()+ ", Enter para mantener): ");
            String fechaIngresada = scanner.nextLine().trim();
            
            if (!fechaIngresada.isEmpty()) {
                            
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaAlta = LocalDate.parse(fechaIngresada, formatter);
                l.setFechaAlta(fechaAlta);
                
            }

            empleadoService.getLegajoService().actualizar(l);
            System.out.println("Legajo actualizado exitosamente.");
            
        } catch (Exception e) {
            System.err.println("Error al actualizar legajo: " + e.getMessage());
        }
    }
    
    private Legajo crearLegajo() {

            System.out.print("Nro_legajo: ");
            String nroLegajo = scanner.nextLine().trim();

            System.out.print("Categoria: ");
            String categoria = scanner.nextLine().trim();
            
            System.out.print("Estado: ");
            String estado = scanner.nextLine().trim();
            
            System.out.print("Fecha de alta: ");
            String fechaIngresada = scanner.nextLine().trim();
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaAlta = LocalDate.parse(fechaIngresada, formatter);
            
            System.out.print("Observaciones: ");
            String observaciones = scanner.nextLine().trim();
            
            return new Legajo(nroLegajo, categoria, estado, fechaAlta, observaciones, 0);

    }

    private void actualizarLegajoDeEmpleado(Empleado e) throws Exception {
        if (e.getLegajo() != null) {
            System.out.print("¿Desea actualizar el legajo? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                
                System.out.print("Nuevo nro_legajo (actual: " + e.getLegajo().getNroLegajo() + ", Enter para mantener): ");
                String nroLegajo = scanner.nextLine().trim();
                if (!nroLegajo.isEmpty()) {
                    e.getLegajo().setNroLegajo(nroLegajo);
                }

                System.out.print("Nueva categoria (actual: " + e.getLegajo().getCategoria() + ", Enter para mantener): ");
                String categoria = scanner.nextLine().trim();
                if (!categoria.isEmpty()) {
                    e.getLegajo().setCategoria(categoria);
                }

                System.out.print("Nuevo estado (actual: " + e.getLegajo().getEstado() + ", Enter para mantener): ");
                String estado = scanner.nextLine().trim();
                if (!estado.isEmpty()) {
                    e.getLegajo().setEstado(estado);
                }

                System.out.print("Nueva fecha de alta (actual: " + e.getLegajo().getFechaAlta()+ ", Enter para mantener): ");
                String fechaIngresada = scanner.nextLine().trim();

                if (!fechaIngresada.isEmpty()) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fechaAlta = LocalDate.parse(fechaIngresada, formatter);
                    e.getLegajo().setFechaAlta(fechaAlta);

                }

                empleadoService.getLegajoService().actualizar(e.getLegajo());
                System.out.println("Legajo actualizado exitosamente.");
                
            }
        } else {
            
            System.out.print("El empleado no tiene legajo. ¿Desea agregar uno? (s/n): ");
            if (scanner.nextLine().equalsIgnoreCase("s")) {
                Legajo nuevoLeg = crearLegajo();
                empleadoService.getLegajoService().insertar(nuevoLeg);
                e.setLegajo(nuevoLeg);
            }
            
        }
    }
}
