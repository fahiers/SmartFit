package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.Id;

import lombok.ToString;


@Entity
@ToString
public class Usuario{
	
	
	private String id;
	private String nombre;
	private String apellido;
	private String correo;
	private String password;
	@Id
	private String rut;
	private int errorLogin;
	private boolean activo;
	private LinkedList<String> roles;
	private LinkedList<String> permisos;
	@OneToOne
	private Sede sede;
	
	public Usuario() {
	}
	public Usuario(String nombre, String apellido, String correo, String password, String rut, int errorLogin,
			boolean activo, LinkedList<String> roles, LinkedList<String> permisos) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.correo = correo;
		this.password = password;
		this.rut = rut;
		this.errorLogin = errorLogin;
		this.activo = activo;
		this.roles = roles;
		this.permisos = permisos;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre.toLowerCase();
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido.toLowerCase();
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRut() {
		return rut;
	}
	public void setRut(String rut) {
		this.rut = rut;
		this.id = rut;
	}
	public int getErrorLogin() {
		return errorLogin;
	}
	public void setErrorLogin(int errorLogin) {
		this.errorLogin = errorLogin;
	}
	public boolean isActivo() {
		return activo;
	}
	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	public LinkedList<String> getRoles() {
		return roles;
	}
	public void setRoles(LinkedList<String> roles) {
		this.roles = roles;
	}
	public LinkedList<String> getPermisos() {
		return permisos;
	}
	public void setPermisos(LinkedList<String> permisos) {
		this.permisos = permisos;
	}
	public Sede getSede() {
		return sede;
	}
	public void setSede(Sede sede) {
		this.sede = sede;
	}
	
}
