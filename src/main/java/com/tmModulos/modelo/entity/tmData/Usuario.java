package com.tmModulos.modelo.entity.tmData;

import javax.persistence.*;

@Entity
@Table(name="usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="UsuarioGenerator")
    @SequenceGenerator(name="UsuarioGenerator", sequenceName = "usuario_id_seq",allocationSize=1)
    @Column(name = "id")
    private long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "usuario")
    private String usuario;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "area")
    private String area;

    public Usuario() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
