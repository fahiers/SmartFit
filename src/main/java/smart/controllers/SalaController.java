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
import smart.models.Equipo;
import smart.models.Sala;
import smart.models.Sede;
import smart.models.Usuario;
import smart.security.UserPrincipal;

@RestController
@RequestMapping("/salas")
public class SalaController {

	private CRUDServices crudService;
	private Usuario user;
	public SalaController() {
	}
	
	@GetMapping("/crearSala")
	public ModelAndView usuario(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "salas:crear");
        Sala newSala = new Sala();
        model.addAttribute("newSala", newSala);
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);
        return new ModelAndView("home");
	}
	
	@GetMapping("/verSalas")
	public ModelAndView verUsuarios(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "salas:ver");
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);
        model.addAttribute("claseSede",Sede.class);
        return new ModelAndView("home");
	}
	@PostMapping("/obtenerSalas")
		public ModelAndView getSalas(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException{
			this.crudService = new CRUDServices();
			Sede sede = (Sede) crudService.read(id, Sede.class);
        	LinkedList<Sala> salas = new LinkedList<>();
        	if(sede.getSalas()!=null) {
            	for (DocumentReference sala : sede.getSalas()) {
        			salas.add(sala.get().get().toObject(Sala.class));
        		}
        	}
            model.addAttribute("allSalas",salas);
            model.addAttribute("sede",sede);
            return new ModelAndView("fragments/salas/verSalasSelect");
		}
	@PostMapping("/addSala")
	public String create(@ModelAttribute Sala sala) throws InterruptedException, ExecutionException {
		sala.setSede(crudService.getDocRef("sedes", sala.getId()));
		DocumentReference newDoc= crudService.newDoc("salas");
		sala.setId(newDoc.getId());
		sala.setProfesores(new LinkedList<>());
		Sede sedeObj = sala.getSede().get().get().toObject(Sede.class);
		if(sedeObj.getSalas() == null) {
			sedeObj.setSalas(new LinkedList<DocumentReference>());
		}
		sedeObj.getSalas().add(newDoc);
		crudService.update(sedeObj, sedeObj.getId());
		return crudService.create(sala,sala.getId());
	}
	
	@PostMapping("/readSala")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.read(id,Sala.class);
	}
	@PostMapping("/deleteSala")
	public String delete(@RequestParam(name="id") String id) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Sala sala = (Sala) crudService.read(id, Sala.class);
		Sede sede = (Sede) crudService.read(sala.getSede().getId(), Sede.class);
		sede.getSalas().remove(crudService.getDocRef("salas", id));
		crudService.update(sede,sede.getId());
		LinkedList<Equipo> equipos = crudService.getEquiposWhere(crudService.getDocRef("salas", id));
		for(Equipo eq : equipos) {
			eq.setSala(null);
			crudService.update(eq,eq.getId());
		}
		return crudService.delete(id,Sala.class);
	}
	@PostMapping("/editSala")
	public ModelAndView edit(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Sala sala = (Sala) crudService.read(id, Sala.class);
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("sedeId",sala.getSede().getId());
        model.addAttribute("allSedes",sedes);
        model.addAttribute("Sala", sala);
        return new ModelAndView("fragments/salas/editarSala");
	}
	@PostMapping("/updateSala")
	public String update(@ModelAttribute Sala sala) throws InterruptedException, ExecutionException {
		Sala previa = (Sala) crudService.read(sala.getId().split(":")[0], Sala.class);
		sala.setSede(crudService.getDocRef("sedes", sala.getId().split(":")[1]));
		sala.setId(sala.getId().split(":")[0]);
		if(previa.getSede() != sala.getSede()) {
			Sede sedePrevia = previa.getSede().get().get().toObject(Sede.class);
			Sede sedeNueva = sala.getSede().get().get().toObject(Sede.class);
			DocumentReference prev = crudService.getDocRef("salas", previa.getId());
			DocumentReference nueva = crudService.getDocRef("salas", sala.getId());
			sedePrevia.getSalas().remove(prev);
			sedeNueva.getSalas().add(nueva);
			crudService.update(sedePrevia,sedePrevia.getId());
			crudService.update(sedeNueva,sedeNueva.getId());
			
		}
		sala.setProfesores(previa.getProfesores());
 		String respuesta= crudService.update(sala,sala.getId());
		return respuesta;
	}
	@PostMapping("/extrasSala")
	public ModelAndView extra(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Sala sala = (Sala) crudService.read(id, Sala.class);
        LinkedList<Equipo> equipos = crudService.getEquiposWhere(crudService.getDocRef("salas", id));
        model.addAttribute("allEquipos", equipos);
        return new ModelAndView("fragments/salas/extrasSala");
	}
}
