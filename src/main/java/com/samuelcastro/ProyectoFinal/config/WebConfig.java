package com.samuelcastro.ProyectoFinal.config;

import com.samuelcastro.ProyectoFinal.utils.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebConfig es una clase de configuración de Spring que implementa la interfaz WebMvcConfigurer.
 * Esta clase se utiliza para personalizar la configuración de Spring MVC, incluyendo la
 * adición de formateadores personalizados.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    // Inyección del formateador de fechas personalizado
    @Autowired
    private DateFormatter dateFormatter;

    /**
     * Método para registrar formateadores personalizados.
     *
     * @param registry el registro de formateadores donde se añaden los formateadores personalizados
     */
    @Override
    public void addFormatters(@SuppressWarnings("null") FormatterRegistry registry) {
        // Registrar el formateador de fechas personalizado
        registry.addFormatter(dateFormatter);
    }
}
