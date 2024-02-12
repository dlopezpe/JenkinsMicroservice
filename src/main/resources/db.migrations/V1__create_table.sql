create type state_type as enum ('NEW', 'STARTED', 'WAITING', 'FINISHED', 'DONE', 'ERROR', 'PAUSED');

CREATE TABLE build_compilation (
    id BIGSERIAL NOT NULL PRIMARY KEY, --Identificacion de la compilacion (número)
    job_name VARCHAR(255) NOT NULL, --Nombre del build (cadena - solo letras)
    build_number INTEGER NOT NULL, --Número del build
    path_repo VARCHAR(255) NOT NULL, -- Path del repositorio (cadena con barras (\))
    version VARCHAR(15) NOT NULL,--versión (M.m.f)
    status_build state_type NOT NULL, --Estado del build
    created_at timestamp NOT NULL,
    modified_at timestamp
);
create unique index "build_compilation_idx" on "build_compilation" ("id");
