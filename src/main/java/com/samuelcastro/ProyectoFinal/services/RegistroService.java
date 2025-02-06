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

    public List<Registro> findAll() {
        return registroRepository.findAll();
    }

    public Registro findById(int id) {
        return registroRepository.findById(id).orElse(null);
    }

    public Registro save(Registro registro) {
        return registroRepository.save(registro);
    }

    public void deleteById(int id) {
        registroRepository.deleteById(id);
    }
}