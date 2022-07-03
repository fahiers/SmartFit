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
        newSede.setSalas(new LinkedList<DocumentReference>());
        model.addAttribute("newSede", newSede);
        model.addAttribute("regiones", this.getRegiones());
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
	@PostMapping("/deleteSede")
	public String delete(@RequestParam(name="id") String id) throws InterruptedException, ExecutionException {
		Sede sede = (Sede) crudService.read(id,Sede.class);
        int contador = 0;
        for (DocumentReference sala : sede.getSalas()) {
			crudService.delete(sala.get().get().toObject(Sala.class).getId(),Sala.class);
			contador+=1;
		}
        String respuesta = crudService.delete(id,Sede.class) + "/" + contador;
		return respuesta;
	}

	@PostMapping("/editSede")
	public ModelAndView edit(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Sede sede = (Sede) crudService.read(id, Sede.class);
        model.addAttribute("Sede", sede);
        LinkedList<String> regiones = this.getRegiones();
        model.addAttribute("regActual", this.getRegiones().indexOf(sede.getRegion()));
        model.addAttribute("regiones", regiones);
        return new ModelAndView("fragments/sedes/editarSede");
	}
	@PostMapping("/updateSede")
	public String update(@ModelAttribute Sede sede) throws InterruptedException, ExecutionException {
		sede.setSalas(((Sede) crudService.read(sede.getId(), Sede.class)).getSalas());
		String respuesta= crudService.update(sede,sede.getId());
		return respuesta;
	}
	@PostMapping("/extrasSede")
	public ModelAndView extra(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Sede sede = (Sede) crudService.read(id, Sede.class);
        LinkedList<Sala> salas = new LinkedList<>();
        if(sede.getSalas() != null) {
            for (DocumentReference sala : sede.getSalas()) {
    			salas.add(sala.get().get().toObject(Sala.class));
    		}
        }
        model.addAttribute("allSalas", salas);
        return new ModelAndView("fragments/sedes/extrasSede");
	}
	public LinkedList<String> getRegiones() {
		LinkedList<String> regiones = new LinkedList<>();
		regiones.add("I - Región de Tarapacá");
		regiones.add("II - Región de Antofagasta");
		regiones.add("III - Región de Atacama");
		regiones.add("IV - Región de Coquimbo");
		regiones.add("V - Región de Valparaíso");
		regiones.add("VI - Región del Libertador General Bernardo O’Higgins");
		regiones.add("VII - Región del Maule");
		regiones.add("VIII - Región del Biobío");
		regiones.add("IX - Región de La Araucanía");
		regiones.add("X - Región de Los Lagos");
		regiones.add("XI - Región Aysén del General Carlos Ibáñez del Campo");
		regiones.add("XII - Región de Magallanes y Antártica Chilena");
		regiones.add("RM - Región Metropolitana de Santiago");
		regiones.add("XIV - Región de Los Ríos");
		regiones.add("XV - Región de Arica y Parinacota");
		regiones.add("XVI - Región de Ñuble");
		return regiones;
	}
}
