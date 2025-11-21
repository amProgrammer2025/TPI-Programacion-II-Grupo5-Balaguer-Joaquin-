/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tpi.grupo5_empleado.legajo.Main;

import java.util.Scanner;
import tpi.grupo5_empleado.legajo.Dao.EmpleadoDAO;
import tpi.grupo5_empleado.legajo.Dao.LegajoDAO;
import tpi.grupo5_empleado.legajo.Service.EmpleadoServiceImpl;
import tpi.grupo5_empleado.legajo.Service.LegajoServiceImpl;

/**
 *
 * @author gauta
 */
public class AppMenu {
    
    private final Scanner scanner;

    private final MenuHandler menuHandler;

    private boolean running;

    public AppMenu() {
        this.scanner = new Scanner(System.in);
        EmpleadoServiceImpl empleadoService = createEmpleadoService();
        this.menuHandler = new MenuHandler(scanner, empleadoService);
        this.running = true;
    }

    public static void main(String[] args) {
        AppMenu app = new AppMenu();
        app.run();
    }

    public void run() {
        while (running) {
            try {
                MenuDisplay.mostrarMenuPrincipal();
                int opcion = Integer.parseInt(scanner.nextLine());
                processOption(opcion);
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Por favor, ingrese un numero.");
            }
        }
        scanner.close();
    }

    private void processOption(int opcion) {
        switch (opcion) {
            case 1 -> menuHandler.crearEmpleado();
            case 2 -> menuHandler.listarEmpleados();
            case 3 -> menuHandler.actualizarEmpleado();
            case 4 -> menuHandler.eliminarEmpleado();
            case 5 -> menuHandler.crearLegajoIndependiente();
            case 6 -> menuHandler.listarLegajos();
            case 7 -> menuHandler.actualizarLegajoPorId();
            case 8 -> menuHandler.eliminarLegajoPorId();
            case 9 -> menuHandler.actualizarLegajoPorEmpleado();
            case 0 -> {
                System.out.println("Saliendo...");
                running = false;
            }
            default -> System.out.println("Opcion no valida.");
        }
    }

    private EmpleadoServiceImpl createEmpleadoService() {
        LegajoDAO legajoDAO = new LegajoDAO();
        EmpleadoDAO empleadoDAO = new EmpleadoDAO(legajoDAO);
        LegajoServiceImpl legajoService = new LegajoServiceImpl(legajoDAO);
        return new EmpleadoServiceImpl(empleadoDAO, legajoService);
    }
}
