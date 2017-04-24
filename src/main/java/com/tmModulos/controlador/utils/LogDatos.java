package com.tmModulos.controlador.utils;



public class LogDatos {

    private String mensaje;
    private TipoLog tipoLog;

    public LogDatos() {
    }

    public LogDatos(String mensaje, TipoLog tipoLog) {
        this.mensaje = mensaje;
        this.tipoLog = tipoLog;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public TipoLog getTipoLog() {
        return tipoLog;
    }

    public void setTipoLog(TipoLog tipoLog) {
        this.tipoLog = tipoLog;
    }
}
