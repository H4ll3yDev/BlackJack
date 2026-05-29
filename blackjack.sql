-- ============================================================
--  BLACKJACK - Script de creación de base de datos
--  Ejecutar en MySQL antes de lanzar el proyecto
-- ============================================================

CREATE DATABASE IF NOT EXISTS blackjack
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE blackjack;

-- ------------------------------------------------------------
--  TABLA: jugador
--  Registro de usuarios. nombre_usuario es clave única (login).
--  saldo = fichas disponibles del jugador.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS jugador (
    id               INT          AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario   VARCHAR(50)  NOT NULL UNIQUE,
    contrasena       VARCHAR(255) NOT NULL,
    saldo            INT          NOT NULL DEFAULT 1000,
    partidas_ganadas INT          NOT NULL DEFAULT 0,
    fecha_registro   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
--  TABLA: ficha
--  Denominaciones de fichas disponibles en el casino.
--  Se cargan desde aquí para mostrar las opciones de apuesta.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS ficha (
    id         INT         AUTO_INCREMENT PRIMARY KEY,
    valor      INT         NOT NULL,        -- valor en unidades (5, 10, 25, 50, 100)
    color      VARCHAR(20) NOT NULL         -- color identificativo de la ficha
);

-- ------------------------------------------------------------
--  TABLA: carta
--  Baraja estándar de 52 cartas. Se carga al inicio de partida.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS carta (
    id     INT         AUTO_INCREMENT PRIMARY KEY,
    valor  VARCHAR(5)  NOT NULL,   -- 'A','2','3',...,'10','J','Q','K'
    palo   VARCHAR(20) NOT NULL,   -- 'Corazones','Diamantes','Treboles','Picas'
    puntos INT         NOT NULL    -- valor numérico en Blackjack (A=11, figuras=10)
);

-- ------------------------------------------------------------
--  TABLA: partida
--  Cabecera de cada ronda jugada.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS partida (
    id            INT       AUTO_INCREMENT PRIMARY KEY,
    num_jugadores INT       NOT NULL,
    fecha         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ------------------------------------------------------------
--  TABLA: resultado
--  Resultado de cada jugador por partida.
--  apuesta = fichas apostadas en esa ronda.
-- ------------------------------------------------------------
CREATE TABLE IF NOT EXISTS resultado (
    id          INT     AUTO_INCREMENT PRIMARY KEY,
    partida_id  INT     NOT NULL,
    jugador_id  INT     NOT NULL,
    puntuacion  INT     NOT NULL DEFAULT 0,
    apuesta     INT     NOT NULL DEFAULT 0,
    es_ganador  BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_resultado_partida FOREIGN KEY (partida_id) REFERENCES partida(id),
    CONSTRAINT fk_resultado_jugador FOREIGN KEY (jugador_id) REFERENCES jugador(id)
);

-- ============================================================
--  DATOS INICIALES
-- ============================================================

-- Fichas del casino
INSERT INTO ficha (valor, color) VALUES
    (5,   'Blanco'),
    (10,  'Rojo'),
    (25,  'Verde'),
    (50,  'Azul'),
    (100, 'Negro');

-- Baraja completa: 52 cartas (4 palos x 13 valores)
INSERT INTO carta (valor, palo, puntos) VALUES
-- CORAZONES
('A','Corazones',11),('2','Corazones',2), ('3','Corazones',3),
('4','Corazones',4), ('5','Corazones',5), ('6','Corazones',6),
('7','Corazones',7), ('8','Corazones',8), ('9','Corazones',9),
('10','Corazones',10),('J','Corazones',10),('Q','Corazones',10),
('K','Corazones',10),
-- DIAMANTES
('A','Diamantes',11),('2','Diamantes',2), ('3','Diamantes',3),
('4','Diamantes',4), ('5','Diamantes',5), ('6','Diamantes',6),
('7','Diamantes',7), ('8','Diamantes',8), ('9','Diamantes',9),
('10','Diamantes',10),('J','Diamantes',10),('Q','Diamantes',10),
('K','Diamantes',10),
-- TREBOLES
('A','Treboles',11), ('2','Treboles',2),  ('3','Treboles',3),
('4','Treboles',4),  ('5','Treboles',5),  ('6','Treboles',6),
('7','Treboles',7),  ('8','Treboles',8),  ('9','Treboles',9),
('10','Treboles',10),('J','Treboles',10), ('Q','Treboles',10),
('K','Treboles',10),
-- PICAS
('A','Picas',11),    ('2','Picas',2),     ('3','Picas',3),
('4','Picas',4),     ('5','Picas',5),     ('6','Picas',6),
('7','Picas',7),     ('8','Picas',8),     ('9','Picas',9),
('10','Picas',10),   ('J','Picas',10),    ('Q','Picas',10),
('K','Picas',10);
