package br.com.casadocodigo.loja.conf;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ServletSpringMVC extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {// classes de configurações do servlet que são inicializadas ao iniciar a aplicação no servidor
		return new Class[] {SecurityConfiguration.class,AppWebConfiguration.class,JPAConfiguration.class,JPAProdConfiguration.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() { // classes de configurações do servlet que são inicializadas na utilização da aplicação
		return new Class[] {};
	}

	@Override
	protected String[] getServletMappings() { // mapeamentos que o servlet vai atender
		return new String[] {"/"};
	}

	@Override // adiciono este filtro as propriedades do Servlet, para que seja utilizado o encoding universal para texto
	protected Filter[] getServletFilters() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		return new Filter[] {encodingFilter, new OpenEntityManagerInViewFilter()}; // posso adicionar o filtro que vai permitir que o entity manager consiga buscar dados na base após a view carregada
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement("")); // registrador para conversão de dados de arquivos. Como não vou alterar o arquivo recebido passo um parametro vazio
	}
	
	/* Tiro a configuracao onStartup pois vou passar o Profile como parametro ao executar a aplicacao */
	// como tenho mais de um perfil dentro da aplicacao, para os testes, quando o servidor sobe deve saber qual contexto de profile utilizar na aplicacao
/*	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		super.onStartup(servletContext);
		servletContext.addListener(new RequestContextListener());
		servletContext.setInitParameter("spring.profiles.active", "dev");		
	}*/
}
