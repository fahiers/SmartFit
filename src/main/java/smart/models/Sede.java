package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import org.springframework.data.annotation.Id;

@Entity
public class Sede {
	
	@Id
	private String id;
	private String direccion;
	private int aforo;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private LinkedList<Sala> salas;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public int getAforo() {
		return aforo;
	}
	public void setAforo(int aforo) {
		this.aforo = aforo;
	}
	public LinkedList<Sala> getSalas() {
		return salas;
	}
	public void setSalas(LinkedList<Sala> salas) {
		this.salas = salas;
	}
}
