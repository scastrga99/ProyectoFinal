package com.samuelcastro.ProyectoFinal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarRecordatorio(String destinatario, String nombrePersona, String tituloLibro, String fechaDevolucion) {
        try {
            SimpleMailMessage mensaje = new SimpleMailMessage();
            mensaje.setTo(destinatario);
            mensaje.setSubject("Recordatorio de Devolución de Libro");
            mensaje.setText("Estimado " + nombrePersona + ",\n\nLe recordamos que debe devolver el libro titulado " + tituloLibro + " con fecha de devolucion el dia " + fechaDevolucion +".\n\nGracias.");
            mailSender.send(mensaje);
            System.out.println("Correo de recordatorio enviado a: " + destinatario);
        } catch (Exception e) {
            System.err.println("Error enviando correo de recordatorio: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void enviarNuevaContraseña(String destinatario, String nuevaContraseña) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Nueva Contraseña");
        mensaje.setText("Su nueva contraseña es: " + nuevaContraseña + "\n\nPor favor, cambie su contraseña después de iniciar sesión.");
        mailSender.send(mensaje);
    }
}