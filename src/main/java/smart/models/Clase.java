package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Id;

import com.google.cloud.firestore.DocumentReference;

@Entity
public class Clase {

	@Id
	private String id;
	private String nombre;
	private String duracion;
	private LinkedList<String> horarios;
	private String descripcion;
	@ManyToOne(fetch=FetchType.LAZY)
	private DocumentReference sala;
	
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
	public LinkedList<String> getHorarios() {
		return horarios;
	}
	public void setHorarios(LinkedList<String> horarios) {
		this.horarios = horarios;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public DocumentReference getSala() {
		return sala;
	}
	public void setSala(DocumentReference sala) {
		this.sala = sala;
	}
	
	
}
