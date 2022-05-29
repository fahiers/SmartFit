package smart.models;


import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.Id;

import com.google.cloud.firestore.DocumentReference;

@Entity
public class Sala {
	
	@Id
	private String id;
	private String nombre;
	private String estado;
	private String tipo;
	private int aforo;
	@ManyToMany(fetch=FetchType.LAZY)
	private LinkedList<DocumentReference> profesores;
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private DocumentReference sede;
	
	public LinkedList<DocumentReference> getProfesores() {
		return profesores;
	}
	public void setProfesores(LinkedList<DocumentReference> profesores) {
		this.profesores = profesores;
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
		this.nombre = nombre;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public DocumentReference getSede() {
		return sede;
	}
	public void setSede(DocumentReference sede) {
		this.sede = sede;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public int getAforo() {
		return aforo;
	}
	public void setAforo(int aforo) {
		this.aforo = aforo;
	}
	
	
}
