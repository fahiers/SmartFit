package smart.models;

import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

import org.springframework.data.annotation.Id;

import com.google.cloud.firestore.DocumentReference;

@Entity
public class Sede {
	
	@Id
	private String Id;
	private String nombre;
	private String direccion;
	private String region;
	private int aforo;
	private LinkedList<DocumentReference> salas;
	
	public Sede() {
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
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
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}

	public LinkedList<DocumentReference> getSalas() {
		return salas;
	}

	public void setSalas(LinkedList<DocumentReference> salas) {
		this.salas = salas;
	}
	
}
