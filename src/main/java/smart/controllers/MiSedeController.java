package smart.controllers;

import java.time.LocalDateTime;
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
@RequestMapping("/miSede")
public class MiSedeController {
	
	private CRUDServices crudService;
	private Usuario user;
	
	public MiSedeController() {
	}
	
	@GetMapping("/show")
	public ModelAndView verSedes(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        LinkedList<Sala> salas = new LinkedList<>();
        LinkedList<Sede> sedes = new LinkedList<>();
        Sede miSede = user.getSede().get().get().toObject(Sede.class);
        if(user.getSede()!= null && (!user.getRoles().contains("ADMIN") || !user.getRoles().contains("PERSONALDTI"))) {
            for (DocumentReference sala : miSede.getSalas()) {
    			salas.add(sala.get().get().toObject(Sala.class));
    		}
        }
        if(user.getRoles().contains("ADMIN") || user.getRoles().contains("PERSONALDTI")) {
        	for (Object sedePrev : crudService.getAllDocs(Sede.class)) {
        		sedes.add((Sede) sedePrev);
    		}
        }
        DocumentReference userDoc = crudService.getDocRef("usuarios", user.getRut());
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "miSede:ver");
        model.addAttribute("sede",miSede);
        model.addAttribute("allSalas",salas);
        model.addAttribute("allSedes",sedes);
        model.addAttribute("userDoc",userDoc);
        model.addAttribute("claseUser",Usuario.class);
        return new ModelAndView("home");
	}
	
	@PostMapping("/changeSede")
	public ModelAndView changeSede(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		Sede sede = (Sede) crudService.read(id, Sede.class);
    	LinkedList<Sala> salas = new LinkedList<>();
    	if(sede.getSalas()!=null) {
        	for (DocumentReference sala : sede.getSalas()) {
    			salas.add(sala.get().get().toObject(Sala.class));
    		}
    	}
        DocumentReference userDoc = crudService.getDocRef("usuarios", user.getRut());
        model.addAttribute("usuario", user);
        model.addAttribute("sede",sede);
        model.addAttribute("allSalas",salas);
        model.addAttribute("userDoc",userDoc);
        model.addAttribute("claseUser",Usuario.class);
        return new ModelAndView("fragments/miSede/selectMiSede");
	}
	
	@PostMapping("/ocupar")
	public String ocupar(@RequestParam(name="salaId") String sala, @RequestParam(name="userId") String user) throws InterruptedException, ExecutionException {
		Usuario userObj = (Usuario) crudService.read(user, Usuario.class);
		Sala salaObj = (Sala) crudService.read(sala, Sala.class);
		if(userObj.getSalaActual()!=null) {
			Sala salaPrevia = (Sala) crudService.read(userObj.getSalaActual().getId(), Sala.class);
			salaPrevia.getProfesores().remove(crudService.getDocRef("usuarios", userObj.getRut()));
			salaPrevia.setEstado("Libre");
			Registro regObjPrev = new Registro();
			regObjPrev.setId(crudService.newDoc("registros").getId());
			regObjPrev.setSala(crudService.getDocRef("salas", salaPrevia.getId()));
			regObjPrev.setSede(salaObj.getSede());
			regObjPrev.setUsuario(crudService.getDocRef("usuarios", user));
			regObjPrev.setTipo("Salida");
			regObjPrev.setFecha(new Date());
			crudService.update(regObjPrev, regObjPrev.getId());
			crudService.update(salaPrevia, salaPrevia.getId());
		}
		userObj.setSalaActual(crudService.getDocRef("salas", sala));
		salaObj.getProfesores().add(crudService.getDocRef("usuarios", user));
		salaObj.setEstado("Ocupada");
		Registro regObj = new Registro();
		regObj.setId(crudService.newDoc("registros").getId());
		regObj.setSala(crudService.getDocRef("salas", sala));
		regObj.setSede(salaObj.getSede());
		regObj.setUsuario(crudService.getDocRef("usuarios", user));
		regObj.setTipo("Ingreso");
		regObj.setFecha(new Date());
		crudService.update(regObj, regObj.getId());
		crudService.update(salaObj, sala);
		crudService.update(userObj, user);
		return "Ok";
	}
	
	@PostMapping("/liberar")
	public Object liberar(@RequestParam(name="userId") String user) throws InterruptedException, ExecutionException {
		Usuario userObj = (Usuario) crudService.read(user, Usuario.class);
		Sala salaPrevia = (Sala) crudService.read(userObj.getSalaActual().getId(), Sala.class);
		salaPrevia.getProfesores().remove(crudService.getDocRef("usuarios", userObj.getRut()));
		salaPrevia.setEstado("Libre");
		userObj.setSalaActual(null);
		Registro regObj = new Registro();
		regObj.setId(crudService.newDoc("registros").getId());
		regObj.setSala(crudService.getDocRef("salas", salaPrevia.getId()));
		regObj.setSede(salaPrevia.getSede());
		regObj.setUsuario(crudService.getDocRef("usuarios", user));
		regObj.setTipo("Salida");
		regObj.setFecha(new Date());
		crudService.update(regObj, regObj.getId());
		crudService.update(salaPrevia, salaPrevia.getId());
		crudService.update(userObj, user);
		return "Ok";
	}
}
