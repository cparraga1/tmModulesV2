package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="RoleGenerator")
    @SequenceGenerator(name="RoleGenerator", sequenceName = "role_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "leer")
    private boolean permisoLeer;

    @Column(name = "editar")
    private boolean permisoEscribir;

    @Column(name = "eliminar")
    private boolean permisoEliminar;

    @Transient
    private long idNuevo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role", cascade = CascadeType.REMOVE)
    private Set<Usuario> usuariosRecords= new HashSet<Usuario>(0);

    public Role() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPermisoLeer() {
        return permisoLeer;
    }

    public void setPermisoLeer(boolean permisoLeer) {
        this.permisoLeer = permisoLeer;
    }

    public boolean isPermisoEscribir() {
        return permisoEscribir;
    }

    public void setPermisoEscribir(boolean permisoEscribir) {
        this.permisoEscribir = permisoEscribir;
    }

    public boolean isPermisoEliminar() {
        return permisoEliminar;
    }

    public void setPermisoEliminar(boolean permisoEliminar) {
        this.permisoEliminar = permisoEliminar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdNuevo() {
        return idNuevo;
    }

    public void setIdNuevo(long idNuevo) {
        this.idNuevo = idNuevo;
    }
}
