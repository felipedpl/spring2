package br.com.casadocodigo.loja.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import br.com.casadocodigo.loja.daos.ProdutoDAO;
import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.CarrinhoItem;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

@Controller
@RequestMapping("/carrinho")
// por default todos o Spring gerencia seus beans,controllers e components utilizando o escopo de aplicação (Singleton), ou seja, cada recurso utilizado será "criado" apenas 1 vez e utilizado por toda aplicação
// para resolver isso, dizemos que o controler vai funcionar por requisição do usuário
@Scope(value=WebApplicationContext.SCOPE_REQUEST)
public class CarrinhoComprasController {

	@Autowired
	private ProdutoDAO produtoDAO;
	
	@Autowired
	private CarrinhoCompras carrinho;
	
	@RequestMapping("/add")
	public ModelAndView add (Integer produtoId, TipoPreco tipoPreco) {
		System.out.println(tipoPreco.name());
		ModelAndView modelAndView = new ModelAndView("redirect:/carrinho");
		CarrinhoItem carrinhoItem = criaItem(produtoId,tipoPreco);
		carrinho.add(carrinhoItem);
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView itens () {
		return new ModelAndView("carrinho/itens");
	}

	private CarrinhoItem criaItem(Integer produtoId, TipoPreco tipo) {
		Produto produto = produtoDAO.find(produtoId);
		return new CarrinhoItem(produto, tipo);
	}
	
	@RequestMapping("/remover")
	public ModelAndView remover(Integer produtoId, TipoPreco tipo) {
		ModelAndView view = new ModelAndView("redirect:/carrinho");
		carrinho.remover(produtoId,tipo);
		return view;
	}
}
