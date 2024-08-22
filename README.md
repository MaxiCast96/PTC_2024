Base de datos del proyecto:

```
-- Establecer _ORACLE_SCRIPT a TRUE para evitar errores con CREATE USER
ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;

-- Crear usuario y otorgarle permisos de conexión
CREATE USER ANDRE_DEVELOPER IDENTIFIED BY "1234";
GRANT CONNECT TO ANDRE_DEVELOPER;

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

-- Crear tablas
CREATE TABLE Roles (
    rol_id INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'cliente');
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'empleado');
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'administrador');

select * from Roles

CREATE TABLE Usuarios (
    usuario_id VARCHAR2(255) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(100) NOT NULL,
    rol_id INT,
    foto_perfil VARCHAR(255),
    CONSTRAINT fk_usuario_rol_id FOREIGN KEY (rol_id) REFERENCES Roles(rol_id)
);

select * from Usuarios

CREATE TABLE Peliculas (
    pelicula_id INT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR2(120),
    duracion INT NOT NULL,
    clasificacion_id INT NOT NULL,
    genero_id INT NOT NULL,
    poster_id INT NOT NULL,
    CONSTRAINT fk_generoPelicula FOREIGN KEY (genero_id) REFERENCES GeneroPelicula(genero_id),
    CONSTRAINT fk_posterPelicula FOREIGN KEY (poster_id) REFERENCES PosterPelicula(poster_id),
    CONSTRAINT fk_clasificacionPelicula FOREIGN KEY (clasificacion_id) REFERENCES Clasificacion(clasificacion_id)
);

INSERT INTO Peliculas (pelicula_id, titulo, descripcion, duracion, clasificacion_id, genero_id, poster_id)
VALUES (1, 'The Shawshank Redemption', 'A tale of hope and redemption in a maximum-security prison', 142, 1, 1, 1);


CREATE TABLE Clasificacion (
    clasificacion_id INT PRIMARY KEY,
    nombre_clasificacion VARCHAR2(20)
);

INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) VALUES (1, 'bl')
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) 
VALUES (2, 'A');
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) 
VALUES (3, 'B');
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) 
VALUES (4, 'C');
INSERT INTO Clasificacion (clasificacion_id, nombre_clasificacion) 
VALUES (5, 'D');

select * from Clasificacion


CREATE TABLE GeneroPelicula (
    genero_id INT PRIMARY KEY,
    genero VARCHAR2(25) UNIQUE NOT NULL
);

INSERT INTO GeneroPelicula (genero_id, genero) VALUES (1, 'Acción')
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (2, 'Aventura')
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (3, 'Romance')
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (4, 'Terror')
INSERT INTO GeneroPelicula (genero_id, genero) VALUES (5, 'Psicoanálisis')

SELECT * FROM GeneroPelicula


CREATE TABLE PosterPelicula (
    poster_id INT PRIMARY KEY,
    poster VARCHAR2(255) NOT NULL
);

INSERT INTO PosterPelicula (poster_id, poster) VALUES (1, 'URL de poster')

CREATE TABLE Salas_PTC (
    sala_id INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    capacidad_asientos INT NOT NULL
);

INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos) VALUES (1, '1A', 20)
INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos) VALUES (2, '1B', 20)
INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos) VALUES (3, '1C', 20)
INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos) VALUES (4, '1D', 20)
INSERT INTO Salas_PTC (sala_id, nombre, capacidad_asientos) VALUES (5, '1E', 20)

SELECT * FROM Salas_PTC

CREATE TABLE Asientos (
    asiento_id INT PRIMARY KEY,
    sala_id INT,
    fila VARCHAR2(1) NOT NULL,
    numero INT NOT NULL,
    UNIQUE (sala_id, fila, numero),
    CONSTRAINT fk_asientos_sala_id FOREIGN KEY (sala_id) REFERENCES Salas_PTC(sala_id)
);

INSERT INTO Asientos (asiento_id, sala_id, fila, numero) VALUES (1, 1, 'A', 1)
INSERT INTO Asientos (asiento_id, sala_id, fila, numero) VALUES (2, 1, 'A', 2)
INSERT INTO Asientos (asiento_id, sala_id, fila, numero) VALUES (3, 1, 'A', 3)
INSERT INTO Asientos (asiento_id, sala_id, fila, numero) VALUES (4, 1, 'A', 4)
INSERT INTO Asientos (asiento_id, sala_id, fila, numero) VALUES (5, 1, 'A', 5)

select * from Asientos

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
    CONSTRAINT fk_funciones_horario_id FOREIGN KEY (funcion_id) REFERENCES Horario_Funcion(horario_id)
);

INSERT INTO Funciones (funcion_id, pelicula_id, sala_id, fecha_hora, tipo_id, idioma_id, horario_id)
VALUES (1, 1, 1, TO_TIMESTAMP('2024-01-20 19:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1, 1, 1);



CREATE TABLE TipoFuncion (
    tipo_id INT PRIMARY KEY,
    funcion_tipo VARCHAR2(25)
);

INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (tipo_funcion_seq.NEXTVAL, 'Estreno');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (tipo_funcion_seq.NEXTVAL, 'Normal');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (tipo_funcion_seq.NEXTVAL, '3D');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (tipo_funcion_seq.NEXTVAL, 'IMAX');
INSERT INTO TipoFuncion (tipo_id, funcion_tipo) VALUES (tipo_funcion_seq.NEXTVAL, 'VIP');

select * FROM TipoFuncion

CREATE TABLE IdiomaFuncion (
    idioma_id INT PRIMARY KEY,
    idioma VARCHAR(25)
);

INSERT INTO IdiomaFuncion (idioma_id, idioma) VALUES (1, 'español')

CREATE TABLE Reservas_PTC (
    reserva_id INT PRIMARY KEY,
    usuario_id VARCHAR(255),
    funcion_id INT,
    fecha_reserva DATE NOT NULL,
    total_pago DECIMAL(10, 2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    CONSTRAINT fk_reservas_PTC_usuario_id FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id),
    CONSTRAINT fk_reservas_funcion_id FOREIGN KEY (funcion_id) REFERENCES Funciones(funcion_id)
);

CREATE TABLE Horario_Funcion(
horario_id INT PRIMARY KEY,
hora VARCHAR2(5)
)

INSERT INTO Horario_Funcion (horario_id, hora) VALUES (1, '12:00')
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (2, '2:00')
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (3, '8:00')
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (4, '14:00')
INSERT INTO Horario_Funcion (horario_id, hora) VALUES (5, '16:00')

SELECT * FROM Horario_Funcion

CREATE TABLE DetallesReservas (
    detalle_id INT PRIMARY KEY,
    reserva_id INT,
    asiento_id INT,
    CONSTRAINT fk_DetallesReservas_reserva_id FOREIGN KEY (reserva_id) REFERENCES Reservas_PTC(reserva_id),
    CONSTRAINT fk_DetallesReservas_asiento_id FOREIGN KEY (asiento_id) REFERENCES Asientos(asiento_id)
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

INSERT INTO EstadoAsientos (estado_asiento_id, funcion_id, asiento_id, estado_id)
VALUES (1, 1, 1, 1);


select * from EstadoAsientos

CREATE TABLE Estado_disponible_ocupado (
    estado_id INT PRIMARY KEY,
    estado VARCHAR2(10)
);

INSERT INTO Estado_disponible_ocupado (estado_id, estado) VALUES (1, 'disponible')
INSERT INTO Estado_disponible_ocupado (estado_id, estado) VALUES (2, 'ocupado')

SELECT p.titulo, g.genero 
FROM Peliculas p
INNER JOIN GeneroPelicula g ON p.genero_id = g.genero_id;

SELECT r.reserva_id, u.nombre, f.fecha_hora
FROM Reservas_PTC r
INNER JOIN Usuarios u ON r.usuario_id = u.usuario_id
INNER JOIN Funciones f ON r.funcion_id = f.funcion_id
WHERE u.usuario_id = '2';  

SELECT a.fila, a.numero
FROM Asientos a
INNER JOIN EstadoAsientos e ON a.asiento_id = e.asiento_id
WHERE e.funcion_id = 1;  

SELECT p.titulo, COUNT(*) AS total_reservas
FROM Peliculas p
INNER JOIN Funciones f ON p.pelicula_id = f.pelicula_id
INNER JOIN Reservas_PTC r ON f.funcion_id = r.funcion_id
GROUP BY p.titulo
ORDER BY total_reservas DESC;



-- Crear triggers para AUTO_INCREMENT
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

-- Inserts iniciales
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'cliente');
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'empleado');
INSERT INTO Roles (rol_id, nombre) VALUES (roles_seq.NEXTVAL, 'administrador');

INSERT INTO PosterPelicula (poster_id, poster) VALUES (poster_id.NEXTVAL, 'https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fp_insideout_19751_af12286c.jpeg?alt=media&token=362b1c16-1102-4b68-a4d8-9bd68cafda57')



-- Seleccionar todos los usuarios
SELECT * FROM Usuarios;

SELECT * FROM Roles;

--Destrucción Masiva--
DROP TABLE DetallesReservas CASCADE CONSTRAINTS;
DROP TABLE EstadoAsientos CASCADE CONSTRAINTS;
DROP TABLE Reservas_PTC CASCADE CONSTRAINTS;
DROP TABLE Funciones CASCADE CONSTRAINTS;
DROP TABLE Asientos CASCADE CONSTRAINTS;
DROP TABLE Salas_PTC CASCADE CONSTRAINTS;
DROP TABLE Usuarios CASCADE CONSTRAINTS;
DROP TABLE Roles CASCADE CONSTRAINTS;
DROP TABLE PosterPelicula CASCADE CONSTRAINTS;
DROP TABLE GeneroPelicula CASCADE CONSTRAINTS;
DROP TABLE Clasificacion CASCADE CONSTRAINTS;
DROP TABLE Peliculas CASCADE CONSTRAINTS;
DROP TABLE TipoFuncion CASCADE CONSTRAINTS;
DROP TABLE IdiomaFuncion CASCADE CONSTRAINTS;
DROP TABLE Estado_disponible_ocupado CASCADE CONSTRAINTS;

DROP SEQUENCE roles_seq;
DROP SEQUENCE usuarios_seq;
DROP SEQUENCE peliculas_seq;
DROP SEQUENCE clasificacion_seq;
DROP SEQUENCE genero_pelicula_seq;
DROP SEQUENCE poster_pelicula_seq;
DROP SEQUENCE salas_ptc_seq;
DROP SEQUENCE asientos_seq;
DROP SEQUENCE funciones_seq;
DROP SEQUENCE tipo_funcion_seq;
DROP SEQUENCE idioma_funcion_seq;
DROP SEQUENCE reservas_ptc_seq;
DROP SEQUENCE detalles_reservas_seq;
DROP SEQUENCE estado_asientos_seq;
DROP SEQUENCE estado_disponible_ocupado_seq;
```
