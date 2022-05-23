package smart.servicios;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class UsuariosService extends CRUDServices implements CollectFS{

	@Override
	@PostConstruct
	public void setColeccion() {
		this.coleccion = this.getClass().getSimpleName().replaceAll("Service","").toLowerCase();
	}
}
