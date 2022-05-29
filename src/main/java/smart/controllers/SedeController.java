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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import smart.servicios.CRUDServices;
import smart.models.Sede;
import smart.models.Usuario;
import smart.security.UserPrincipal;

@RestController
@RequestMapping("/sedes")
public class SedeController {
	
	private CRUDServices crudService;
	private Usuario user;
	
	public SedeController() {
	}
	
	@GetMapping("/crearSede")
	public ModelAndView crearSede(Model model){
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "sedes:crear");
        Sede newSede = new Sede();
        model.addAttribute("newSede", newSede);
        return new ModelAndView("home");
	}
	
	@GetMapping("/verSedes")
	public ModelAndView verSedes(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "sedes:ver");
        LinkedList<Object> objetos = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objetos) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);
        return new ModelAndView("home");
	}
	
	@PostMapping("/addSede")
	public String create(@ModelAttribute Sede sede) throws InterruptedException, ExecutionException {
		sede.setId(crudService.newDoc("sedes").getId());
		return crudService.create(sede,sede.getId());
	}
	
	@PostMapping("/readSede")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.read(id,Sede.class);
	}
	@PostMapping("/updateSede")
	public String update(@RequestBody Sede sede) throws InterruptedException, ExecutionException {
		return crudService.update(sede,sede.getId());
	}
	@PostMapping("/deleteSede")
	public String delete(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.delete(id,Sede.class);
	}
}
