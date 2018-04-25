package br.com.casadocodigo.loja.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(value=WebApplicationContext.SCOPE_SESSION, proxyMode=ScopedProxyMode.TARGET_CLASS) // como minha aplicação pode ter mais de um usuario, cada um terá seu único carrinho de compras. Preciso dizer ao Spring que este componente não pode ser Singleton e sim um componente de sessão.
// deve-se lembrar que o controller deve ser mais especifico (menos abrangente) no seu escopo para não gerar erro de compilação
// o proxy informa ao Spring que todas as classes que utilizam esta, estão no mesmo escopo (Sessão). A classe fica acessivel aos controllers sem precisar mudar o escopo deles
public class CarrinhoCompras implements Serializable {

	/**
	 * o servidor ao ver que o objeto está em escopo de sessão, tenta salvar o objeto em arquivo para que possa ser recuperado. Por isso a classe precisa ser serializavel
	 */
	private static final long serialVersionUID = 1L;
	Map<CarrinhoItem, Integer> itens = new LinkedHashMap<CarrinhoItem, Integer>();
	
	public Collection<CarrinhoItem> getItens() {
		return itens.keySet();
	}
	
	public void add (CarrinhoItem item) {
		itens.put(item, getQuantidade(item) + 1);
		list();
	}

	public int getQuantidade(CarrinhoItem item) {
		if (!itens.containsKey(item)) {
			itens.put(item, 0);
		}
		
		return itens.get(item);
	}
	
	public int getQuantidade() {
		return itens.values().stream().reduce(0, (proximo, acumulador) -> (proximo + acumulador));
	}
	
	public BigDecimal getTotal(CarrinhoItem item) {
		return item.getTotal(getQuantidade(item));
	}
	
	public BigDecimal getTotal () {
		BigDecimal total = BigDecimal.ZERO;
		
		for (CarrinhoItem c : itens.keySet()) {
			total = total.add(getTotal(c));
		}
		
		return total;
	}
	
	public void list() {
		for (Map.Entry<CarrinhoItem, Integer> entry : itens.entrySet()) {
			System.out.println(entry.getKey().getProduto().getTitulo() + " - " + entry.getKey().getTipo() + " - " + entry.getValue());
		}
	}

	public void remover(Integer produtoId, TipoPreco tipo) {
		Produto p = new Produto();
		p.setId(produtoId);
		itens.remove(new CarrinhoItem(p, tipo));
	}
}
