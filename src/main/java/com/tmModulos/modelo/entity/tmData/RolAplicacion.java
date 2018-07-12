package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="rol_aplicacion")
public class RolAplicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="rolAplicacionGenerator")
    @SequenceGenerator(name="rolAplicacionGenerator", sequenceName = "rol_aplicacion_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "rol", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "aplicacion", nullable = false)
    private Aplicacion aplicacion;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "usuario", nullable = false)
    private Usuario usuario;

    public RolAplicacion() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Aplicacion getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(Aplicacion aplicacion) {
        this.aplicacion = aplicacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
