package br.com.casadocodigo.loja.daos;

import java.math.BigDecimal;
import java.util.List;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.casadocodigo.loja.builders.ProdutoBuilder;
import br.com.casadocodigo.loja.conf.JPAConfiguration;
import br.com.casadocodigo.loja.confs.DataSourceConfigurationTest;
import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

@RunWith(SpringJUnit4ClassRunner.class) // solicito rodar os testes no contexto do Spring
@ContextConfiguration(classes= {JPAConfiguration.class,ProdutoDAO.class,DataSourceConfigurationTest.class}) // informo quais classes de configuracao do contexto Spring vou precisar para rodar meus testes
@ActiveProfiles("test") // criamos um banco de dados apartado para testes, e esta classe o utilizara durante execucao
public class ProdutoDAOTest {

	@Autowired
	private ProdutoDAO produtoDao; // o produtoDAO utiliza o entity manager, que só existe no contexto do Spring. Precisamos pegar o contexto para evitar nullPointer

	@Test
	@Transactional // preciso dessa anotação por conta do acesso a base, que precisa de uma transação para ocorrer
	public void deveSomarTodosOsPrecosPorTipoLivro () {	
		List<Produto> impressos = ProdutoBuilder.newProduto(TipoPreco.IMPRESSO, BigDecimal.TEN).more(3).buildAll();
		List<Produto> ebooks = ProdutoBuilder.newProduto(TipoPreco.EBOOK, BigDecimal.TEN).more(3).buildAll();
		
		impressos.stream().forEach(produtoDao::gravar);
		ebooks.stream().forEach(produtoDao::gravar);
		
		BigDecimal valor = produtoDao.somaProdutoPorTipoPreco(TipoPreco.IMPRESSO);
		Assert.assertEquals(new BigDecimal(40).setScale(2), valor);
	}
}
