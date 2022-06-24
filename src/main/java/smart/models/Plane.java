package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;

@Entity
public class Plane {

	@Id
	private String id;
	private String nombre;
	private int inscripcion;
	private int mensualidad;
	private int mantenimiento;
	private LinkedList<String> caracteristicas;

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
		this.nombre = nombre;
	}
	public int getPrecio() {
		return mensualidad;
	}
	public void setPrecio(int precio) {
		this.mensualidad = precio;
	}
	public LinkedList<String> getCaracteristicas() {
		return caracteristicas;
	}
	public void setCaracteristicas(LinkedList<String> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	public int getMensualidad() {
		return mensualidad;
	}
	public void setMensualidad(int mensualidad) {
		this.mensualidad = mensualidad;
	}
	public int getMantenimiento() {
		return mantenimiento;
	}
	public void setMantenimiento(int mantenimiento) {
		this.mantenimiento = mantenimiento;
	}
	public int getInscripcion() {
		return inscripcion;
	}
	public void setInscripcion(int inscripcion) {
		this.inscripcion = inscripcion;
	}
	
	
}
