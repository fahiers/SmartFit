package smart.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import smart.models.Usuario;
import smart.security.UserPrincipal;
import smart.servicios.CRUDServices;
import smart.servicios.FireBaseIniciador;

@Controller
@RequestMapping("/")

public class MainController {
	
	@Autowired
	PasswordEncoder pass;
	
	@RequestMapping(value="/", method = RequestMethod.GET)
    public ModelAndView welcome() throws InterruptedException, ExecutionException {
		return new ModelAndView("index");
    }
	@RequestMapping(value="/login", method = RequestMethod.GET)
    public ModelAndView login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return new ModelAndView("login");
    }
	
	@RequestMapping(value="/home", method = RequestMethod.GET)
    public ModelAndView home(Model model) throws InterruptedException, ExecutionException {
		Usuario user = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        model.addAttribute("usuario", user);
        model.addAttribute("pagina", "home");
        Usuario newUser = new Usuario();
        model.addAttribute("newUser", newUser);
        return new ModelAndView("home");
    }
}