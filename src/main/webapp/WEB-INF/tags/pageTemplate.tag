<%@ tag language="java" pageEncoding="ISO-8859-1"%>
<%@attribute name="titulo" required="true" %> <!-- atributos referente ao que vai ser dinamico em cada pagina -->
<%@attribute name="bodyClass" required="false" %>
<%@attribute name="extraScripts" fragment="true" %> <!-- utilizo fragmentos quando quero que determinadas paginas rodem determinados scripts -->

<!-- utilizando template para padronizar e facilitar a manutencao das paginas. Pela especificacao o template é uma tag e de estar dentro da pasta tags embaixo de WEB-INF -->
<!DOCTYPE html>
<html>
<head>
	  <meta charset="utf-8"/>
		  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
		<title>${titulo} - Casa do Código</title>
		<link href="/casadocodigo/resources/css/cssbase-min.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href='http://fonts.googleapis.com/css?family=Droid+Sans:400,700'
			rel='stylesheet'/>
		<link href="/casadocodigo/resources/css/fonts.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href="/casadocodigo/resources/css/fontello-ie7.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href="/casadocodigo/resources/css/fontello-embedded.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href="/casadocodigo/resources/css/fontello.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href="/casadocodigo/resources/css/style.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href="/casadocodigo/resources/css/layout-colors.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href="/casadocodigo/resources/css/responsive-style.css"
			rel="stylesheet" type="text/css" media="all" />
		<link href="/casadocodigo/resources/css/guia-do-programador-style.css" 
			rel="stylesheet" type="text/css"  media="all"  />
	    <link href="/casadocodigo/resources/css/produtos.css" 
	    	rel="stylesheet" type="text/css"  media="all"  />
		<link rel="canonical" href="http://www.casadocodigo.com.br/" />	
		<link href="/casadocodigo/resources/css/book-collection.css"
				rel="stylesheet" type="text/css" media="all" />
</head>
<body class="${bodyClass}">  
<%@include file="/WEB-INF/views/cabecalho.jsp"%>

<jsp:doBody /> <!-- aqui vai o conteudo dinamico de cada pagina -->

<jsp:invoke fragment="extraScripts"></jsp:invoke>

<%@include file="/WEB-INF/views/rodape.jsp" %>
</body>
</html>