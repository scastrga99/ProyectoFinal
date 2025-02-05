package com.samuelcastro.ProyectoFinal.utils;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DateFormatter es un componente de Spring que implementa la interfaz Formatter para convertir
 * cadenas de texto en objetos Date y viceversa. Este formateador se utiliza para manejar la
 * conversión de fechas en formularios HTML y otros contextos donde se necesite convertir
 * entre cadenas y fechas.
 */
@Component
public class DateFormatter implements Formatter<Date> {

    // Formato de fecha utilizado para la conversión
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * Convierte una cadena de texto en un objeto Date.
     *
     * @param text   la cadena de texto que representa la fecha
     * @param locale el locale actual
     * @return el objeto Date resultante de la conversión
     * @throws ParseException si la cadena de texto no puede ser convertida en una fecha
     */
    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.parse(text);
    }

    /**
     * Convierte un objeto Date en una cadena de texto.
     *
     * @param object el objeto Date que se va a convertir
     * @param locale el locale actual
     * @return la cadena de texto resultante de la conversión
     */
    @Override
    public String print(Date object, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        return dateFormat.format(object);
    }
}
