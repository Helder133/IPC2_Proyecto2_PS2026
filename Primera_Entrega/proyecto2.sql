CREATE DATABASE IF NOT EXISTS Proyecto2 DEFAULT CHARACTER SET = 'utf8mb4';

USE Proyecto2;

CREATE TABLE IF NOT EXISTS usuario (
	usuario_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	nombre_completo VARCHAR(200) NOT NULL,
	user_name VARCHAR(200) NOT NULL UNIQUE,
	password VARCHAR(200) NOT NULL,
	email VARCHAR(200) NOT NULL UNIQUE,
	telefono VARCHAR(15) NOT NULL,
	direccion VARCHAR(200) NOT NULL,
	cui VARCHAR(20) NOT NULL,
	fecha_nacimiento DATE NOT NULL,
	rol ENUM ('Cliente','Freelancer','Administrador') NOT NULL,
	estado BOOL NOT NULL DEFAULT 1
);

-- password 123
INSERT INTO usuario (nombre_completo,user_name,password,email,telefono,direccion,cui,fecha_nacimiento,rol) VALUES (
'Admin',
'admin',
'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3',
'admin@gmail.com',
'00000000',
'CUNOC',
'1234567890123',
'2004-08-07',
'Administrador');

SELECT * FROM usuario WHERE (user_name = 'Admin' OR email = 'Admin') AND password = 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3' AND estado = 1;

CREATE TABLE IF NOT EXISTS cliente (
	usuario_id INT PRIMARY KEY NOT NULL,
	descripcion VARCHAR(200) NOT NULL,
	sector VARCHAR(200) NOT NULL,
	sitio_web VARCHAR(200) NOT NULL,
	CONSTRAINT fk_cliente FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id)
);

CREATE TABLE IF NOT EXISTS freelancer (
	usuario_id INT PRIMARY KEY NOT NULL,
	descripcion VARCHAR(200) NOT NULL,
	experiencia ENUM ('Junior','Semi-Senior','Senior') NOT NULL,
	tarifa_hora DECIMAL(10,2),
	CONSTRAINT fk_freelancer FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id)
);

CREATE TABLE IF NOT EXISTS cartera (
	usuario_id INT PRIMARY KEY NOT NULL,
	saldo DECIMAL(10,2) NOT NULL DEFAULT 0.0,
	saldo_bloqueado DECIMAL(10,2) DEFAULT 0.0,
	CONSTRAINT fk_usuario FOREIGN KEY (usuario_id) REFERENCES usuario (usuario_id)
);

CREATE TABLE IF NOT EXISTS transaccion (
	transaccion INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	usuario_id INT NOT NULL,
	tipo ENUM ('Recarga','Bloqueo','Devolucion','Pago') NOT NULL DEFAULT 'Recarga',
	monto DECIMAL(10,2) NOT NULL DEFAULT 0.0,
	fecha DATE NOT NULL,
	CONSTRAINT fk_cartera FOREIGN KEY (usuario_id) REFERENCES cartera (usuario_id)
);

CREATE TABLE IF NOT EXISTS habilidad (
	habilidad_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	nombre VARCHAR(100) NOT NULL UNIQUE,
	descripcion VARCHAR(200) NOT NULL,
	estado BOOL NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS freelancer_habilidad (
	usuario_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	CONSTRAINT pk_freelancer_habilidad PRIMARY KEY (usuario_id, habilidad_id),
	CONSTRAINT fk_usuario2 FOREIGN KEY (usuario_id) REFERENCES freelancer (usuario_id),
	CONSTRAINT fk_habilidad FOREIGN KEY (habilidad_id) REFERENCES habilidad (habilidad_id)
);

CREATE TABLE IF NOT EXISTS categoria (
	categoria_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
	nombre VARCHAR(100) NOT NULL UNIQUE,
	descripcion VARCHAR(200) NOT NULL,
	estado BOOL NOT NULL DEFAULT 1
);

CREATE TABLE IF NOT EXISTS proyecto (
	proyecto_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	usuario_id INT NOT NULL,
	categoria_id INT NOT NULL,
	titulo VARCHAR(200) NOT NULL,
	descripcion VARCHAR(200) NOT NULL,
	presupuesto DECIMAL(10,2) NOT NULL,
	estado ENUM('ABIERTO','EN_REVISION','EN_PROGRESO','ENTREGA_PENDIENTE','COMPLETADO','CANCELADO') NOT NULL DEFAULT 'ABIERTO',
	fecha_creacion DATE,
	fecha_limite DATE,
	CONSTRAINT fk_cliente2 FOREIGN KEY (usuario_id) REFERENCES cliente (usuario_id),
	CONSTRAINT fk_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (categoria_id)
);

CREATE TABLE IF NOT EXISTS proyecto_habilidad (
	proyecto_id INT NOT NULL,
	habilidad_id INT NOT NULL,
	CONSTRAINT pk_proyecto_habilidad PRIMARY KEY (proyecto_id, habilidad_id),
	CONSTRAINT fk_proyecto FOREIGN KEY (proyecto_id) REFERENCES proyecto (proyecto_id),
	CONSTRAINT fk_habilidad2 FOREIGN KEY (habilidad_id) REFERENCES habilidad (habilidad_id)
);

CREATE TABLE IF NOT EXISTS propuesta (
	propuesta_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	proyecto_id INT NOT NULL,
	usuario_id INT NOT NULL,
	monto DECIMAL(10,2) NOT NULL,
	tiempo_entrega INT NOT NULL,
	descripcion VARCHAR(200) NOT NULL,
	estado ENUM('PENDIENTE','ACEPTADA','RECHAZADA') NOT NULL DEFAULT 'PENDIENTE',
	fecha_creacion DATE NOT NULL,
	CONSTRAINT fk_proyecto2 FOREIGN KEY (proyecto_id) REFERENCES proyecto (proyecto_id),
	CONSTRAINT fk_freelancer2 FOREIGN KEY (usuario_id) REFERENCES freelancer (usuario_id)
);

CREATE TABLE IF NOT EXISTS contrato (
	contrato_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	propuesta_id INT NOT NULL,
	comentario VARCHAR(200),
	calificacion INT CHECK (calificacion >= 1 AND calificacion <= 5),
	CONSTRAINT fk_propuesta FOREIGN KEY (propuesta_id) REFERENCES propuesta (propuesta_id)
);

CREATE TABLE IF NOT EXISTS entrega (
	entrega_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	contrato_id INT NOT NULL,
	descripcion VARCHAR(200) NOT NULL,
	archivo VARCHAR(200) NOT NULL,
	estado ENUM('PENDIENTE','APROBADA','RECHAZADA') NOT NULL DEFAULT 'PENDIENTE',
	motivo_rechazo VARCHAR(200),
	fecha DATE NOT NULL,
	CONSTRAINT fk_contrato FOREIGN KEY (contrato_id) REFERENCES contrato (contrato_id)
);

CREATE TABLE IF NOT EXISTS cartera_plataforma (
	plataforma_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	saldo DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS transaccion_plataforma (
	transaccion_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	plataforma_id INT NOT NULL,
	contrato_id INT NOT NULL,
	porcentaje_aplicado DECIMAL(10,2) NOT NULL,
	monto_comision DECIMAL(10,2) NOT NULL,
	fecha DATE NOT NULL,
	CONSTRAINT fk_plataforma FOREIGN KEY (plataforma_id) REFERENCES cartera_plataforma (plataforma_id),
	CONSTRAINT fk_contrato2 FOREIGN KEY (contrato_id) REFERENCES contrato (contrato_id)
);

CREATE TABLE IF NOT EXISTS nueva_h_c (
	solicitud_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	usuario_id INT NOT NULL,
	nombre VARCHAR(200) NOT NULL,
	descripcion VARCHAR(200) NOT NULL,
	tipo ENUM ('HABILIDAD','CATEGORIA') NOT NULL,
	estado ENUM('PENDIENTE','ACEPTADA','RECHAZADA') NOT NULL DEFAULT 'PENDIENTE',
	fecha_creacion DATE NOT NULL,
	CONSTRAINT fk_usuario3 FOREIGN KEY (usuario_id) REFERENCES usuario(usuario_id)
);

CREATE TABLE IF NOT EXISTS configuracion_sistema (
	configuracion_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	comision DECIMAL(10,2) NOT NULL,
	fecha_inicio DATE NOT NULL,
	fecha_fin DATE
);










