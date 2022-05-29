package smart.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Id;

@Entity
public class Equipo {
	@Id
	private String id;
	private String nombre;
	private String tipo;
	private String estado;
	@ManyToMany(fetch=FetchType.LAZY)
	private Sala sala;
}
