package smart.controllers;

import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

import org.springframework.security.core.context.SecurityContextHolder;
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

import lombok.ToString;
import smart.models.Clase;
import smart.models.Equipo;
import smart.models.Sala;
import smart.models.Sede;
import smart.models.Usuario;
import smart.security.UserPrincipal;
import smart.servicios.CRUDServices;

@RestController
@RequestMapping("/equipos")
public class EquipoController {
	
	private CRUDServices crudService;
	private Usuario user;
	
	@GetMapping("/crearEquipo")
	public ModelAndView crearPlan(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "equipos:crear");
        Equipo newEquipo = new Equipo();
        model.addAttribute("newEquipo", newEquipo);
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);
        return new ModelAndView("home");
	}
	
	@GetMapping("/verEquipos")
	public ModelAndView verPlanes(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "equipos:ver");
        LinkedList<Object> objetos = crudService.getAllDocs(Equipo.class);
        LinkedList<Equipo> equipos = new LinkedList<>();
        LinkedList<String> salas = new LinkedList<>();
        for (Object equipo : objetos) {
			equipos.add((Equipo) equipo);
			if(((Equipo) equipo).getSala() == null) {
				salas.add("Sin Sala");
			} else {
				Sala salaAux = (Sala)((Equipo) equipo).getSala().get().get().toObject(Sala.class);
				salas.add(salaAux.getNombre());
			}
		}
        model.addAttribute("allEquipos",equipos);
        model.addAttribute("allSalas",salas);
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
        model.addAttribute("allSalas",salas);
        return new ModelAndView("fragments/clases/selectSala");
	}
	
	@PostMapping("/getDefaultSala")
	public ModelAndView getDefaultSala() throws InterruptedException, ExecutionException{
        return new ModelAndView("fragments/clases/selectSalaDefault");
	}
	
	@PostMapping("/addEquipo")
	public String create(@ModelAttribute Equipo equipo) throws InterruptedException, ExecutionException {
		equipo.setSala(crudService.getDocRef("salas", equipo.getId()));
		equipo.setId(crudService.newDoc("equipos").getId());
		return crudService.create(equipo,equipo.getId());
	}
	
	@PostMapping("/readEquipo")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.read(id,Equipo.class);
	}
	@PostMapping("/deleteEquipo")
	public String delete(@RequestParam(name="id") String id) throws InterruptedException, ExecutionException {
		return crudService.delete(id,Equipo.class);
	}

	@PostMapping("/editEquipo")
	public ModelAndView edit(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Equipo equipo = (Equipo) crudService.read(id, Equipo.class);
    	LinkedList<Sala> salas = new LinkedList<>();
		if(equipo.getSala() == null) {
	        model.addAttribute("sedeOriVal", "");
	        model.addAttribute("salaOriVal", "");
		}else {
			Sala salaOri = (Sala) crudService.read(equipo.getSala().getId(), Sala.class);
			Sede sedeOri = (Sede) crudService.read(salaOri.getSede().getId(), Sede.class);

	        model.addAttribute("sedeOriVal", sedeOri.getId());
	        model.addAttribute("salaOriVal", salaOri.getId());
	    	if(sedeOri.getSalas()!=null) {
	        	for (DocumentReference sala : sedeOri.getSalas()) {
	    			salas.add(sala.get().get().toObject(Sala.class));
	    		}
	    	}
		}
		model.addAttribute("Equipo", equipo);
        model.addAttribute("allSalasOri",salas);
        
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);

        return new ModelAndView("fragments/equipos/editarEquipo");
	}
	@PostMapping("/updateEquipo")
	public String update(@ModelAttribute Equipo equipo) throws InterruptedException, ExecutionException {
		System.out.println(equipo);
		equipo.setSala(crudService.getDocRef("salas", equipo.getId().split(":")[1]));
		equipo.setId(equipo.getId().split(":")[0]);
 		String respuesta= crudService.update(equipo,equipo.getId());
		return respuesta;
	}
}
