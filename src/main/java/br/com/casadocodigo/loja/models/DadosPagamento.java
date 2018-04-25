package br.com.casadocodigo.loja.models;

import java.math.BigDecimal;

// classe utilizada que normaliza as informações requeridas pelo sistema externo, permitindo o binding correto do Spring para o formato JSON 
public class DadosPagamento {

	private BigDecimal value; // os atributos precisam ter o mesmo nome do atributo JSON {"value":500}

	public DadosPagamento(BigDecimal value) {
		this.value = value;
	}
	
	public DadosPagamento() {
		// TODO Auto-generated constructor stub
	}

	// o Spring faz o binding pelo get do atributo (utiliza a api do jackson durante o binding)
	public BigDecimal getValue() {
		return value;
	}
}
