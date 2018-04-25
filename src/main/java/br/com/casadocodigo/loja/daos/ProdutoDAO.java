package br.com.casadocodigo.loja.daos;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import br.com.casadocodigo.loja.models.Produto;
import br.com.casadocodigo.loja.models.TipoPreco;

@Repository // informo ao Spring que este é um recurso (Component) a ser gerenciado por ele (bean do Spring). Não utilizo @Component por ser um recursos persistente (que grava dados)
// o DAO separa o modelo da infraestrutura, onde vamos solicitar acesso de fato ao BD
@Transactional // dizemos ao Spring que uma transação é necessária para o Entity Manager
public class ProdutoDAO {
	
	@PersistenceContext // injeção de dependencia. Nao usamos @autowired por ser um recurso persistente (que grava dados)
	private EntityManager em;
	
	public void gravar (Produto produto) {
		em.persist(produto);
	}

	public List<Produto> listar() {
		List<Produto> resultList = em.createQuery("select distinct(p) from Produto p join fetch p.precos", Produto.class).getResultList(); // faço fetch das coleções para exibição na view (evita LazyInicializationException)
		return resultList;
	}

	public Produto find(Integer id) {
		return em.createQuery("select distinct(p) from Produto p join fetch p.precos precos where p.id = :id",Produto.class).setParameter("id", id).getSingleResult();
	}

	public BigDecimal somaProdutoPorTipoPreco(TipoPreco tipo) {
		TypedQuery<BigDecimal> query = em.createQuery("select sum(preco.valor) from Produto p join p.precos preco where preco.tipo = :tipo", BigDecimal.class);
		query.setParameter("tipo", tipo);
		return query.getSingleResult();
	}
}
