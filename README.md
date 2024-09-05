Base de datos del proyecto:

```
-- Establecer _ORACLE_SCRIPT a TRUE para evitar errores con CREATE USER
ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;

-- Crear usuario y otorgarle permisos de conexión
CREATE USER CinemaNOW IDENTIFIED BY "Estrella_Bella!";
GRANT CONNECT TO CinemaNOW;

-- Crear tablas
CREATE TABLE Clasificacion (
    clasificacion_id INT PRIMARY KEY,
    nombre_clasificacion VARCHAR2(20) NOT NULL UNIQUE
);

insert into Clasificacion values (1, 'terror');

CREATE TABLE GeneroPelicula (
    genero_id INT PRIMARY KEY,
    genero VARCHAR2(25) UNIQUE NOT NULL
);

Insert into GeneroPelicula values (1, 'terror');

CREATE TABLE Roles (
    rol_id INT PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL UNIQUE
);

CREATE TABLE Usuarios (
    usuario_id VARCHAR2(255) PRIMARY KEY,
    nombre VARCHAR2(100) NOT NULL,
    email VARCHAR2(100) NOT NULL UNIQUE,
    contraseña VARCHAR2(100) NOT NULL,
    rol_id INT NOT NULL,
    foto_perfil VARCHAR2(255),
    CONSTRAINT fk_usuario_rol_id FOREIGN KEY (rol_id) REFERENCES Roles(rol_id)
);

CREATE TABLE Salas_PTC (
    sala_id INT PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    capacidad_asientos INT NOT NULL
);

CREATE TABLE Peliculas (
    pelicula_id INT PRIMARY KEY,
    titulo VARCHAR2(100) NOT NULL,
    descripcion VARCHAR2(120),
    duracion INT NOT NULL,
    clasificacion_id INT NOT NULL,
    genero_id INT NOT NULL,
    poster VARCHAR2(255) NOT NULL,
    trailer VARCHAR2(255) NOT NULL,
    CONSTRAINT fk_generoPelicula FOREIGN KEY (genero_id) REFERENCES GeneroPelicula(genero_id),
    CONSTRAINT fk_clasificacionPelicula FOREIGN KEY (clasificacion_id) REFERENCES Clasificacion(clasificacion_id)
);

CREATE TABLE Asientos (
    asiento_id INT PRIMARY KEY,
    sala_id INT,
    fila VARCHAR2(1) NOT NULL,
    numero INT NOT NULL,
    ocupado NUMBER(1) DEFAULT 0,
    UNIQUE (sala_id, fila, numero),
    CONSTRAINT fk_asientos_sala_id FOREIGN KEY (sala_id) REFERENCES Salas_PTC(sala_id)
);


CREATE TABLE TipoFuncion (
    tipo_id INT PRIMARY KEY,
    funcion_tipo VARCHAR2(25) NOT NULL UNIQUE
);

CREATE TABLE IdiomaFuncion (
    idioma_id INT PRIMARY KEY,
    idioma VARCHAR2(25) NOT NULL UNIQUE
);

CREATE TABLE Horario_Funcion(
    horario_id INT PRIMARY KEY,
    hora VARCHAR2(5) NOT NULL UNIQUE
);

CREATE TABLE Funciones (
    funcion_id INT PRIMARY KEY,
    pelicula_id INT,
    sala_id INT,
    fecha_hora TIMESTAMP NOT NULL,
    tipo_id INT NOT NULL,
    idioma_id INT,
    horario_id INT,
    CONSTRAINT fk_funciones_pelicula_id FOREIGN KEY (pelicula_id) REFERENCES Peliculas(pelicula_id),
    CONSTRAINT fk_funciones_sala_id FOREIGN KEY (sala_id) REFERENCES Salas_PTC(sala_id),
    CONSTRAINT fk_funciones_tipo_id FOREIGN KEY (tipo_id) REFERENCES TipoFuncion(tipo_id),
    CONSTRAINT fk_funciones_idioma_id FOREIGN KEY (idioma_id) REFERENCES IdiomaFuncion(idioma_id),
    CONSTRAINT fk_funciones_horario_id FOREIGN KEY (horario_id) REFERENCES Horario_Funcion(horario_id)
);

Create Table Reservas_Android (
    funcionand_id INT PRIMARY KEY,
    usuario_id VARCHAR2(255) NOT NULL,
    pelicula_id INT NOT NULL,
    sala_id INT NOT NULL,
    Hora_funcion VARCHAR2(6),
    fecha_reserva DATE NOT NULL,
    total_pago DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_reservas_android_pelicula_id FOREIGN KEY (pelicula_id) REFERENCES Peliculas(pelicula_id),
    CONSTRAINT fk_reservas_android_sala_id FOREIGN KEY (sala_id) REFERENCES Salas_PTC(sala_id),
    CONSTRAINT fk_reservas_android_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id)
);

CREATE TABLE Reservas_PTC (
    reserva_id INT PRIMARY KEY,
    usuario_id VARCHAR2(255),
    funcion_id INT,
    fecha_reserva DATE NOT NULL,
    total_pago DECIMAL(10, 2) NOT NULL,
    metodo_pago VARCHAR2(50) NOT NULL,
    CONSTRAINT fk_reservas_PTC_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id),
    CONSTRAINT fk_reservas_funcion_id FOREIGN KEY (funcion_id) REFERENCES Funciones(funcion_id)
);

CREATE TABLE DetallesReservas (
    detalle_id INT PRIMARY KEY,
    reserva_id INT,
    asiento_id INT,
    CONSTRAINT fk_DetallesReservas_reserva_id FOREIGN KEY (reserva_id) REFERENCES Reservas_PTC(reserva_id),
    CONSTRAINT fk_DetallesReservas_asiento_id FOREIGN KEY (asiento_id) REFERENCES Asientos(asiento_id)
);

CREATE TABLE Estado_disponible_ocupado (
    estado_id INT PRIMARY KEY,
    estado VARCHAR2(10) NOT NULL UNIQUE
);

CREATE TABLE EstadoAsientos (
    estado_asiento_id INT PRIMARY KEY,
    funcion_id INT,
    asiento_id INT,
    estado_id INT NOT NULL,
    CONSTRAINT fk_EstadoAsientos_funcion_id FOREIGN KEY (funcion_id) REFERENCES Funciones(funcion_id),
    CONSTRAINT fk_EstadoAsientos_asiento_id FOREIGN KEY (asiento_id) REFERENCES Asientos(asiento_id),
    CONSTRAINT fk_EstadoAsientos_Estado_disponible_ocupado FOREIGN KEY (estado_id) REFERENCES Estado_disponible_ocupado(estado_id),
    UNIQUE (funcion_id, asiento_id, estado_id)
);

CREATE TABLE auditoria (
    id_auditoria INT PRIMARY KEY AUTO_INCREMENT,
    tabla_afectada VARCHAR(100),
    accion VARCHAR(10),
    id_registro INT,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear secuencias para AUTO_INCREMENT
CREATE SEQUENCE roles_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE usuarios_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE peliculas_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE clasificacion_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE genero_pelicula_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE poster_pelicula_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE salas_ptc_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE asientos_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE funciones_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE tipo_funcion_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE idioma_funcion_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE reservas_ptc_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE detalles_reservas_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE estado_asientos_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE estado_disponible_ocupado_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE seq_funcionand_id START WITH 1 INCREMENT BY 1;

-- Crear triggers

-- Trigger para registrar en la tabla de auditoría
DELIMITER $$
CREATE TRIGGER trigger_insertar_empleado
AFTER INSERT ON empleados
FOR EACH ROW
BEGIN
    -- Registro en la tabla de auditoría
    INSERT INTO auditoria (tabla_afectada, accion, id_registro)
    VALUES ('empleados', 'INSERT', NEW.id_empleado);
END $$
DELIMITER ;

-- Triggers de autoincrement
CREATE OR REPLACE TRIGGER roles_bir BEFORE INSERT ON Roles FOR EACH ROW
BEGIN
    SELECT roles_seq.NEXTVAL INTO :NEW.rol_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER usuarios_bir BEFORE INSERT ON Usuarios FOR EACH ROW
BEGIN
    SELECT usuarios_seq.NEXTVAL INTO :NEW.usuario_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER peliculas_bir BEFORE INSERT ON Peliculas FOR EACH ROW
BEGIN
    SELECT peliculas_seq.NEXTVAL INTO :NEW.pelicula_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER clasificacion_bir BEFORE INSERT ON Clasificacion FOR EACH ROW
BEGIN
    SELECT clasificacion_seq.NEXTVAL INTO :NEW.clasificacion_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER genero_pelicula_bir BEFORE INSERT ON GeneroPelicula FOR EACH ROW
BEGIN
    SELECT genero_pelicula_seq.NEXTVAL INTO :NEW.genero_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER poster_pelicula_bir BEFORE INSERT ON PosterPelicula FOR EACH ROW
BEGIN
    SELECT poster_pelicula_seq.NEXTVAL INTO :NEW.poster_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER salas_ptc_bir BEFORE INSERT ON Salas_PTC FOR EACH ROW
BEGIN
    SELECT salas_ptc_seq.NEXTVAL INTO :NEW.sala_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER asientos_bir BEFORE INSERT ON Asientos FOR EACH ROW
BEGIN
    SELECT asientos_seq.NEXTVAL INTO :NEW.asiento_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER funciones_bir BEFORE INSERT ON Funciones FOR EACH ROW
BEGIN
    SELECT funciones_seq.NEXTVAL INTO :NEW.funcion_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER tipo_funcion_bir BEFORE INSERT ON TipoFuncion FOR EACH ROW
BEGIN
    SELECT tipo_funcion_seq.NEXTVAL INTO :NEW.tipo_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER idioma_funcion_bir BEFORE INSERT ON IdiomaFuncion FOR EACH ROW
BEGIN
    SELECT idioma_funcion_seq.NEXTVAL INTO :NEW.idioma_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER reservas_ptc_bir BEFORE INSERT ON Reservas_PTC FOR EACH ROW
BEGIN
    SELECT reservas_ptc_seq.NEXTVAL INTO :NEW.reserva_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER detalles_reservas_bir BEFORE INSERT ON DetallesReservas FOR EACH ROW
BEGIN
    SELECT detalles_reservas_seq.NEXTVAL INTO :NEW.detalle_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER estado_asientos_bir BEFORE INSERT ON EstadoAsientos FOR EACH ROW
BEGIN
    SELECT estado_asientos_seq.NEXTVAL INTO :NEW.estado_asiento_id FROM dual;
END;
/

CREATE OR REPLACE TRIGGER estado_disponible_ocupado_bir BEFORE INSERT ON Estado_disponible_ocupado FOR EACH ROW
BEGIN
    SELECT estado_disponible_ocupado_seq.NEXTVAL INTO :NEW.estado_id FROM dual;
END;
/

-- Insert de Roles
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'cliente');
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'empleado'); 
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'gerente');

-- Insert de Usuarios de Prueba 
INSERT INTO Usuarios (usuario_id, nombre, email, contraseña, rol_id, foto_perfil) 
VALUES (usuarios_seq.NEXTVAL, 'Juan Pérez', 'juan.perez@example.com', 'contraseña123', 1, 'link_to_photo_juan');

INSERT INTO Horario_Funcion (horario_id, hora) VALUES (1, '14:00');
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (2, '16:30');
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (3, '20:00');
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (4, '00:30');
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (5, '19:00');
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (6, '21:30');



-- Inserta Salas de Prueba
INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos)
VALUES (1, 'Sala 1', 50);

INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos)
VALUES (2, 'Sala 2', 75);

INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos)
VALUES (3, 'Sala 3', 100);

-- Inserta Asientos de Prueba para Sala 1
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (1, 1, 'A', '1', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (2, 1, 'A', '2', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (3, 1, 'B', '3', 1);

-- Inserta Asientos de Prueba para Sala 2
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (4, 2, 'A', '1', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (5, 2, 'A', '2', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (6, 2, 'B', '3', 0);

-- Inserta Asientos de Prueba para Sala 3
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (7, 3, 'A', '1', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (8, 3, 'A', '2', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (9, 3, 'B', '3', 0);

-- Inserta Asientos de Prueba para Sala 1
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (10, 1, 'B', '4', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (11, 1, 'C', '1', 1);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (12, 1, 'C', '2', 0);

-- Inserta Asientos de Prueba para Sala 2
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (13, 2, 'B', '4', 1);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (14, 2, 'C', '1', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (15, 2, 'C', '2', 0);

-- Inserta Asientos de Prueba para Sala 3
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (16, 3, 'B', '4', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (17, 3, 'C', '1', 1);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (18, 3, 'C', '2', 0);

-- Inserta Asientos de Prueba para Sala 1 (Fila D)
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (19, 1, 'D', '1', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (20, 1, 'D', '2', 1);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (21, 1, 'D', '3', 0);

-- Inserta Asientos de Prueba para Sala 2 (Fila D)
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (22, 2, 'D', '1', 1);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (23, 2, 'D', '2', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (24, 2, 'D', '3', 1);

-- Inserta Asientos de Prueba para Sala 3 (Fila D)
INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (25, 3, 'D', '1', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (26, 3, 'D', '2', 0);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero, ocupado)
VALUES (27, 3, 'D', '3', 1);

--Peliculas--
--Peliculas--
insert into peliculas (pelicula_id, titulo, descripcion, duracion, clasificacion_id, genero_id, poster, trailer)
values (2, 'Garfield', 'descripcion generica de garfield', 10, 1, 1, 'https://imagenes.gatotv.com/categorias/peliculas/posters/garfield_fuera_de_casa.jpg', 'https://youtu.be/GeR3YxTv_zU?si=YBbM2Po9JEIpJhSw');

INSERT INTO Peliculas (pelicula_id, titulo, descripcion, duracion, clasificacion_id, genero_id, poster, trailer)
VALUES (3, 'Spider-Man', 'Película de superhéroes', 120, 1, 1, 'https://mx.web.img3.acsta.net/pictures/22/12/21/16/31/3659442.jpg', 'https://youtu.be/oBmazlyP220?si=igERMejyVzTnIXMr');

INSERT INTO Peliculas (pelicula_id, titulo, descripcion, duracion, clasificacion_id, genero_id, poster, trailer)
VALUES (4, 'Inception', 'Película de ciencia ficción', 148, 1, 1, 'https://m.media-amazon.com/images/M/MV5BMTM0MjUzNjkwMl5BMl5BanBnXkFtZTcwNjY0OTk1Mw@@._V1_.jpg', 'https://youtu.be/RKUbu1clC2U?si=EaohcEZZMWPm8BPq');

INSERT INTO Peliculas (pelicula_id, titulo, descripcion, duracion, clasificacion_id, genero_id, poster, trailer)
VALUES (5, 'En busca de la felicidad', 'Drama sobre la superación personal', 117, 1, 1, 'https://i.pinimg.com/originals/fe/0a/38/fe0a38aa17f750907243efff7866668a.jpg', 'https://youtu.be/rxtGMH7OrLc?si=2F9wfMFQodaoZ7oh');

INSERT INTO Peliculas (pelicula_id, titulo, descripcion, duracion, clasificacion_id, genero_id, poster, trailer)
VALUES (6, 'Iron-Man 3', 'Drama sobre Tony Stark', 117, 1, 1, 'https://movieposter.gr/4020-large_default/iron-man-3.jpg', 'https://youtu.be/Ga817lEqAoI?si=q8QRXAWzGFlniypd');

INSERT INTO Peliculas (pelicula_id, titulo, descripcion, duracion, clasificacion_id, genero_id, poster, trailer)
VALUES (7, 'El Sorprendente Hombre-Araña 2', 'Drama sobre Peter Parker y sus flipantes aventuras contra electro', 117, 1, 1, 'https://m.media-amazon.com/images/I/71rRiiZJVRL._AC_UF894,1000_QL80_.jpg', 'https://youtu.be/U7vjPGJ8THU?si=1YGBUD9K1lYZQMDt');


-- Inserts para Clasificacion
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) VALUES (2, 'Comedia');
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) VALUES (3, 'Acción');
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) VALUES (4, 'Drama');
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) VALUES (5, 'Ciencia Ficción');

-- Inserts para GeneroPelicula
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (2, 'Comedia');
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (3, 'Acción');
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (4, 'Drama');
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (5, 'Ciencia Ficción');

-- Inserts para TipoFuncion
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (1, '2D');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (2, '3D');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (3, 'IMAX');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (4, '4D');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (5, 'VIP');

-- Inserts para IdiomaFuncion
INSERT INTO IdiomaFuncion (idioma_id, idioma) VALUES (1, 'Español');
INSERT INTO IdiomaFuncion (idioma_id, idioma) VALUES (2, 'Inglés');
INSERT INTO IdiomaFuncion (idioma_id, idioma) VALUES (3, 'Francés');
INSERT INTO IdiomaFuncion (idioma_id, idioma) VALUES (4, 'Alemán');
INSERT INTO IdiomaFuncion (idioma_id, idioma) VALUES (5, 'Japonés');

-- Selects
SELECT * FROM Usuarios;

SELECT * FROM Roles;

select * from Peliculas;

select * from Salas_PTC;

select * from Asientos where sala_id = 1;

delete from Asientos;

Select * From Reservas_Android;

select * from Usuarios;

--Inner Join--
SELECT r.funcionand_id, u.nombre AS nombre_usuario,p.titulo AS nombre_pelicula,s.nombre AS nombre_sala,r.hora_funcion,r.fecha_reserva,r.total_pago
FROM Reservas_Android r INNER JOIN Usuarios u ON r.usuario_id = u.usuario_id INNER JOIN Peliculas p ON r.pelicula_id = p.pelicula_id INNER JOIN Salas_PTC s ON r.sala_id = s.sala_id;

SELECT r.funcionand_id, u.nombre AS nombre_usuario,p.titulo AS nombre_pelicula,s.nombre AS nombre_sala,r.hora_funcion,r.fecha_reserva,r.total_pago
FROM Reservas_Android r INNER JOIN Usuarios u ON r.usuario_id = u.usuario_id INNER JOIN Peliculas p ON r.pelicula_id = p.pelicula_id INNER JOIN Salas_PTC s ON r.sala_id = s.sala_id 
WHERE r.usuario_id = 'f97e7e53-2568-44a7-bc16-736e97658ac8';


-- Procedimientos almacenados:

-- Procedimiento almacenado para obtener el listado de empleados con salarios mayores a un valor x
DELIMITER $$
CREATE PROCEDURE obtener_empleados_salario_alto (IN salario_minimo DECIMAL(10,2))
BEGIN
    SELECT * FROM empleados WHERE salario > salario_minimo;
END $$
DELIMITER ;

-- Procedimiento almacenado para actualizar el salario de un empleado por su id
DELIMITER $$
CREATE PROCEDURE actualizar_salario_empleado (IN id INT, IN nuevo_salario DECIMAL(10,2))
BEGIN
    UPDATE empleados SET salario = nuevo_salario WHERE id_empleado = id;
END $$
DELIMITER ;

-- Procedimiento almacenado para eliminar empleados con un salario por debajo de x
DELIMITER $$
CREATE PROCEDURE eliminar_empleados_salario_bajo (IN salario_minimo DECIMAL(10,2))
BEGIN
    DELETE FROM empleados WHERE salario < salario_minimo;
END $$
DELIMITER ;


-- Procedimiento almacenado para auditoría
DELIMITER $$
CREATE PROCEDURE listar_acciones_auditoria ()
BEGIN
    SELECT * FROM auditoria ORDER BY fecha DESC;
END $$
DELIMITER ;


-- Destrucción masiva --
BEGIN EXECUTE IMMEDIATE 'DROP TABLE DetallesReservas CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Reservas_PTC CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE EstadoAsientos CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Estado_disponible_ocupado CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Funciones CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Horario_Funcion CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE IdiomaFuncion CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE TipoFuncion CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Asientos CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Peliculas CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Salas_PTC CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Usuarios CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Roles CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE GeneroPelicula CASCADE CONSTRAINTS'; 
EXECUTE IMMEDIATE 'DROP TABLE Clasificacion CASCADE CONSTRAINTS';
EXECUTE IMMEDIATE 'DROP SEQUENCE roles_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE usuarios_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE peliculas_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE clasificacion_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE genero_pelicula_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE poster_pelicula_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE salas_ptc_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE asientos_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE funciones_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE tipo_funcion_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE idioma_funcion_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE reservas_ptc_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE detalles_reservas_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE estado_asientos_seq';
EXECUTE IMMEDIATE 'DROP SEQUENCE estado_disponible_ocupado_seq';

EXECUTE IMMEDIATE 'DROP TRIGGER roles_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER usuarios_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER peliculas_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER clasificacion_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER genero_pelicula_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER poster_pelicula_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER salas_ptc_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER asientos_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER funciones_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER tipo_funcion_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER idioma_funcion_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER reservas_ptc_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER detalles_reservas_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER estado_asientos_bir';
EXECUTE IMMEDIATE 'DROP TRIGGER estado_disponible_ocupado_bir';
END; /



```
