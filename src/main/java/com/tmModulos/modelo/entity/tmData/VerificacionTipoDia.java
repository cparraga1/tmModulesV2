package com.tmModulos.modelo.entity.tmData;


import javax.persistence.*;

@Entity
@Table(name="verificacion_tipo_dia")
public class VerificacionTipoDia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="VerifiTipoGenerator")
    @SequenceGenerator(name="VerifiTipoGenerator", sequenceName = "verificacion_tipo_dia_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "tipo_dia")
    private String tipoDia;

    @Column(name = "nombre_verificador")
    private String archivo;

    public VerificacionTipoDia() {
    }

    public String getTipoDia() {
        return tipoDia;
    }

    public void setTipoDia(String tipoDia) {
        this.tipoDia = tipoDia;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }
}
