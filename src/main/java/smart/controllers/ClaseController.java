package smart.controllers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
import smart.models.Clase;
import smart.models.Plane;
import smart.models.Sala;
import smart.models.Sede;
import smart.models.Usuario;
import smart.security.UserPrincipal;

@RestController
@RequestMapping("/clases")
public class ClaseController {
	
	private CRUDServices crudService;
	private Usuario user;
	
	public ClaseController() {
	}
	
	@GetMapping("/crearClase")
	public ModelAndView crearPlan(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "clases:crear");
        Clase newClase = new Clase();
        model.addAttribute("newClase", newClase);
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);
        return new ModelAndView("home");
	}
	
	@GetMapping("/verClases")
	public ModelAndView verPlanes(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "clases:ver");
        LinkedList<Object> objetos = crudService.getAllDocs(Clase.class);
        LinkedList<Clase> clases = new LinkedList<>();
        for (Object clase : objetos) {
			clases.add((Clase) clase);
		}
        model.addAttribute("allClases",clases);
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
	@PostMapping("/addHorario")
	public ModelAndView changeSede(@RequestParam(name="newC") String newC, @RequestParam(name="caract") String caract ,Model model) throws InterruptedException, ExecutionException{
		if(caract == "") {
			caract+=newC;
		} else {
			caract+=(","+newC);
		}
		LinkedList<String> caractList = new LinkedList<>();
		for (String car : caract.split(",")) {
			caractList.add(car);
		}
		System.out.println(caract);
        model.addAttribute("caractList",caractList);
        model.addAttribute("caractString",caract);
        return new ModelAndView("fragments/clases/caractAdded");
	}
	@PostMapping("/getDefault")
	public ModelAndView getDefault() throws InterruptedException, ExecutionException{
        return new ModelAndView("fragments/clases/default");
	}
	@PostMapping("/getDefaultSala")
	public ModelAndView getDefaultSala() throws InterruptedException, ExecutionException{
        return new ModelAndView("fragments/clases/selectSalaDefault");
	}
	
	@PostMapping("/addClase")
	public String create(@ModelAttribute Clase clase) throws InterruptedException, ExecutionException {
		clase.setSala(crudService.getDocRef("salas", clase.getId()));
		clase.setId(crudService.newDoc("clases").getId());
		return crudService.create(clase,clase.getId());
	}
	
	@PostMapping("/readClase")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.read(id,Clase.class);
	}
	@PostMapping("/deleteClase")
	public String delete(@RequestParam(name="id") String id) throws InterruptedException, ExecutionException {
		return crudService.delete(id,Clase.class);
	}
	@PostMapping("/editClase")
	public ModelAndView edit(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Clase clase = (Clase) crudService.read(id, Clase.class);
		Sala salaOri = (Sala) crudService.read(clase.getSala().getId(), Sala.class);
		Sede sedeOri = (Sede) crudService.read(salaOri.getSede().getId(), Sede.class);
        model.addAttribute("Clase", clase);
        
        String horarios = "";
        for(String hor: clase.getHorarios()) {
        	horarios+=hor +",";
        }
        if(horarios != "") {
            horarios = horarios.substring(0, horarios.length()-1);
        }
        LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        model.addAttribute("allSedes",sedes);

    	LinkedList<Sala> salas = new LinkedList<>();
    	if(sedeOri.getSalas()!=null) {
        	for (DocumentReference sala : sedeOri.getSalas()) {
    			salas.add(sala.get().get().toObject(Sala.class));
    		}
    	}
        model.addAttribute("allSalasOri",salas);
        model.addAttribute("caractSaved", horarios);
        model.addAttribute("sedeOriVal", sedeOri.getId());
        model.addAttribute("salaOriVal", salaOri.getId());
        return new ModelAndView("fragments/clases/editarClase");
	}
	@PostMapping("/updateClase")
	public String update(@ModelAttribute Clase clase) throws InterruptedException, ExecutionException {
		clase.setSala(crudService.getDocRef("salas", clase.getId().split(":")[1]));
		clase.setId(clase.getId().split(":")[0]);
 		String respuesta= crudService.update(clase,clase.getId());
		return respuesta;
	}
	@PostMapping("/extrasClase")
	public ModelAndView extra(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Clase clase = (Clase) crudService.read(id, Clase.class);
        model.addAttribute("horariosList", clase.getHorarios());
        return new ModelAndView("fragments/clases/extrasClase");
	}
}
