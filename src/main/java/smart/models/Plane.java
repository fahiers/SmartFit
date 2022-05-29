package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;

@Entity
public class Plane {

	@Id
	private String id;
	private String nombre;
	private int precio;
	private LinkedList<LinkedList<String>> caracteristicas;
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
		return precio;
	}
	public void setPrecio(int precio) {
		this.precio = precio;
	}
	public LinkedList<LinkedList<String>> getCaracteristicas() {
		return caracteristicas;
	}
	public void setCaracteristicas(LinkedList<LinkedList<String>> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
	
}
