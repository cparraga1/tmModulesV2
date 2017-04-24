package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="tipo_dia_detalle")
public class TipoDiaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="TipoDiaDetalleGenerator")
    @SequenceGenerator(name="TipoDiaDetalleGenerator", sequenceName = "tipo_dia_detalle_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "codigo")
    private Integer codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_dia", nullable = false)
    private TipoDia tipoDia;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tipoDiaByArco")
    private Set<ArcoTiempo> arcoTiempoRecords = new HashSet<ArcoTiempo>(0);


    public TipoDiaDetalle(String detalle, TipoDia tipoDia) {
        this.detalle = detalle;
        this.tipoDia = tipoDia;
    }

    public TipoDiaDetalle() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public TipoDia getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(TipoDia tipoDia) {
        this.tipoDia = tipoDia;
    }

    public Set<ArcoTiempo> getArcoTiempoRecords() {
        return arcoTiempoRecords;
    }

    public void setArcoTiempoRecords(Set<ArcoTiempo> arcoTiempoRecords) {
        this.arcoTiempoRecords = arcoTiempoRecords;
    }
}
