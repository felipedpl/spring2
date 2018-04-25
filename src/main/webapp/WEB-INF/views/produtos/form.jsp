<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!-- Import da taglib - JSTL -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %> <!-- taglib de forms do spring -->
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %> <!-- taglib do spring para gerar dinamicamente a action /casadocodigo/produtos -->
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html>
<head>
<meta charset=UTF-8">
<c:url value="/resources/css" var="cssPath" />
<link rel="stylesheet" href="${cssPath }/bootstrap.min.css" />
<link rel="stylesheet" href="${cssPath }/bootstrap-theme.min.css" />

<title>Livros de Java, Android, etc</title>
</head>
<body>
<nav class="navbar navbar-inverse">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${s:mvcUrl('HC#index').build()}">Casa do Código</a>
    </div>
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav">
        <li><a href="${s:mvcUrl('PC#listar').build()}">Lista de Produtos</a></li>
        <li><a href="${s:mvcUrl('PC#form').build()}">Cadastro de Produtos</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
		  <li><a href="#"><security:authentication property="principal" var="usuario" />
		  	Usuario: ${usuario.username }
		  </a></li>
		  <li><a href="/casadocodigo/logout">LOGOUT</a></li>
	  </ul>
	  
    </div><!-- /.navbar-collapse -->
  </div>
</nav>

	<div class="container">
	
		<!-- mvcUrl('PC#cadastrar').build() - o Spring monta dinamicamente a action pelo controller (PC) ProdutoController [não preciso nem informer o nome completo, apenas as iniciais case sensitive]. Por fim, informo o método para o qual vou mandar os campos e chamo o build para criar de fato a action  -->
		<form:form action="${s:mvcUrl('PC#cadastrar').build()}" method="post" commandName="produto" enctype="multipart/form-data"> <!-- utilizando o form do spring, posso colocar o prefixo que os campos utilizam na raiz com commandName. Com o enctype dizemos que o form podera enviar arquivos para o Controller -->
		    <div class="form-group">
		        <label>Título</label>
		        <form:input path="titulo" cssClass="form-control" /> <!-- utilizamos o input do spring, para que os campos validos do formulario sejam mantidos em caso de erro em outro campo. Nao precisamos passar o tipo pois vamos assegurar os valores via objeto (Produto) que sera recebido pelo form -->
		        <form:errors path="titulo" />
		    </div>
		    <div class="form-group">
		        <label>Descrição</label>
		        <form:textarea path="descricao"  cssClass="form-control" />
		        <form:errors path="descricao" />
		    </div>
		    <div class="form-group">
		        <label>Páginas</label>
		        <form:input path="paginas" cssClass="form-control" />
		        <form:errors path="paginas" />
		    </div>
		    <div class="form-group">
		    	<label>Data de Lançamento</label>
		    	<form:input path="dataLancamento" cssClass="form-control" />
		    	<form:errors path="dataLancamento" />
		    </div>
		    
		    <c:forEach items="${tipos}" var="tipoPreco" varStatus="status"> <!-- lista: tipos, cada valor: tipoPreco, index: status -->
			    <div class="form-group">
			        <label>${tipoPreco}</label> <!-- representa cada item do enum de TipoPreco -->
			        <form:input path="precos[${status.index}].valor" cssClass="form-control" /> <!-- preencho a lista de precos do Produto com o elemento controlado pelo indice pegando o atributo valor -->
			        <form:hidden path="precos[${status.index}].tipo" value="${tipoPreco}" /> <!-- preencho a lista de precos do Produto com o elemento controlado pelo indice pegando o atributo tipo --> 
			    </div>
		    </c:forEach>
		    <div class="form-group">
		    	<label>Sumário</label>
		    	<input name="sumario" type="file" class="form-control" /> <!-- para estilizar as tags que nao sao form do Spring, utiliza o class ao inves de cssClass -->
		    </div>
		    <button type="submit" class="btn btn-primary">Cadastrar</button>
		</form:form>
	</div>
</body>
</html>