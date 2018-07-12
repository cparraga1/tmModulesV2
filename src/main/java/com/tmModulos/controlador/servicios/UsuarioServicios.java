package com.tmModulos.controlador.servicios;


import com.tmModulos.modelo.dao.tmData.AplicacionDao;
import com.tmModulos.modelo.dao.tmData.RolAplicacionDao;
import com.tmModulos.modelo.dao.tmData.UsuarioDao;
import com.tmModulos.modelo.entity.tmData.Aplicacion;
import com.tmModulos.modelo.entity.tmData.RolAplicacion;
import com.tmModulos.modelo.entity.tmData.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service("UsuariosService")
public class UsuarioServicios implements Serializable{

    @Autowired
    public UsuarioDao usuarioDao;

    @Autowired
    public AplicacionDao aplicacionDao;

    @Autowired
    public RolAplicacionDao rolAplicacionDao;

    public void addUsuario(Usuario usuario) {
        usuarioDao.addUsuario(usuario);
    }

    public void deleteUsuario(Usuario usuario) {
       usuarioDao.deleteUsuario(usuario);
    }


    public void updateUsuario(Usuario usuario) {
        usuarioDao.updateUsuario(usuario);
    }


    public List<Usuario> getUsuarioAll() {
        return usuarioDao.getUsuarioAll();
    }

    public Usuario encontrarUsuarioByNombreUsuario(String user){
        return usuarioDao.encontrarUsuarioByNombreUsuario(user);
    }

    public Aplicacion getAplicacion(int idAplicacion) {
        return aplicacionDao.obtenerAplicacionById(idAplicacion);
    }

    public RolAplicacion getRolUsuarioAplicacion(Aplicacion aplicacion, Usuario usuario) {
        return rolAplicacionDao.getRolAplicacion(aplicacion,usuario);
    }
}
