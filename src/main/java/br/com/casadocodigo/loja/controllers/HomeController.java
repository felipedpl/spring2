package br.com.casadocodigo.loja.controllers;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import br.com.casadocodigo.loja.daos.ProdutoDAO;
import br.com.casadocodigo.loja.daos.UsuarioDAO;
import br.com.casadocodigo.loja.models.Role;
import br.com.casadocodigo.loja.models.Usuario;

@Controller
public class HomeController {
	
	@Autowired
	private ProdutoDAO produtoDAO;
	
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	@RequestMapping("/") // determina que o metodo atende ao endereço determinado
	@Cacheable(value="produtosHome") // informo ao Spring que quero manter em cache o objeto enviado por este método
	public ModelAndView index () {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("produtos", produtoDAO.listar());
		return modelAndView;
	}
	
	@RequestMapping("/url-magica-943943fh2tb4ct234bt246b2c4923t4b9")
	@ResponseBody // identifica o retorno como uma String de fato, sem que procure por uma view jsp
	@Transactional // para o banco de dados, que precisa de uma transacao
	public String newUserAccess() {
		Usuario usuario = new Usuario();
		usuario.setNome("Admin");
		usuario.setEmail("admin@casadocodigo.com.br");
		usuario.setSenha("$2a$10$lt7pS7Kxxe5JfP.vjLNSyOXP11eHgh7RoPxo5fvvbMCZkCUss2DGu");
		usuario.setRoles(Arrays.asList(new Role("ROLE_ADMIN")));
		
		usuarioDAO.gravar(usuario);
		return "URL magica executada!";
	}

}
