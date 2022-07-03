package smart.models;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import org.springframework.data.annotation.Id;

import com.google.cloud.firestore.DocumentReference;

import lombok.ToString;

@Entity
@ToString
public class Equipo {
	@Id
	private String id;
	private String nombre;
	private String tipo;
	private String estado;
	@ManyToMany(fetch=FetchType.LAZY)
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public DocumentReference getSala() {
		return sala;
	}
	public void setSala(DocumentReference sala) {
		this.sala = sala;
	}
	
	
}
