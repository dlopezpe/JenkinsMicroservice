package com.seido.micro.core.back.model;

import lombok.Getter;

/**
 * Define the values of the status of a Build Jenkins
 */
@Getter
public enum BuildMetadataStatus {
    NEW("1","NEW"),
    STARTED("2","STARTED"),
    WAITING("3","WAITING"),
    FINISHED("4","FINISHED"),
    DONE("5","DONE"),
    ERROR("6","ERROR"),
    PAUSED("7","PAUSED")
        ;
    private String codigo;
    private String descripcion;

    private BuildMetadataStatus(String codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

}
