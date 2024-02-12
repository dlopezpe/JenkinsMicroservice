package com.seido.micro.core.utils;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class utils
 */
public class Utils{

    private Utils() {throw new IllegalStateException("Utility class");}

    public static String getTimeStamp(){
        // Obtener la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();

        // Definir el formato deseado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Formatear la fecha y la hora
        String formattedDateTime = now.format(formatter);
        return formattedDateTime;
    }
}
