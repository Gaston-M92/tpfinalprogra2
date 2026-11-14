-- Sistema de gestion de turnos para consultorios ambulatorios
-- Script de creacion de la base de datos

CREATE DATABASE IF NOT EXISTS turnos_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_spanish_ci;

USE turnos_db;

-- Tabla pacientes
-- Guarda los atributos heredados de Persona + los propios de Paciente

CREATE TABLE IF NOT EXISTS pacientes (
dni VARCHAR(10) NOT NULL,
nombre VARCHAR(50) NOT NULL,
apellido VARCHAR(50) NOT NULL,
telefono VARCHAR(20),
historia_clinica INT NOT NULL,
obra_social VARCHAR(50),
fecha_nacimiento DATE NOT NULL,
PRIMARY KEY (dni),
UNIQUE (historia_clinica)
);

-- Tabla medicos
-- Guarda los atributos heredados de Persona + los propios de Medico
-- Los horaios de atencion se manejan en memoria

CREATE TABLE IF NOT EXISTS medicos (
dni VARCHAR(10) NOT NULL,
nombre VARCHAR(50) NOT NULL,
apellido VARCHAR(50) NOT NULL,
telefono VARCHAR(20),
matricula VARCHAR(20) NOT NULL,
especialidad VARCHAR(30) NOT NULL,
duracion_turno_min INT NOT NULL,
PRIMARY KEY (dni),
UNIQUE (matricula)
);

-- Tabla turnos
-- Relaciona un paciente con un medico en una fecha y hora

CREATE TABLE IF NOT EXISTS turnos (
id_turno VARCHAR(20) NOT NULL,
dni_paciente VARCHAR(10) NOT NULL,
dni_medico VARCHAR(10) NOT NULL,
fecha_hora DATETIME NOT NULL,
motivo_consulta VARCHAR(200),
estado VARCHAR(20) NOT NULL,
PRIMARY KEY (id_turno),
FOREIGN KEY (dni_paciente) REFERENCES pacientes(dni),
FOREIGN KEY (dni_medico) REFERENCES medicos(dni)
);

-- Datos de prueba

INSERT INTO pacientes (dni, nombre, apellido, telefono, historia_clinica, obra_social, fecha_nacimiento) VALUES
('30111222', 'Laura',  'Gómez',     '2234567890', 1001, 'OSDE',      '1985-03-12'),
('35444555', 'Martín', 'Pérez',     '2235551234', 1002, 'IOMA',      '1992-07-25'),
('40777888', 'Sofía',  'Rodríguez', '2239876543', 1003, 'Particular','2001-11-03');

INSERT INTO medicos (dni, nombre, apellido, telefono, matricula, especialidad, duracion_turno_min) VALUES
('20123456', 'Carlos', 'Fernández', '2231112233', 'MP-4521', 'CLINICA_MEDICA', 20),
('25987654', 'Ana',    'López',     '2234445566', 'MP-7834', 'PEDIATRIA',      30),
('28456789', 'Jorge',  'Martínez',  '2237778899', 'MP-3390', 'CARDIOLOGIA',    30);

INSERT INTO turnos (id_turno, dni_paciente, dni_medico, fecha_hora, motivo_consulta, estado) VALUES
('T001', '30111222', '20123456', '2026-11-16 09:00:00', 'Control anual',          'RESERVADO'),
('T002', '35444555', '20123456', '2026-11-16 09:20:00', 'Dolor de garganta',      'RESERVADO'),
('T003', '40777888', '28456789', '2026-11-17 10:00:00', 'Electrocardiograma',     'ATENDIDO'),
('T004', '30111222', '25987654', '2026-11-18 11:30:00', 'Consulta por vacunas',   'CANCELADO');