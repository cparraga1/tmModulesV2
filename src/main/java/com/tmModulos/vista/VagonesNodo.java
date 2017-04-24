package com.tmModulos.vista;

import com.tmModulos.modelo.entity.tmData.Nodo;
import com.tmModulos.modelo.entity.tmData.Vagon;

import java.util.List;


public class VagonesNodo {

    private Vagon vagon;
    private List<Nodo> nodos;

    public VagonesNodo() {
    }

    public VagonesNodo(Vagon vagon, List<Nodo> nodos) {
        this.vagon = vagon;
        this.nodos = nodos;
    }

    public Vagon getVagon() {
        return vagon;
    }

    public void setVagon(Vagon vagon) {
        this.vagon = vagon;
    }

    public List<Nodo> getNodos() {
        return nodos;
    }

    public void setNodos(List<Nodo> nodos) {
        this.nodos = nodos;
    }
}
