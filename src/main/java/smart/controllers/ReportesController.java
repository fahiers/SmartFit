package smart.controllers;

import java.text.SimpleDateFormat;
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
@RequestMapping("/reportes")
public class ReportesController {
	
	private CRUDServices crudService;
	private Usuario user;
	
	public ReportesController() {
	}
	
	@GetMapping("/show")
	public ModelAndView verSedes(Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		this.user=((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
		LinkedList<Object> objSede = crudService.getAllDocs(Sede.class);
        LinkedList<Sede> sedes = new LinkedList<>();
        for (Object sede : objSede) {
			sedes.add((Sede) sede);
		}
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date fecha = new Date();
        String hoy = formatter.format(fecha);
        fecha.setYear(fecha.getYear()-2);
        String last2years = formatter.format(fecha);
        model.addAttribute("hasta",hoy);
        model.addAttribute("desde",last2years);
        model.addAttribute("allSedes",sedes);
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "reportes:ver");
        return new ModelAndView("home");
	}
	@PostMapping("/cargarUsers")
	public ModelAndView changeSede(@RequestParam(name="id") String id, Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		DocumentReference sedeDf = crudService.getDocRef("sedes", id);
		model.addAttribute("usuarios", crudService.getAllUsersSede(sedeDf));
        return new ModelAndView("fragments/reportes/selectUsers");
	}
	@PostMapping("/reportePorSede")
	public ModelAndView reportePorSede(){
        return new ModelAndView("fragments/reportes/reportesxSede");
	}
	@PostMapping("/reportePorUser")
	public ModelAndView reportePorUser(){
        return new ModelAndView("fragments/reportes/reportesxUser");
	}
}
