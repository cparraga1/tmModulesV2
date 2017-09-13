package com.tmModulos.controlador.utils;

import com.tmModulos.modelo.entity.tmData.IntervalosProgramacion;
import com.tmModulos.modelo.entity.tmData.TempHorario;

import java.util.List;

public class FranjaCuartos {

    private IntervalosProgramacion intervalosProgramacion;
    private List<TempHorario> tablaHorario;
    private List<Double> diffTiempos;

    public FranjaCuartos() {
    }

    public IntervalosProgramacion getIntervalosProgramacion() {
        return intervalosProgramacion;
    }

    public void setIntervalosProgramacion(IntervalosProgramacion intervalosProgramacion) {
        this.intervalosProgramacion = intervalosProgramacion;
    }

    public List<TempHorario> getTablaHorario() {
        return tablaHorario;
    }

    public void setTablaHorario(List<TempHorario> tablaHorario) {
        this.tablaHorario = tablaHorario;
    }

    public List<Double> getDiffTiempos() {
        return diffTiempos;
    }

    public void setDiffTiempos(List<Double> diffTiempos) {
        this.diffTiempos = diffTiempos;
    }
}
