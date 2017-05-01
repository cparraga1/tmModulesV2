package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name="matriz_distancia")
public class MatrizTemporal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="MatrizTGenerator")
    @SequenceGenerator(name="MatrizTGenerator", sequenceName = "matriz_temporal_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "instante")
    private String instante;

    @Column(name = "linea")
    private BigInteger linea;

    @Column(name = "sublinea")
    private BigInteger sublinea;

    @Column(name = "ruta")
    private BigInteger ruta;

    @Column(name = "punto")
    private BigInteger punto;

    public MatrizTemporal() {
    }

    public String getInstante() {
        return instante;
    }

    public void setInstante(String instante) {
        this.instante = instante;
    }

    public BigInteger getLinea() {
        return linea;
    }

    public void setLinea(BigInteger linea) {
        this.linea = linea;
    }

    public BigInteger getSublinea() {
        return sublinea;
    }

    public void setSublinea(BigInteger sublinea) {
        this.sublinea = sublinea;
    }

    public BigInteger getRuta() {
        return ruta;
    }

    public void setRuta(BigInteger ruta) {
        this.ruta = ruta;
    }

    public BigInteger getPunto() {
        return punto;
    }

    public void setPunto(BigInteger punto) {
        this.punto = punto;
    }
}


