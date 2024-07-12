--Tablas--
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
    foto_perfil VARCHAR(255), 
    CONSTRAINT fk_usuario_rol_id
    FOREIGN KEY (rol_id) REFERENCES Roles(rol_id)
);

CREATE TABLE Peliculas (
    pelicula_id INT PRIMARY KEY, 
    titulo VARCHAR(100) NOT NULL,
    descripcion VARCHAR2(120),
    duracion INT NOT NULL,
    
    clasificacion_id INT NOT NULL,
    genero_id INT NOT NULL,
    poster_id INT NOT NULL, 
    
    CONSTRAINT fk_clasificacionPelicula FOREIGN KEY (clasificacion_id)
    REFERENCES Clasificacion(clasificacion_id),
    
    CONSTRAINT fk_generoPelicula FOREIGN KEY (genero_id)
    REFERENCES GeneroPelicula(genero_id),
    
    CONSTRAINT fk_posterPelicula FOREIGN KEY (poster_id)
    REFERENCES PosterPelicula(poster_id)
);

Create table Clasificacion(
clasificacion_id INT PRIMARY KEY,
nombre_clasificacion VARCHAR2(20)
)


CREATE TABLE GeneroPelicula(
genero_id INT PRIMARY KEY,
genero VARCHAR2(15)
)

CREATE TABLE PosterPelicula(
    poster_id INT PRIMARY KEY NOT NULL,
    poster VARCHAR2 (255) NOT NULL
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
    tipo_id INT NOT NULL, 
    idioma_id INT, 
    
    CONSTRAINT fk_funciones_pelicula_id
    FOREIGN KEY (pelicula_id) REFERENCES Peliculas(pelicula_id),
    
    CONSTRAINT fk_funciones_sala_id
    FOREIGN KEY (sala_id) REFERENCES Salas_PTC(sala_id),
    
    CONSTRAINT fk_Funciones_tipo_id
    FOREIGN KEY (tipo_id)REFERENCES Tipofuncion(tipo_id),
    
    CONSTRAINT fk_funcion_idioma_id
    FOREIGN KEY (idioma_id) REFERENCES IdiomaFuncion(idioma_id)
);

CREATE TABLE TipoFuncion(
    tipo_id INT PRIMARY KEY NOT NULL, 
    funcion_tipo VARCHAR2 (25) 
);

CREATE TABLE IdiomaFuncion(
    idioma_id INT PRIMARY KEY NOT NULL, 
    idioma VARCHAR (25) 
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
    estado_id INT NOT NULL,
    CONSTRAINT fk_EstadoAsientos_Estado_disponible_ocupado
    FOREIGN KEY (estado_id) REFERENCES Estado_disponible_ocupado(estado_id),
    CONSTRAINT fk_EstadoAsientos_funcion_id
    FOREIGN KEY (funcion_id) REFERENCES Funciones(funcion_id),
    CONSTRAINT fk_EstadoAsientos_asiento_id
    FOREIGN KEY (asiento_id) REFERENCES Asientos(asiento_id),
    UNIQUE (funcion_id, asiento_id) 
);

CREATE TABLE Estado_disponible_ocupado(
estado_id INT PRIMARY KEY, 
estado VARCHAR2(10)
)

--Normalización actualizada a partir de los diagramas ER--
CREATE TABLE Reserva_y_usuario(
usuario_id INT, 
reserva_id INT,

CONSTRAINT fk_Reserva_y_usuario_usuario_id
FOREIGN KEY (usuario_id)
REFERENCES Usuarios(usuario_id),

CONSTRAINT fk_Reserva_y_usuario_reserva_id
FOREIGN KEY (reserva_id)
REFERENCES Reservas_PTC(reserva_id)
)

CREATE TABLE Reserva_y_funciones(
reserva_id INT,
funcion_id INT,

CONSTRAINT fk_Reserva_y_funciones_reserva_id
FOREIGN KEY (reserva_id)
REFERENCES Reservas_PTC(reserva_id),
CONSTRAINT fk_Reserva_y_funciones_funcion_id
FOREIGN KEY (funcion_id)
REFERENCES Funciones(funcion_id)
)

CREATE TABLE Funcion_y_sala(
funcion_id INT,
CONSTRAINT fk_Funcion_y_sala_funcion_id
FOREIGN KEY (funcion_id)
REFERENCES Funciones(funcion_id),
sala_id INT,
CONSTRAINT fk_Funcion_y_sala_sala_id
FOREIGN KEY (sala_id)
REFERENCES Salas_PTC(sala_id)
)

CREATE TABLE Sala_y_asiento(
sala_id INT,
CONSTRAINT fk_Sala_y_asiento_sala_id
FOREIGN KEY (sala_id)
REFERENCES Salas_PTC(sala_id),
asiento_id INT,
CONSTRAINT fk_Sala_y_asiento_asiento_id
FOREIGN KEY (asiento_id)
REFERENCES Asientos(asiento_id)
)

CREATE TABLE Funcion_y_pelicula(
funcion_id INT,
CONSTRAINT fk_Funcion_y_pelicula_funcion_id
FOREIGN KEY (funcion_id)
REFERENCES Funciones(funcion_id),
pelicula_id INT,
CONSTRAINT fk_Funcion_y_pelicula_pelicula_id
FOREIGN KEY (pelicula_id) 
REFERENCES Peliculas(pelicula_id)
)
--Inserts--
INSERT INTO Roles (nombre) VALUES ('cliente'), ('empleado'), ('administrador');


--Selects--
