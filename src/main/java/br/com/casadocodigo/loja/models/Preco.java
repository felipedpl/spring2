package br.com.casadocodigo.loja.models;

import java.math.BigDecimal;

import javax.persistence.Embeddable;

@Embeddable // permite ao Spring relacionar e portar o elemento pra dentro de uma colecao, não obrigando a gerar um ID, que neste caso, não faz sentido existir
public class Preco {

	private BigDecimal valor;
	private TipoPreco tipo;
	
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	public TipoPreco getTipo() {
		return tipo;
	}
	public void setTipo(TipoPreco tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return this.tipo.name() + " - " + this.valor;
	}
}
