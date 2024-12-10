package com.example.mislibros.model;

public class LogEvento {
    private String DescripcionEvento;
    private String OrigenEvento;
    private String UsuarioEntidad;

    public LogEvento(){}
    public LogEvento(String descripcionEvento, String origenEvento, String usuarioEntidad) {
        DescripcionEvento = descripcionEvento;
        OrigenEvento = origenEvento;
        UsuarioEntidad = usuarioEntidad;
    }

    public String getDescripcionEvento() {
        return DescripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        DescripcionEvento = descripcionEvento;
    }

    public String getOrigenEvento() {
        return OrigenEvento;
    }

    public void setOrigenEvento(String origenEvento) {
        OrigenEvento = origenEvento;
    }

    public String getUsuarioEntidad() {
        return UsuarioEntidad;
    }

    public void setUsuarioEntidad(String usuarioEntidad) {
        UsuarioEntidad = usuarioEntidad;
    }
}
