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
	experiencia ENUM ('Junior','Semi_Senior','Senior') NOT NULL,
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
	transaccion_id INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
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
	estado ENUM('PENDIENTE','ACEPTADA','RECHAZADA', 'RETIRADO') NOT NULL DEFAULT 'PENDIENTE',
	fecha_creacion DATE NOT NULL,
	CONSTRAINT fk_proyecto2 FOREIGN KEY (proyecto_id) REFERENCES proyecto (proyecto_id),
	CONSTRAINT fk_freelancer2 FOREIGN KEY (usuario_id) REFERENCES freelancer (usuario_id)
);

CREATE TABLE IF NOT EXISTS contrato (
	contrato_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	propuesta_id INT NOT NULL UNIQUE,
	estado ENUM('ACTIVO', 'FINALIZADO', 'CANCELADO') NOT NULL DEFAULT 'ACTIVO',
	motivo_cancelacion VARCHAR(250),
	fecha_creacion DATE NOT NULL,
	fecha_finalizacion DATE,
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


SELECT * FROM usuario WHERE (user_name = 'Admin' OR email = 'Admin') AND password = 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3' AND estado = 1;

SELECT u.nombre_completo, u.user_name,   c.*, f.* FROM usuario u LEFT JOIN cliente c ON u.usuario_id = c.usuario_id LEFT JOIN freelancer f ON u.usuario_id = f.usuario_id;

SELECT * FROM cartera where usuario_id = 2;
SELECT * FROM transaccion where usuario_id = 2;

SELECT * FROM usuario;
SELECT * FROM cliente;
SELECT * FROM freelancer;
SELECT * FROM categoria;

SELECT * FROM habilidad;
SELECT * FROM proyecto_habilidad;
SELECT * FROM freelancer_habilidad;

SELECT h.* FROM freelancer_habilidad fh JOIN habilidad h ON fh.habilidad_id = h.habilidad_id WHERE fh.usuario_id = 3;

SELECT * FROM categoria;

SELECT p.*, c.nombre, c.descripcion, c.estado FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id;
SELECT h.* FROM proyecto_habilidad ph JOIN habilidad h ON ph.habilidad_id = h.habilidad_id WHERE ph.proyecto_id = 1; 
SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE proyecto_id = 12 AND usuario_id = 5

SELECT h.* FROM proyecto_habilidad ph JOIN habilidad h ON ph.habilidad_id = h.habilidad_id WHERE ph.proyecto_id = ?;
SELECT p.* FROM proyecto_habilidad ph JOIN proyecto p ON ph.proyecto_id = p.proyecto_id WHERE ph.habilidad_id = 2;
SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id JOIN proyecto_habilidad ph ON p.proyecto_id = ph.proyecto_id WHERE ph.habilidad_id = 2;
SELECT p.*, c.nombre, c.descripcion, c.estado AS estado_categoria FROM proyecto p JOIN categoria c ON p.categoria_id = c.categoria_id WHERE p.estado = 'ABIERTO';

INSERT INTO configuracion_sistema (comision, fecha_inicio) VALUES (0.15, '2004-01-01');
INSERT INTO configuracion_sistema (comision, fecha_inicio) VALUES (0.1, '2004-02-01');
UPDATE configuracion_sistema SET fecha_fin = '2004-02-01' WHERE configuracion_id = 2;

SELECT p.propuesta_id, p.proyecto_id, p.monto, p.tiempo_entrega, p.descripcion, p.estado, p.fecha_creacion, u.usuario_id, u.nombre_completo, u.user_name, COALESCE(cal.promedio_calificacion, 0) AS promedio_calificacion, COALESCE(cal.total_calificaciones, 0) AS total_calificaciones FROM propuesta p INNER JOIN usuario u ON p.usuario_id = u.usuario_id LEFT JOIN (SELECT p2.usuario_id, COUNT(c.calificacion) AS total_calificaciones, AVG(c.calificacion) AS promedio_calificacion FROM propuesta p2 INNER JOIN contrato c ON p2.propuesta_id = c.propuesta_id GROUP BY p2.usuario_id) cal ON p.usuario_id = cal.usuario_id JOIN proyecto p3 on p.proyecto_id = p3.proyecto_id WHERE p.proyecto_id = ? AND p3.usuario_id = ?;


SELECT * FROM propuesta WHERE propuesta_id = 2 AND usuario_id = 3 ;

SELECT pr.proyecto_id  FROM propuesta p JOIN proyecto pr ON p.proyecto_id = pr.proyecto_id WHERE p.propuesta_id = 3 AND pr.usuario_id = 2; 
SELECT c.*, pr.titulo FROM contrato c JOIN propuesta p on c.propuesta_id = p.propuesta_id JOIN proyecto pr on p.proyecto_id = pr.proyecto_id WHERE c.propuesta_id  = ?;
SELECT c.*, pr.titulo FROM contrato c JOIN propuesta p on c.propuesta_id = p.propuesta_id JOIN proyecto pr on p.proyecto_id = pr.proyecto_id WHERE c.contrato_id = 1;

SELECT * FROM proyecto;
SELECT * FROM propuesta;
SELECT * FROM contrato;
SELECT c.* FROM contrato c JOIN propuesta p on c.propuesta_id = p.propuesta_id JOIN proyecto p2 ON p.proyecto_id = p2.proyecto_id WHERE p2.usuario_id = 2;
SELECT pr.proyecto_id FROM contrato c JOIN propuesta p on c.propuesta_id = p.propuesta_id JOIN proyecto pr on p.proyecto_id = pr.proyecto_id WHERE pr.usuario_id = 2 AND c.contrato_id = 3;
SELECT pr.proyecto_id FROM contrato c JOIN propuesta p ON c.propuesta_id = p.propuesta_id JOIN proyecto pr ON p.proyecto_id = pr.proyecto_id WHERE c.contrato_id = 3;

SELECT p.proyecto_id FROM entrega e JOIN contrato c ON e.contrato_id = c.contrato_id JOIN propuesta p ON c.propuesta_id = p.propuesta_id WHERE e.entrega_id = 1;
SELECT p.proyecto_id FROM contrato c JOIN propuesta p ON c.propuesta_id = p.propuesta_id WHERE c.contrato_id = 1;
SELECT c.propuesta_id FROM entrega e JOIN contrato c ON e.contrato_id = c.contrato_id WHERE e.entrega_id  = 1;
SELECT contrato_id FROM entrega WHERE entrega_id = 1;

select * from entrega;
select * from contrato;
select * from propuesta;

SELECT p.propuesta_id FROM entrega e JOIN contrato c ON e.contrato_id = c.contrato_id JOIN propuesta p ON c.propuesta_id = p.propuesta_id WHERE e.contrato_id = 1;
SELECT c.contrato_id FROM entrega e JOIN contrato c ON e.contrato_id = c.contrato_id WHERE e.

SELECT * FROM cartera_plataforma ORDER BY plataforma_id DESC LIMIT 1;

SELECT * FROM configuracion_sistema ORDER BY configuracion_id DESC LIMIT 1;
SELECT configuracion_id FROM configuracion_sistema ORDER BY configuracion_id DESC LIMIT 1;
SELECT * FROM configuracion_sistema;

SELECT h.* FROM habilidad h LEFT JOIN freelancer_habilidad fh ON h.habilidad_id = fh.habilidad_id AND fh.usuario_id = ? WHERE fh.habilidad_id IS NULL AND h.estado = 1;








