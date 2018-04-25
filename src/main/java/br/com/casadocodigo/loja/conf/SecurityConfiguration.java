package br.com.casadocodigo.loja.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.casadocodigo.loja.daos.UsuarioDAO;

// Classe onde estão definidas as configurações de segurança da aplicação
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private UsuarioDAO usuarioDAO;
	
	// metodo que por padrao bloqueia todas as requisições não autenticadas
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/produtos/form").hasRole("ADMIN") // por padrao a role é buscada ROLE_ + a regra definida ROLE_ADMIN
			.antMatchers("/url-magica-943943fh2tb4ct234bt246b2c4923t4b9").permitAll()
			.antMatchers(HttpMethod.POST, "/produtos").hasRole("ADMIN")
			.antMatchers(HttpMethod.GET, "/produtos").permitAll()
			.antMatchers("/produtos/**").permitAll()
			.antMatchers("/resources/**").permitAll() // desbloqueia os recursos que a aplicação utiliza
			.antMatchers("/").permitAll()
			.anyRequest().authenticated()
			.and().formLogin()
			.loginPage("/login").permitAll() // informar o Spring para utilizar a pagina de login customizada
			.and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")); // pagina default do Spring para logout 
	}
	
	// metodo que o Spring usa para fazer as configurações dos usuarios existentes
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(usuarioDAO) // incluo o dao que vai carregar o usuario
		.passwordEncoder(new BCryptPasswordEncoder()); // determino o codificador para as senhas dos usuarios com a criptografia BCrypt
	}
}
