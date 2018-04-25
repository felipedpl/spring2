package br.com.casadocodigo.loja.controllers;

import java.util.List;

import javax.persistence.NoResultException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.daos.ProdutoDAO;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;
import br.com.casadocodigo.loja.validation.ProdutoValidation;

@Controller
@RequestMapping("/produtos") // como todo mapeamento interno é precedido por /produto, faz sentido ele ser um mapeamento do Controller para evitar repetição
public class ProdutoController {
	
	@Autowired
	private ProdutoDAO produtoDAO;
	
	@Autowired
	private FileSaver fileSaver;
	
	@InitBinder // faço o vinculo do validador com o Controller
	public void initBinder (WebDataBinder binder) {
		binder.addValidators(new ProdutoValidation());
	}

	@RequestMapping("/form") // determina que o metodo atende ao endereço determinado
	public ModelAndView form (Produto produto) {
		ModelAndView modelAndView = new ModelAndView("produtos/form"); // transporta informações para a view especificada
		modelAndView.addObject("tipos",TipoPreco.values()); // envia os tipos de preços
		System.out.println("Página para cadastrar um produto");
		return modelAndView;
	}
	
	@RequestMapping(method=RequestMethod.POST) // determina que o metodo atende ao endereço determinado
	@CacheEvict(value="produtosHome",allEntries=true) // toda vez que incluir um novo livro, limpo o cache para que a lista atualizada seja recarregada e colocada em cache novamente
	public ModelAndView cadastrar (MultipartFile sumario, @Valid Produto produto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		// MultipartFile - objeto pelo qual o Spring envia arquivos
		// se qualquer erro for colocado ao fazer o bind do objeto, retorno ao formulario e nao gravo na base
		// o BindingResult deve vir logo após ao objeto anotado com o Valid
		
		System.out.println(sumario.getOriginalFilename());
		if (bindingResult.hasErrors()) {
			return form(produto);
		}
		
		System.out.println(produto);
		String sumarioPath = fileSaver.write("arquivos-sumario", sumario); // passo para a classe de infra responsável por mandar o arquivo para o servidor. Lembrando que a pasta deve ser previamente criada na estrutura
		produto.setSumarioPath(sumarioPath); // gravo o caminho no atributo
		produtoDAO.gravar(produto);
		redirectAttributes.addFlashAttribute("sucesso", "Produto gravado com sucesso"); // como o redirect envolve 2 request (GET - REDIRECT), para enviar qualquer objeto até o 2º request, 
																				   // quando a lista de fato é carregada, temos que usar o escopo FLASH que dura 1 request.
		return new ModelAndView("redirect:/produtos"); // ALWAYS REDIRECT AFTER POST. Redireciono após o cadastro para a lista de produtos (é entendido como um GET),
													  // assim evito que o form possa ser resubmetido N vezes pelo navegador, evitando o reenvio de dados também ao banco de dados
													  // Com o redirect, após submeter o form é feita uma requisição com Status:302 para a lista
	}
	
	// estou diferenciando este mapeamento do de cima pela tipo de metodo usado pelo protocolo HTTP.
	// Como o form faz um post e este caso é uma listagem, faz sentido que seja um GET
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView listar () {
		ModelAndView modelAndView = new ModelAndView("produtos/lista");
		List<Produto> produtos = produtoDAO.listar();
		modelAndView.addObject("produtos", produtos);
		return modelAndView;
	}
	
	@RequestMapping("/detalhe/{id}") // URL amigável fornecida pelo proprio spring
	public ModelAndView detalhe (@PathVariable("id") Integer id) {
		ModelAndView modelAndView = new ModelAndView("produtos/detalhe");
		Produto produto = produtoDAO.find(id);
		modelAndView.addObject("produto", produto);
		return modelAndView;
	}
	
	@ExceptionHandler(NoResultException.class)
	public ModelAndView excecaoProduto(Exception exception) {
		System.out.println("Tratamento especifico para erros com produtos");
		exception.printStackTrace();
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("exception", exception);
		return modelAndView;
	}
	
/*	// não é necessario ficar criando metodos semelhantes aos existentes apenas para retornar um objeto JSON para facilitar a integração.
	// deve-se configurar alguem que vai resolver (negociar) os conteudos de acordo com o acesso do usuario (html/jsp ou json)
	// com a integração com o Jackson, o Spring vai garantir o bind do objeto para a tela no formato JSON. Se tivessemos mais de uma biblioteca para transformar objetos JSON teriamos conflito
	@RequestMapping("/{id}") 
	@ResponseBody // informo ao Spring que o retorno é o objeto e não uma página
	public Produto detalheJson (@PathVariable("id") Integer id) {
		return produtoDAO.find(id);
	}*/
}
