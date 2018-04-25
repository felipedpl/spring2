package br.com.casadocodigo.loja.controllers;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigo.loja.models.CarrinhoCompras;
import br.com.casadocodigo.loja.models.DadosPagamento;
import br.com.casadocodigo.loja.models.Usuario;

@Controller
@RequestMapping("/pagamento")
public class PagamentoController {

	@Autowired
	CarrinhoCompras carrinho;
	
	@Autowired
	RestTemplate restTemplate; // classe do Spring que permite fazer requisicoes externas (integração)
	
	@Autowired
	private MailSender mailsender; // interface para envio de e-mails, preciso defini-la na configuracao
	
	@RequestMapping(value="/finalizar", method=RequestMethod.POST)
	public Callable<ModelAndView> finalizar(@AuthenticationPrincipal Usuario usuario, RedirectAttributes attributes) { // Utilizamos o Callable para que apenas o usuario que esta realizando o pagamento aguarde o retorno da requisição ao sistema externo. 
																			//De forma assincrona, o sistema deixar de se comportar como mono-thread, liberando a utilização para outros usuários
																			// o AutheticationPrincipal utiliza o security do spring para pegar o usuario da sessao finalizando a compra
		return () -> { //utilizado recurso do Java8. Callable é uma classe anonima com um metodo (call). Deste modo implementamos o call retornando o objeto corretamente
			System.out.println(carrinho.getTotal());
			ModelAndView modelAndView = new ModelAndView("redirect:/produtos");
			String uri = "http://book-payment.herokuapp.com/payment";
			try {
				String response = restTemplate.postForObject(uri, new DadosPagamento(carrinho.getTotal()), String.class); // método que envia um objeto, neste caso JSON, numa requisição post para a url informada, esperando um retorno do tipo String			
				attributes.addFlashAttribute("sucesso", response);
				System.out.println("Pagamento processado, enviando e-mail");
				enviaEmailSobreCompra(usuario);
			}
			catch (HttpClientErrorException e) {
				e.printStackTrace();
				attributes.addFlashAttribute("falha", "Valor maior que o permitido");
			}
			return modelAndView;
		};
	}

	private void enviaEmailSobreCompra(Usuario usuario) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setSubject("Venda finalizada com sucesso");
		//mailMessage.setTo(usuario.getEmail());
		mailMessage.setTo("fe_dpl@yahoo.com.br");
		mailMessage.setFrom("felipedpl@hotmail.com");
		mailMessage.setText("Compra realizada no valor de " + carrinho.getTotal());
		System.out.println("disparando email");
		mailsender.send(mailMessage);
	}
}
