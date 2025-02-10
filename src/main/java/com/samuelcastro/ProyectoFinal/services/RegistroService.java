package com.samuelcastro.ProyectoFinal.services;

import com.samuelcastro.ProyectoFinal.entities.Registro;
import com.samuelcastro.ProyectoFinal.repositories.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegistroService {

    @Autowired
    private RegistroRepository registroRepository;

    public Registro save(Registro registro) {
        return registroRepository.save(registro);
    }

    public void registrarOperacion(String entidad, int entidadId, String operacion, String detalles) {
        Registro registro = new Registro();
        registro.setEntidad(entidad);
        registro.setEntidadId(entidadId);
        registro.setOperacion(operacion);
        registro.setDetalles(detalles);
        save(registro);
    }

    public List<Registro> findAll() {
        return registroRepository.findAll();
    }
}
