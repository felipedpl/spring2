package br.com.casadocodigo.loja.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.google.common.cache.CacheBuilder;

import br.com.casadocodigo.loja.controllers.HomeController;
import br.com.casadocodigo.loja.daos.ProdutoDAO;
import br.com.casadocodigo.loja.infra.FileSaver;
import br.com.casadocodigo.loja.models.CarrinhoCompras;

@EnableWebMvc
@ComponentScan(basePackageClasses={HomeController.class,ProdutoDAO.class,FileSaver.class,CarrinhoCompras.class}) // recebe as classes que devem ser varridas pelo Spring (Component genericos e especificos)
@EnableCaching // para que o cache funcione na aplicação, preciso fazer esta habilitação e criar um gerenciador de cache para o Spring utilizar (CacheManager)
public class AppWebConfiguration extends WebMvcConfigurerAdapter {

	// configuração para o meu servlet encontrar as views do meu projeto
	@Bean // falamos que quem vai gerenciar o retorno deste método é o Spring
	public InternalResourceViewResolver internalResourceViewResolver () {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/views/"); // as views serão encontradas neste caminho. Por default a pasta WEB-INF é protegida no servidor, 
											// evitando que os arquivos nela contidos sejam acessados diretamente pela URL sem passar pelo controller da aplicação, podendo quebrar regras de negócio.
		resolver.setSuffix(".jsp"); // as views terão esta terminação
		resolver.setExposedContextBeanNames("carrinhoCompras"); // disponibilizo beans específicos para as minhas páginas utilizarem.
		return resolver;
	}
	
	@Bean
	public MessageSource messageSource () {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("/WEB-INF/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(1); // frequecia de carregamento do meu arquivo. Caso precise mudar a mensagem exibida, não é necessario reiniciar o servidor
		return messageSource; // retorno o arquivo de mensagens que o Spring vai utilizar pra identificar os textos
	}
	
	@Bean // como o formato de data brasileiro vai ser um padrão no sistema, centralizamos ele para a aplicação
	public FormattingConversionService mvcConversionService () {
		DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();
		DateFormatterRegistrar registrar = new DateFormatterRegistrar(); // guarda o padrao de data
		registrar.setFormatter(new DateFormatter("dd/MM/yyyy")); // padrao
		registrar.registerFormatters(conversionService); // servico responsavel
		
		return conversionService;
	}
	
	@Bean // estamos cadastrando uma configuração que resolverá envio de arquivos na aplicação. O objeto identifica os tipos dos recursos (PDF, JPG, .. ) e fornece uma forma mais simples de manipulá-los
	public MultipartResolver multipartResolver () {
		return new StandardServletMultipartResolver();
	}
	
	@Override // mapeia os recursos (css, imagens, js) a partir de um path
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}
    
    @Bean // o Spring precisa dessa declaração para poder criar este objeto corretamente
    public RestTemplate RestTemplate() {
    	return new RestTemplate();
	}
    
    @Bean // devolvo para o Spring utilizar um gerenciador de cache para aplicação
    public CacheManager cacheManager() {
    	// utilizo o framework de cache do Google (Guava) que permite configurar a quantidade de elementos (chave->valor), tempo de expiração, etc...
    	CacheBuilder<Object,Object> builder = CacheBuilder.newBuilder().maximumSize(100).expireAfterAccess(5, TimeUnit.MINUTES); // guarda 100 elementos e expira a cada 5 minutos
    	GuavaCacheManager manager = new GuavaCacheManager();
    	manager.setCacheBuilder(builder);
    	return manager;    													
    }
    
    // padrao de mercado que configura um negociador de conteudo que resolve requisições e devolve em formatos diferentes a partir de uma mesma URL (mudando apenas a extensão, no caso do .json)
    @Bean
    public ViewResolver contentNegotiationViewResolver(ContentNegotiationManager manager) {
    	List<ViewResolver> viewResolvers = new ArrayList<ViewResolver>();
    	viewResolvers.add(internalResourceViewResolver()); // adiciono o resolver existente ate o momento para html/jsp
    	viewResolvers.add(new JsonViewResolver()); // crio um resolver para Json
    	ContentNegotiatingViewResolver resolver = new ContentNegotiatingViewResolver();
    	resolver.setViewResolvers(viewResolvers); // incluo os tipos de resolvers
    	resolver.setContentNegotiationManager(manager); // incluo o objeto que vai gerencia-los
    	return resolver;
    }
	
    // aponta para o Servlet default atender requisições de recursos (resources), impedindo o servlet da aplicação de tentar interpretá-los como páginas
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    // adiciono um interceptador que vai interpretar a linguagem escolhida pelo usuario no cabecalho, mudando o idioma do site
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	registry.addInterceptor(new LocaleChangeInterceptor());
    }
    
    // guardo a linguagem definida pelo usuario num cookie
    @Bean
    public LocaleResolver localeResolver () {
    	return new CookieLocaleResolver();
    }
    
    @Bean
    public MailSender mailSender() {
    	JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
    	javaMailSenderImpl.setHost("smtp.live.com");
    	javaMailSenderImpl.setUsername("felipedpl@hotmail.com");
    	javaMailSenderImpl.setPassword("");
    	javaMailSenderImpl.setPort(587);
    	
    	// habilito as configuracoes de seguranca para o envio do email
    	Properties prop = new Properties();
    	prop.put("mail.smtp.auth", true);
    	prop.put("mail.smtp.starttls.enable", true);
    	
    	javaMailSenderImpl.setJavaMailProperties(prop);
    	return javaMailSenderImpl;
    }
}
