package com.tmModulos.controlador.procesador;

import com.tmModulos.modelo.entity.tmData.Expediciones;
import com.tmModulos.modelo.entity.tmData.ExpedicionesTemporal;


public class auxEp {

    private Expediciones mayorExp;
    private Expediciones menorExp;

    public auxEp() {
    }

    public Expediciones getMayorExp() {
        return mayorExp;
    }

    public void setMayorExp(Expediciones mayorExp) {
        this.mayorExp = mayorExp;
    }

    public Expediciones getMenorExp() {
        return menorExp;
    }

    public void setMenorExp(Expediciones menorExp) {
        this.menorExp = menorExp;
    }
}
