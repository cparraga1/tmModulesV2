package com.tmModulos.controlador.servicios;


import com.tmModulos.modelo.dao.tmData.UsuarioDao;
import com.tmModulos.modelo.entity.tmData.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("UsuariosService")
public class UsuarioServicios {

    @Autowired
    public UsuarioDao usuarioDao;

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
}
