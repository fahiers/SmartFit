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
        for (DocumentReference sala : user.getSede().get().get().toObject(Sede.class).getSalas()) {
			salas.add(sala.get().get().toObject(Sala.class));
		}
        Sede sedeActual = (Sede) crudService.readFromRef(user.getSede(), Sede.class);
        DocumentReference userDoc = crudService.getDocRef("usuarios", user.getRut());
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "miSede:ver");
        model.addAttribute("sedeActual",sedeActual);
        model.addAttribute("allSalas",salas);
        model.addAttribute("userDoc",userDoc);
        model.addAttribute("claseUser",Usuario.class);
        System.out.println("cargo");
        return new ModelAndView("home");
	}
	
	@GetMapping("/ocupar")
	public String ocupar(@RequestParam(name="salaId") String sala, @RequestParam(name="userId") String user) throws InterruptedException, ExecutionException {
		Usuario userObj = (Usuario) crudService.read(user, Usuario.class);
		Sala salaObj = (Sala) crudService.read(sala, Sala.class);
		if(userObj.getSalaActual()!=null) {
			Sala salaPrevia = (Sala) crudService.read(userObj.getSalaActual().getId(), Sala.class);
			salaPrevia.getProfesores().remove(crudService.getDocRef("usuarios", userObj.getRut()));
			salaPrevia.setEstado("Libre");
			crudService.update(salaPrevia, salaPrevia.getId());
		}
		userObj.setSalaActual(crudService.getDocRef("salas", sala));
		salaObj.getProfesores().add(crudService.getDocRef("usuarios", user));
		salaObj.setEstado("Ocupada");
		crudService.update(salaObj, sala);
		crudService.update(userObj, user);
		return "Ok";
	}
	
	@GetMapping("/liberar")
	public Object liberar(@RequestParam(name="userId") String user) throws InterruptedException, ExecutionException {
		Usuario userObj = (Usuario) crudService.read(user, Usuario.class);
		Sala salaPrevia = (Sala) crudService.read(userObj.getSalaActual().getId(), Sala.class);
		salaPrevia.getProfesores().remove(crudService.getDocRef("usuarios", userObj.getRut()));
		salaPrevia.setEstado("Libre");
		userObj.setSalaActual(null);
		crudService.update(salaPrevia, salaPrevia.getId());
		crudService.update(userObj, user);
		return "Ok";
	}
}
