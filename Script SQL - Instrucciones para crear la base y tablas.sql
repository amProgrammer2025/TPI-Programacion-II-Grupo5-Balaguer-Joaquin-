DROP DATABASE IF EXISTS dbtpi;
CREATE DATABASE dbtpi;
USE dbtpi;

-- Creamos la tabla correspondiente a la entidad Empleado
CREATE TABLE IF NOT EXISTS Empleado(
	id bigint PRIMARY KEY AUTO_INCREMENT,
    eliminado boolean DEFAULT false,
    nombre varchar(80) NOT NULL,
    dni varchar(15) UNIQUE NOT NULL,
    email varchar(120),
    fecha_ingreso date,
    area varchar(50)
);

-- Creamos la tabla correspondiente a la entidad Legajo
CREATE TABLE IF NOT EXISTS Legajo(
	id bigint PRIMARY KEY AUTO_INCREMENT,
    eliminado boolean DEFAULT false,
    nro_legajo varchar(20) UNIQUE NOT NULL,
    categoria varchar(30),
    estado enum("ACTIVO", "INACTIVO") NOT NULL,
    fecha_alta date,
    observaciones text(255),
    id_empleado bigint UNIQUE NOT NULL,
    FOREIGN KEY (id_empleado) REFERENCES Empleado(id)
);