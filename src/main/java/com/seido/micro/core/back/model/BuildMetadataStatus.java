package com.seido.micro.core.back.model;

import lombok.Getter;

/**
 * Define the values of the status of a Build Jenkins
 */
@Getter
public enum BuildMetadataStatus {
    /**
     * En la API de Jenkins, los trabajos (jobs) pueden tener varios estados según su estado actual.
     * Aquí hay algunos de los estados comunes que puedes encontrar:
     *
     *     SUCCESS (Éxito): El trabajo se ha ejecutado correctamente sin errores.
     *
     *     FAILURE (Fallo): El trabajo se ha ejecutado pero ha encontrado algún error durante la ejecución.
     *
     *     UNSTABLE (Inestable): El trabajo se ha ejecutado, pero algunos aspectos del mismo no han cumplido con
     *     los criterios establecidos para considerarse exitoso. Por ejemplo, puede haber fallado un conjunto de
     *     pruebas, pero no lo suficiente como para clasificarlo como un fallo completo.
     *
     *     ABORTED (Cancelado): El trabajo se ha cancelado antes de que pueda completarse.
     *
     *     NOT_BUILT (No construido): El trabajo no se ha construido. Esto puede deberse a diversas razones, como
     *     la configuración del trabajo o la falta de cambios desde la última ejecución.
     *
     *     IN_PROGRESS (En progreso): El trabajo se está ejecutando actualmente.
     *
     *     QUEUED (En cola): El trabajo está esperando su turno para ejecutarse.
     *
     *     UNKNOWN (Desconocido): El estado no puede determinarse o no se proporciona.
     */
    SUCCESS("1","Éxito"),
    FAILURE("2","Fallo"),
    STABLE("4","Estable"),
    UNSTABLE("5","Inestable"),
    ABORTED("6","Cancelado"),
    NOT_BUILT("7","No construido"),
    IN_PROGRESS("8","En progreso. En construcción"),
    QUEUED("9","En cola"),
    UNKNOWN("10","Desconocido")
    ;



    private String codigo;
    private String descripcion;

    private BuildMetadataStatus(String codigo, String descripcion){
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

}
