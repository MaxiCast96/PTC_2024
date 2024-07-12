Esta es la base de datos del proyecto:

ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;
CREATE USER "Guille" IDENTIFIED BY "fortnite_2017";
GRANT CONNECT TO "Guille";


-- Crear las secuencias para cada tabla que requiere AUTO_INCREMENT
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

-- Crear las tablas
CREATE TABLE Roles (
    rol_id INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE Usuarios (
    usuario_id INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(100) NOT NULL,
    rol_id INT,
    foto_perfil VARCHAR(255), -- URL de la foto de perfil
    CONSTRAINT fk_usuario_rol_id
    FOREIGN KEY (rol_id) REFERENCES Roles(rol_id)
);

CREATE TABLE Peliculas (
    pelicula_id INT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR2(120),
    duracion INT NOT NULL, -- en minutos
    nombre_clasificacion VARCHAR(20) NOT NULL, --Relacionado con la tabla 'Clasificacion'--,
    genero_id INT NOT NULL, --Relacionado con la tabla 'GeneroPelicula'--,
    poster_id INT NOT NULL, --Relacionado con la tabla 'PosterPelicula'--
    CONSTRAINT fk_generoPelicula FOREIGN KEY (genero_id) REFERENCES genero_id (GeneroPelicula),
    CONSTRAINT fk_posterPelicula FOREIGN KEY (poster_id) REFERENCES poster_id (PosterPelicula)
);

CREATE TABLE Clasificacion (
    clasificacion_id INT PRIMARY KEY,
    nombre_clasificacion VARCHAR2(20)
);

CREATE TABLE GeneroPelicula (
    genero_id INT PRIMARY KEY NOT NULL,
    genero VARCHAR2 UNIQUE NOT NULL --Género de película--
);

CREATE TABLE PosterPelicula (
    poster_id INT PRIMARY KEY NOT NULL,
    poster VARCHAR2 (255) NOT NULL --Poster o imagen promocional de la película--
);

CREATE TABLE Salas_PTC (
    sala_id INT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    capacidad_asientos INT NOT NULL
);

CREATE TABLE Asientos (
    asiento_id INT PRIMARY KEY,
    sala_id INT,
    fila VARCHAR2(1) NOT NULL,
    numero INT NOT NULL,
    UNIQUE (sala_id, fila, numero),
    CONSTRAINT fk_asientos_sala_id
    FOREIGN KEY (sala_id) REFERENCES Salas_PTC(sala_id)
);

CREATE TABLE Funciones (
    funcion_id INT PRIMARY KEY,
    pelicula_id INT,
    sala_id INT,
    fecha_hora TIMESTAMP NOT NULL,
    tipo_id INT NOT NULL, --Relacionado con con la tabla 'TipoFuncion'--,
    idioma_id INT, --Relacionado con la tabla 'IdiomaFuncion'--,
    CONSTRAINT fk_funciones_pelicula_id
    FOREIGN KEY (pelicula_id) REFERENCES Peliculas(pelicula_id),
    CONSTRAINT fk_funciones_sala_id
    FOREIGN KEY (sala_id) REFERENCES Salas_PTC(sala_id),
    CONSTRAINT fk_tipoFuncion FOREIGN KEY (tipo_id) REFERENCES tipo_id (TipoFuncion),
    CONSTRAINT fk_idiomaFuncion FOREIGN KEY (idioma_id) REFERENCES idioma_id (IdiomaFuncion)
);

CREATE TABLE TipoFuncion (
    tipo_id INT PRIMARY KEY NOT NULL,
    funcion_tipo VARCHAR2 (25) --Para clasificar si la funcion es '2D' o '3D'--
);

CREATE TABLE IdiomaFuncion (
    idioma_id INT PRIMARY KEY NOT NULL,
    idioma VARCHAR (25) --Para identificar si la funcion esta 'Idioma Original', 'Doblada' o 'Subtitulada'--
);

CREATE TABLE Reservas_PTC (
    reserva_id INT PRIMARY KEY,
    usuario_id INT,
    funcion_id INT,
    fecha_reserva DATE NOT NULL,
    total_pago DECIMAL(10, 2) NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    CONSTRAINT fk_reservas_PTC_usuario_id
    FOREIGN KEY (usuario_id) REFERENCES Usuarios(usuario_id),
    CONSTRAINT fk_reservas_funcion_id
    FOREIGN KEY (funcion_id) REFERENCES Funciones(funcion_id)
);

CREATE TABLE DetallesReservas (
    detalle_id INT PRIMARY KEY,
    reserva_id INT,
    asiento_id INT,
    CONSTRAINT fk_DetallesReservas_reserva_id 
    FOREIGN KEY (reserva_id) REFERENCES Reservas_PTC(reserva_id),
    CONSTRAINT fk_DetallesReservas_asiento_id
    FOREIGN KEY (asiento_id) REFERENCES Asientos(asiento_id)
);

CREATE TABLE EstadoAsientos (
    estado_asiento_id INT PRIMARY KEY,
    funcion_id INT,
    asiento_id INT,
    estado VARCHAR(10) NOT NULL, -- 'disponible' u 'ocupado'
    CONSTRAINT fk_EstadoAsientos_funcion_id
    FOREIGN KEY (funcion_id) REFERENCES Funciones(funcion_id),
    CONSTRAINT fk_EstadoAsientos_asiento_id
    FOREIGN KEY (asiento_id) REFERENCES Asientos(asiento_id),
    UNIQUE (funcion_id, asiento_id) -- Asegura que un asiento en una función específica no se repita
);

CREATE TABLE Estado_disponible_ocupado (
    estado_id INT PRIMARY KEY,
    estado VARCHAR2(10)
);

-- Crear los triggers para cada tabla que requiere AUTO_INCREMENT
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

-- Inserts
INSERT INTO Roles (nombre) VALUES ('cliente'), ('empleado'), ('administrador');

