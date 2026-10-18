-- Funcionalidades pastorales: bitacora, confirmacion de turnos, calendario, push,
-- envios automaticos (cumpleanos, recordatorio TCD) y resumen semanal.
-- MySQL 8. Ejecutar UNA sola vez (produccion usa ddl-auto=none).

CREATE TABLE IF NOT EXISTS auditoria (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    fecha       DATETIME(6)  NOT NULL,
    id_usuario  INT          NULL,
    usuario     VARCHAR(150) NULL,
    accion      VARCHAR(50)  NOT NULL,
    entidad     VARCHAR(50)  NULL,
    id_entidad  VARCHAR(50)  NULL,
    descripcion VARCHAR(1000) NULL,
    ip          VARCHAR(45)  NULL,
    PRIMARY KEY (id),
    KEY idx_auditoria_fecha (fecha),
    KEY idx_auditoria_accion_fecha (accion, fecha)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS push_suscripcion (
    id             INT          NOT NULL AUTO_INCREMENT,
    id_persona     INT          NOT NULL,
    endpoint       VARCHAR(500) NOT NULL,
    p256dh         VARCHAR(200) NOT NULL,
    auth           VARCHAR(100) NOT NULL,
    user_agent     VARCHAR(255) NULL,
    fecha_creacion DATETIME(6)  NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_push_endpoint (endpoint),
    KEY idx_push_persona (id_persona)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS envio_automatico (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    tipo        VARCHAR(40) NOT NULL,
    id_persona  INT         NOT NULL,
    fecha       DATE        NOT NULL,
    canales     VARCHAR(60) NULL,
    fecha_envio DATETIME(6) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_envio_tipo_persona_fecha (tipo, id_persona, fecha)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS resumen_semanal (
    id             INT        NOT NULL AUTO_INCREMENT,
    semana_inicio  DATE       NOT NULL,
    semana_fin     DATE       NOT NULL,
    contenido      MEDIUMTEXT NOT NULL,
    fecha_creacion DATETIME(6) NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_resumen_semana (semana_inicio)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

-- Confirmacion del turno por parte del servidor
ALTER TABLE servicio
    ADD COLUMN confirmacion        VARCHAR(20)  NULL,
    ADD COLUMN motivo_confirmacion VARCHAR(300) NULL,
    ADD COLUMN fecha_confirmacion  DATETIME(6)  NULL;

-- Fecha de registro (crecimiento) y token privado del calendario
ALTER TABLE persona
    ADD COLUMN fecha_registro   DATETIME(6) NULL,
    ADD COLUMN calendario_token VARCHAR(64) NULL,
    ADD UNIQUE KEY uk_persona_calendario_token (calendario_token);
