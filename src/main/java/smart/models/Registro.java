package smart.models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.google.cloud.firestore.DocumentReference;

@Entity
public class Registro {
	private String id;
	private Date fecha;
	private String tipo;
	@ManyToOne 
	private DocumentReference usuario;
	@ManyToOne
	private DocumentReference sede;
	@ManyToOne
	private DocumentReference sala;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public DocumentReference getUsuario() {
		return usuario;
	}
	public void setUsuario(DocumentReference usuario) {
		this.usuario = usuario;
	}
	public DocumentReference getSede() {
		return sede;
	}
	public void setSede(DocumentReference sede) {
		this.sede = sede;
	}
	public DocumentReference getSala() {
		return sala;
	}
	public void setSala(DocumentReference sala) {
		this.sala = sala;
	}
	
	
}
