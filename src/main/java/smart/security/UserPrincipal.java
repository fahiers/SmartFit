package smart.security;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import smart.models.Usuario;

public class UserPrincipal implements UserDetails{

	private Usuario user;
	
	public UserPrincipal(Usuario user) {
		this.user = user;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		LinkedList<GrantedAuthority> autorizaciones = new LinkedList<>();
		this.user.getPermisos().forEach(item ->{
			autorizaciones.add(new SimpleGrantedAuthority(item));
		});
		this.user.getRoles().forEach(item ->{
			autorizaciones.add(new SimpleGrantedAuthority("ROLE_"+item));
		});
		return autorizaciones;
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getRut()+":"+user.getNombre()+":"+user.getApellido();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		if (this.user.getErrorLogin()>=3) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.user.isActivo();
	}
	
	public Usuario getUser() {
		return this.user;
	}
}
