package smart.models;

import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.Id;

public class Sala {
	
	@Id
	private String id;
	private String estado;
	private String tipo;
	@OneToMany(fetch=FetchType.LAZY)
	private Usuario profesor;
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Sede sede;
	@ManyToMany(fetch=FetchType.LAZY)
	private LinkedList<Equipo> equipos;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public Usuario getProfesor() {
		return profesor;
	}
	public void setProfesor(Usuario profesor) {
		this.profesor = profesor;
	}
	public Sede getSede() {
		return sede;
	}
	public void setSede(Sede sede) {
		this.sede = sede;
	}
	public LinkedList<Equipo> getEquipos() {
		return equipos;
	}
	public void setEquipos(LinkedList<Equipo> equipos) {
		this.equipos = equipos;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
