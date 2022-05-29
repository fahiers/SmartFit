package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.Id;

import com.google.cloud.firestore.DocumentReference;

import lombok.ToString;


@Entity
@ToString
public class Usuario{

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
	private DocumentReference sede;
	@OneToOne
	private DocumentReference salaActual;
	
	
	public Usuario() {
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
	public DocumentReference getSede() {
		return sede;
	}
	public void setSede(DocumentReference sede) {
		this.sede = sede;
	}
	public DocumentReference getSalaActual() {
		return salaActual;
	}
	public void setSalaActual(DocumentReference salaActual) {
		this.salaActual = salaActual;
	}
	
}
