package smart.controllers;

import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.google.cloud.firestore.DocumentReference;
import com.google.firebase.database.utilities.Pair;
import com.lowagie.text.DocumentException;

import smart.servicios.CRUDServices;
import smart.servicios.GeneradorPDF;
import smart.models.Clase;
import smart.models.Registro;
import smart.models.Sala;
import smart.models.Sede;
import smart.models.Usuario;
import smart.security.UserPrincipal;

@RestController
@RequestMapping("/reportes")
public class ReportesController {
	@Autowired
	private ApplicationContext applicationContext;
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
	
	@PostMapping("/reporte")
	public ModelAndView reportePorSede(
			@RequestParam(name="sede") String sede,
			@RequestParam(name="user") String userId,
			@RequestParam(name="desde") String desde,
			@RequestParam(name="hasta") String hasta,
			@RequestParam(name="tipo")String tipo,Model model) throws InterruptedException, ExecutionException{
		this.crudService = new CRUDServices();
		DocumentReference sedeDf = crudService.getDocRef("sedes",sede);
		Sede sedeObj = sedeDf.get().get().toObject(Sede.class);
		LinkedList<Pair<Usuario,LinkedList<Registro>>> registros = new LinkedList<>();
		if(tipo.equals("Sede")) {
			LinkedList<DocumentReference> usuarios = crudService.getUserDocRef();
			for(DocumentReference user:usuarios) {
				LinkedList<Registro> registrosUser = crudService.getRegistrosWhere(sedeDf, user, desde, hasta);
				Pair<Usuario,LinkedList<Registro>> par = new Pair<>(user.get().get().toObject(Usuario.class),registrosUser);
				registros.add(par);
			}
		}else {
			DocumentReference usuarioDF = crudService.getDocRef("usuarios", userId);
			LinkedList<Registro> registrosUser = crudService.getRegistrosWhere(sedeDf, usuarioDF, desde, hasta);
			Pair<Usuario,LinkedList<Registro>> par = new Pair<>(usuarioDF.get().get().toObject(Usuario.class),registrosUser);
			registros.add(par);
		}
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		model.addAttribute("tipo", tipo);
		model.addAttribute("usuario", userId);
		model.addAttribute("formato", formatter);
		model.addAttribute("sede", sedeObj);
		model.addAttribute("desde", desde);
		model.addAttribute("hasta", hasta);
		model.addAttribute("claseSala", Clase.class);
		model.addAttribute("registros", registros);
        return new ModelAndView("pdf/reporte");
	}
	
	@PostMapping("/pdf")
	public ResponseEntity generarPDF(@RequestParam(name="data") String data) throws DocumentException {
		ByteArrayOutputStream byteArrayOutputStreamPDF = new GeneradorPDF().crearPDF(data);
		ByteArrayResource inputStreamResourcePDF = new ByteArrayResource(byteArrayOutputStreamPDF.toByteArray());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + "reporte").contentType(MediaType.APPLICATION_PDF)
				.contentLength(inputStreamResourcePDF.contentLength()).body(inputStreamResourcePDF);

	}
}
