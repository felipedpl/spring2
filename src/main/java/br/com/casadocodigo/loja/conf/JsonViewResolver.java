package br.com.casadocodigo.loja.conf;

import java.util.Locale;

import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class JsonViewResolver implements ViewResolver {

	// este metodo retornara o Json como view
	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {
		MappingJackson2JsonView jsonView = new MappingJackson2JsonView(); // usa o Jackson para apresentar o Json como view
		jsonView.setPrettyPrint(true); // apresenta o Json formatado
		return jsonView;
	}

}
