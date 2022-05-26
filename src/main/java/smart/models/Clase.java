package smart.models;

import java.util.LinkedList;

import org.springframework.data.annotation.Id;

public class Clase {

	@Id
	private String id;
	private String nombre;
	private String duracion;
	private String horario;
	private LinkedList<LinkedList<String>> descripcion;
}
