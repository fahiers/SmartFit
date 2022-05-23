package smart.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")

public class HomeControl {
    @RequestMapping(method = RequestMethod.GET)
    public String printWelcome(Model modelo) {
    	
    	System.out.println("llegoAlControlador");
    	modelo.addAttribute("message", "Hello Spring MVC 5!");
        return "index";
    }
 
}