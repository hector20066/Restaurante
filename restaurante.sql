-- Script para la creacion de la base de datos

-- Create database
CREATE DATABASE IF NOT EXISTS restaurante

-- change database
USE restaurante

-- Create tables in database
CREATE TABLE bebidas(
    codigo INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    descripcion VARCHAR(300),
    precio FLOAT NOT NULL,
    tipo_bebida VARCHAR(20)
);

CREATE TABLE platos(
    codigo INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    descripcion VARCHAR(300),
    precio FLOAT NOT NULL,
    tipo_plato VARCHAR(20)
);

CREATE TABLE cliente(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    nombre VARCHAR(50),
    direccion VARCHAR(20),
    correo VARCHAR(70)
);

CREATE TABLE venta(
    num_venta INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    id_cliente INT NULL,
    fecha DATE,
    total DECIMAL(10,2),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE detalle_venta(
    idDetalle INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    idVenta INT NULL,
    idPlato INT NULL,
    idBebida INT NULL,
    cantPlato INT NULL,
    cantBebida INT NULL,
    precioPlato DECIMAL(10,2) NULL,
    precioBebida DECIMAL(10,2) NULL,
    FOREIGN KEY (idVenta) REFERENCES venta(num_venta) ON DELETE CASCADE,
    FOREIGN KEY (idPlato) REFERENCES platos(codigo) ON DELETE CASCADE,
    FOREIGN KEY (idBebida) REFERENCES bebidas(codigo) ON DELETE CASCADE
);

CREATE TABLE calificacion_plato(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    num_estrellas INT NOT NULL,
    comentario VARCHAR(500),
    idPlato INT NOT NULL,
    idCliente INT NOT NULL,
    fecha DATE,
    FOREIGN KEY (idCliente) REFERENCES cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (idPlato) REFERENCES platos(codigo) ON DELETE CASCADE
);

CREATE TABLE calificacion_bebida(
    id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    num_estrellas INT NOT NULL,
    comentario VARCHAR(500),
    idBebida INT NOT NULL,
    idCliente INT NOT NULL,
    fecha DATE,
    FOREIGN KEY(idBebida) REFERENCES bebidas(codigo) ON DELETE CASCADE,
    FOREING KEY(idCliente) REFERENCES cliente(id) ON DELETE CASCADE
);

-- Create views

CREATE VIEW vista_bebidas_disponibles AS
SELECT 
    codigo,
    nombre,
    precio,
    tipo_bebida AS tipo,
    descripcion
FROM bebidas;

CREATE VIEW vista_platos_disponibles AS
SELECT 
    codigo,
    nombre,
    precio,
    tipo_plato AS tipo,
    descripcion
FROM platos;

-- create triggers

-- validar calificacion de los platos

DELIMITER $$

CREATE TRIGGER trg_validar_estrellas_plato
BEFORE INSERT ON calificacion_plato
FOR EACH ROW
BEGIN
    IF NEW.num_estrellas < 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Las estrellas deben estar entre 1 y 5';
    END IF;
END$$

DELIMITER ;

-- Validar calificacion de las bebidas

DELIMITER $$

CREATE TRIGGER trg_validar_estrellas_bebida
BEFORE INSERT ON calificacion_bebida
FOR EACH ROW
BEGIN
    IF NEW.num_estrellas < 1 OR NEW.num_estrellas > 5 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Las estrellas deben estar entre 1 y 5';
    END IF;
END$$

DELIMITER ;

-- Controlar quien califica los platos
DELIMITER $$

CREATE TRIGGER trg_validar_compra_plato
BEFORE INSERT ON calificacion_plato
FOR EACH ROW
BEGIN
    DECLARE existe INT;
    SELECT COUNT(*) INTO existe
    FROM detalle_venta dv
    JOIN venta v ON dv.idVenta = v.num_venta
    WHERE dv.idPlato = NEW.idPlato
    AND v.id_cliente = NEW.idCliente;

    IF existe = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Debe comprar el plato antes de calificar';
    END IF;
END$$

DELIMITER ;

-- Create procedures

-- Registrar cliente

DELIMITER $$

CREATE PROCEDURE sp_registrar_cliente(
    IN p_id INT,
    IN p_nombre VARCHAR(50),
    IN p_direccion VARCHAR(100),
    IN p_correo VARCHAR(70)
)
BEGIN
    INSERT INTO cliente(id, nombre, direccion, correo)
    VALUES (p_id, p_nombre, p_direccion, p_correo);
END$$

DELIMITER ;

-- Registrar plato

DELIMITER $$

CREATE PROCEDURE sp_registrar_plato(
    IN p_nombre VARCHAR(30),
    IN p_descripcion VARCHAR(300),
    IN p_precio FLOAT,
    IN p_tipo VARCHAR(20)
)
BEGIN
    INSERT INTO platos(nombre, descripcion, precio, tipo_plato)
    VALUES (p_nombre, p_descripcion, p_precio, p_tipo);
END$$

DELIMITER ;

-- Registrar bebidas

DELIMITER $$

CREATE PROCEDURE sp_registrar_bebida(
    IN p_nombre VARCHAR(60),
    IN p_descripcion VARCHAR(300),
    IN p_precio FLOAT,
    IN p_tipo VARCHAR(20)
)
BEGIN
    INSERT INTO bebidas(nombre, descripcion, precio, tipo_bebida)
    VALUES (p_nombre, p_descripcion, p_precio, p_tipo);
END$$

DELIMITER ;

-- Registrar ventas

DELIMITER $$

CREATE PROCEDURE sp_registrar_venta(
    IN p_idCliente INT,
    IN p_fecha DATE,
    IN p_total DECIMAL(10,2)
)
BEGIN
    INSERT INTO venta(id_cliente, fecha, total)
    VALUES (p_idCliente, p_fecha, p_total);
    
    SELECT LAST_INSERT_ID() AS num_venta;
END$$

DELIMITER ;

-- Registrar calificacion de platos

DELIMITER $$

CREATE PROCEDURE sp_calificar_plato(
    IN p_estrellas INT,
    IN p_comentario VARCHAR(300),
    IN p_idPlato INT,
    IN p_idCliente INT
)
BEGIN
    INSERT INTO calificacion_plato(num_estrellas, comentario, idPlato, idCliente, fecha)
    VALUES (p_estrellas, p_comentario, p_idPlato, p_idCliente, CURDATE());
END$$

DELIMITER ;

-- Registrar calificacion de bebidas

DELIMITER $$

CREATE PROCEDURE sp_registrar_calificacion_bebida(
    IN p_num_estrellas INT,
    IN p_comentario VARCHAR(300),
    IN p_idBebida INT,
    IN p_idCliente INT)
BEGIN
    IF p_num_estrellas < 1 OR p_num_estrellas > 5 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La calificación debe estar entre 1 y 5';
    ELSE
        INSERT INTO calificacion_bebida( num_estrellas,comentario,idBebida,idCliente,fecha)
        VALUES (p_num_estrellas,p_comentario,p_idBebida, p_idCliente, CURDATE());
    END IF;
END$$

DELIMITER ;
