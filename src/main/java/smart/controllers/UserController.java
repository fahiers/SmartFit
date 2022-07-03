package smart.controllers;

import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.google.cloud.firestore.DocumentReference;

import smart.servicios.CRUDServices;
import smart.models.Registro;
import smart.models.Sala;
import smart.models.Sede;
import smart.models.Usuario;
import smart.security.UserPrincipal;

@RestController
@RequestMapping("/usuarios")
public class UserController {
	
	@Autowired
	PasswordEncoder pass;
	
	private CRUDServices crudService;
	private Usuario user;
	public UserController() {
	}
	
	@GetMapping("/crearUsuario")
	public ModelAndView usuario(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "usuarios:crear");
        Usuario newUser = new Usuario();
        model.addAttribute("newUser", newUser);
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);
        return new ModelAndView("home");
	}
	
	@GetMapping("/verUsuarios")
	public ModelAndView verUsuarios(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "usuarios:ver");
        LinkedList<Object> objetos = crudService.getAllDocs(Usuario.class);
        LinkedList<Usuario> usuarios = new LinkedList<>();
        for (Object usuario : objetos) {
			usuarios.add((Usuario) usuario);
		}
        model.addAttribute("allUsers",usuarios);
        model.addAttribute("claseSede",Sede.class);
        return new ModelAndView("home");
	}
	
	@PostMapping("/addUser")
	public String create(@ModelAttribute Usuario user) throws InterruptedException, ExecutionException {
		user.setPassword(pass.encode(user.getPassword()));
		user.setSede(crudService.getDocRef("sedes", user.getRut().split(":")[1]));
		user.setRut(user.getRut().split(":")[0]);
		String respuesta= crudService.create(user,user.getRut());
		return respuesta;
	}
	
	@PostMapping("/readUser")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.read(id,Usuario.class);
	}
	@PostMapping("/editUser")
	public ModelAndView edit(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Usuario user = (Usuario) crudService.read(id, Usuario.class);
        model.addAttribute("Usuario", user);
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);
        model.addAttribute("sedeId",user.getSede().getId());
        return new ModelAndView("fragments/usuarios/editarUsuario");
	}
	@PostMapping("/updateUser")
	public String update(@ModelAttribute Usuario user) throws InterruptedException, ExecutionException {
		Usuario previo = (Usuario) crudService.read(user.getRut().split(":")[0], Usuario.class);
		if(user.getPassword().equals("")) user.setPassword(previo.getPassword());
		else user.setPassword(pass.encode(user.getPassword()));
		user.setSede(crudService.getDocRef("sedes", user.getRut().split(":")[1]));
		user.setRut(user.getRut().split(":")[0]);
		user.setSalaActual(previo.getSalaActual());
		if(previo.getSalaActual()!= null && previo.getSede() != user.getSede()) {
			Sala salaPrevia = previo.getSalaActual().get().get().toObject(Sala.class);
			salaPrevia.getProfesores().remove(crudService.getDocRef("usuarios", previo.getRut()));
			Registro regObj = new Registro();
			regObj.setId(crudService.newDoc("registros").getId());
			regObj.setSala(crudService.getDocRef("salas", salaPrevia.getId()));
			regObj.setSede(salaPrevia.getSede());
			regObj.setUsuario(crudService.getDocRef("usuarios", previo.getRut()));
			regObj.setTipo("Salida");
			regObj.setFecha(new Date());
			crudService.update(regObj, regObj.getId());
			crudService.update(salaPrevia, salaPrevia.getId());
		}
		String respuesta= crudService.update(user,user.getRut());
		return respuesta;
	}
	@PostMapping("/deleteUser")
	public String delete(@RequestParam(name="id") String id) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Usuario userObj = (Usuario) this.crudService.read(id, Usuario.class);
		if(userObj.getSalaActual() != null) {
			Sala salaPrevia = (Sala) crudService.read(userObj.getSalaActual().getId(), Sala.class);
			salaPrevia.getProfesores().remove(crudService.getDocRef("usuarios", userObj.getRut()));
			Registro regObj = new Registro();
			regObj.setId(crudService.newDoc("registros").getId());
			regObj.setSala(crudService.getDocRef("salas", salaPrevia.getId()));
			regObj.setSede(salaPrevia.getSede());
			regObj.setUsuario(crudService.getDocRef("usuarios", id));
			regObj.setTipo("Salida");
			regObj.setFecha(new Date());
			crudService.update(regObj, regObj.getId());
			crudService.update(salaPrevia, salaPrevia.getId());
		}
		return crudService.delete(id,Usuario.class);
	}

	@PostMapping("/extrasUser")
	public ModelAndView extra(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Usuario user = (Usuario) crudService.read(id, Usuario.class);
        model.addAttribute("Usuario", user);
        return new ModelAndView("fragments/usuarios/extrasUsuario");
	}
}
