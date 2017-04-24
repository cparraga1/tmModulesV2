package com.tmModulos.vista;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

@ManagedBean(name="user")
@SessionScoped
public class UserBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public void upl() {
		System.out.println("entre");
	}
}