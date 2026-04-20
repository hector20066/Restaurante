CREATE DATABASE IF NOT EXISTS restaurante;
USE restaurante;

CREATE TABLE platos (
    codigo INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(300),
    precio FLOAT,
    tipo_plato VARCHAR(20)
);

CREATE TABLE bebidas (
    codigo INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    descripcion VARCHAR(300),
    precio FLOAT NOT NULL,
    tipo_bebida VARCHAR(20)
);

CREATE TABLE cliente (
    id INT PRIMARY KEY,
    nombre VARCHAR(50),
    direccion VARCHAR(20),
    correo VARCHAR(70)
);

CREATE TABLE venta (
    num_venta INT PRIMARY KEY AUTO_INCREMENT,
    id_cliente INT,
    fecha DATE,
    total DECIMAL(10,2),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE detalle_venta (
    idDetalle INT PRIMARY KEY AUTO_INCREMENT,
    idVenta INT,
    idPlato INT NULL,
    idBebida INT NULL,
    cantPlato INT NULL,
    cantBebida INT NULL,
    precioPlato DECIMAL(10,2) NULL,
    precioBebida DECIMAL(10,2) NULL,
    FOREIGN KEY (idVenta) REFERENCES venta(num_venta) ON DELETE CASCADE,
    FOREIGN KEY (idPlato) REFERENCES platos(codigo),
    FOREIGN KEY (idBebida) REFERENCES bebidas(codigo)
);

CREATE TABLE calificacion_bebida (
    id INT PRIMARY KEY AUTO_INCREMENT,
    num_estrellas INT,
    comentario VARCHAR(300),
    idBebida INT,
    idCliente INT,
    fecha DATE,
    FOREIGN KEY (idBebida) REFERENCES bebidas(codigo),
    FOREIGN KEY (idCliente) REFERENCES cliente(id)
);

CREATE TABLE calificacion_plato (
    id INT PRIMARY KEY AUTO_INCREMENT,
    num_estrellas INT,
    comentario VARCHAR(300),
    idPlato INT,
    idCliente INT,
    fecha DATE,
    FOREIGN KEY (idPlato) REFERENCES platos(codigo),
    FOREIGN KEY (idCliente) REFERENCES cliente(id)
);
