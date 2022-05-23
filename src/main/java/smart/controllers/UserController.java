package smart.controllers;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smart.servicios.UsuariosService;
import smart.models.Usuario;

@RestController
@RequestMapping("/usuario")
public class UserController {
	
	@Autowired
	UsuariosService usersService;

	@PostMapping("/addUser")
	public String create(@RequestBody Usuario user) throws InterruptedException, ExecutionException {
		return usersService.create(user);
	}
	
	@PostMapping("/readUser")
	public Object read(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return usersService.read(id,Usuario.class);
	}
	@PostMapping("/updateUser")
	public String update(@RequestBody Usuario user) throws InterruptedException, ExecutionException {
		return usersService.update(user);
	}
	@PostMapping("/deleteUser")
	public String delete(@RequestHeader() String id) throws InterruptedException, ExecutionException {
		return usersService.delete(id);
	}
}
