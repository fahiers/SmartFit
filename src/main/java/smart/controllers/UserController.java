package smart.controllers;

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
	public String create(@ModelAttribute Usuario user, @RequestParam(name="sedeString") String sede) throws InterruptedException, ExecutionException {
		user.setPassword(pass.encode(user.getPassword()));
		user.setSede(crudService.getDocRef("sedes", sede));
		return crudService.create(user,user.getRut());
	}
	
	@PostMapping("/readUser")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.read(id,Usuario.class);
	}
	@PostMapping("/updateUser")
	public String update(@RequestBody Usuario user) throws InterruptedException, ExecutionException {
		return crudService.update(user,user.getRut());
	}
	@PostMapping("/deleteUser")
	public String delete(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.delete(id,Usuario.class);
	}
}
