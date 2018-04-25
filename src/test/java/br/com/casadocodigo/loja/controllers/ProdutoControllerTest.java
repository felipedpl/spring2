package br.com.casadocodigo.loja.controllers;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.casadocodigo.loja.conf.AppWebConfiguration;
import br.com.casadocodigo.loja.conf.JPAConfiguration;
import br.com.casadocodigo.loja.conf.SecurityConfiguration;
import br.com.casadocodigo.loja.confs.DataSourceConfigurationTest;

@WebAppConfiguration // como vou fazer uma requisicao pra pagina, preciso carregar as outras configuracoes de MVC do Spring
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {JPAConfiguration.class, AppWebConfiguration.class, DataSourceConfigurationTest.class,SecurityConfiguration.class})
@ActiveProfiles("test")
public class ProdutoControllerTest {

	@Autowired
	private WebApplicationContext wac; // preciso de um contexto para fazer requisicoes
	
	@Autowired
	private Filter springSecurityFilterChain;
	
	private MockMvc mockMvc; // objeto que vai simular as requisicoes para os controllers das paginas
	
	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).addFilters(springSecurityFilterChain).build(); // antes de iniciar os testes, preciso construir o objeto de requisicao a partir do contexto conhecido pelo Spring
	}
	
	@Test
	public void deveRetornarParaHomeComOsLivros() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/")) // faço um request com GET para o endereco / (home)
				.andExpect(MockMvcResultMatchers.model().attributeExists("produtos")) // esperado que seja carregado o objeto produto pra pagina
				.andExpect(MockMvcResultMatchers.forwardedUrl("/WEB-INF/views/home.jsp")); // resultado de pagina esperado
	}
	
	@Test
	public void somenteAdminDeveAcessarProdutosForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/produtos/form") // verificar quem tem acesso a url de cadastro de produto
			.with(SecurityMockMvcRequestPostProcessors
			.user("user@casadocodigo.com.br")
			.password("123456")
			.roles("USUARIO")))
			.andExpect(MockMvcResultMatchers.status().is(403)); // status de redirecionamento para um usuario sem permissao
	}
}
