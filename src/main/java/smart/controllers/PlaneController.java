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
import smart.models.Plane;
import smart.models.Sala;
import smart.models.Sede;
import smart.models.Usuario;
import smart.security.UserPrincipal;

@RestController
@RequestMapping("/planes")
public class PlaneController {
	
	private CRUDServices crudService;
	private Usuario user;
	
	public PlaneController() {
	}
	
	@GetMapping("/crearPlan")
	public ModelAndView crearPlan(Model model){
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "planes:crear");
        Plane newPLan = new Plane();
        model.addAttribute("newPlan", newPLan);
        model.addAttribute("caractList", new LinkedList<>());
        return new ModelAndView("home");
	}
	
	@GetMapping("/verPlanes")
	public ModelAndView verPlanes(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "planes:ver");
        LinkedList<Object> objetos = crudService.getAllDocs(Plane.class);
        LinkedList<Plane> planes = new LinkedList<>();
        for (Object plan : objetos) {
			planes.add((Plane) plan);
		}
        model.addAttribute("allPlanes",planes);
        return new ModelAndView("home");
	}
	
	@PostMapping("/addCaract")
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
        return new ModelAndView("fragments/planes/caractAdded");
	}
	@PostMapping("/getDefault")
	public ModelAndView getDefault() throws InterruptedException, ExecutionException{
        return new ModelAndView("fragments/planes/default");
	}
	@PostMapping("/addPlan")
	public String create(@ModelAttribute Plane plan) throws InterruptedException, ExecutionException {
		plan.setId(crudService.newDoc("planes").getId());
		return crudService.create(plan,plan.getId());
	}
	
	@PostMapping("/readPlan")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return crudService.read(id,Plane.class);
	}
	@PostMapping("/deletePlan")
	public String delete(@RequestParam(name="id") String id) throws InterruptedException, ExecutionException {
		return crudService.delete(id,Plane.class);
		
	}

	@PostMapping("/editPlan")
	public ModelAndView edit(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Plane planes = (Plane) crudService.read(id, Plane.class);
        model.addAttribute("Plan", planes);
        String caract = "";
        for(String car: planes.getCaracteristicas()) {
        	caract+=car +",";
        }
        caract = caract.substring(0, caract.length()-2);
        System.out.println(caract);
        model.addAttribute("caractSaved", caract);
        return new ModelAndView("fragments/planes/editarPlan");
	}
	@PostMapping("/updatePlan")
	public String update(@ModelAttribute Plane plan) throws InterruptedException, ExecutionException {
 		String respuesta= crudService.update(plan,plan.getId());
		return respuesta;
	}
	@PostMapping("/extrasPlan")
	public ModelAndView extra(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException {
		this.crudService = new CRUDServices();
		Plane plan = (Plane) crudService.read(id, Plane.class);
        model.addAttribute("caractList", plan.getCaracteristicas());
        return new ModelAndView("fragments/planes/caractAdded");
	}
}
