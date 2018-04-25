package br.com.casadocodigo.loja.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice // essa anotacao configura o controller (monitorando os outros controllers) para atender, neste caso, excecoes geradas dentro de qualquer controller, centralizando o tratamento
public class ExceptionHandlerController {
	
	@ExceptionHandler(Exception.class) // quais excecoes serão tratadas
	public ModelAndView handleExceptions(Exception exception) {
		exception.printStackTrace();
		ModelAndView modelAndView = new ModelAndView("error");
		modelAndView.addObject("exception", exception);
		return modelAndView;
	}
}
