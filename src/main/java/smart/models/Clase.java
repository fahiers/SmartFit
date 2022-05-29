package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;

import org.springframework.data.annotation.Id;

@Entity
public class Clase {

	@Id
	private String id;
	private String nombre;
	private String duracion;
	private String horario;
	private LinkedList<LinkedList<String>> descripcion;
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
	public String getDuracion() {
		return duracion;
	}
	public void setDuracion(String duracion) {
		this.duracion = duracion;
	}
	public String getHorario() {
		return horario;
	}
	public void setHorario(String horario) {
		this.horario = horario;
	}
	public LinkedList<LinkedList<String>> getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(LinkedList<LinkedList<String>> descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
