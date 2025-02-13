package com.samuelcastro.ProyectoFinal.editors;

import org.springframework.web.multipart.MultipartFile;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

public class MultipartFileToByteArrayEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        // No se necesita implementación
    }

    @Override
    public void setValue(Object value) {
        if (value instanceof MultipartFile) {
            MultipartFile multipartFile = (MultipartFile) value;
            try {
                super.setValue(multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
                super.setValue(null);
            }
        } else {
            super.setValue(null);
        }
    }
}