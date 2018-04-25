<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="tags" %>

<tags:pageTemplate titulo="Livros de Java, Android, iOS, Mobile e muito mais">
	<section id="index-section" class="container middle">
		<h2>Problema no servidor</h2>
		
		<!-- Evita o estouro da exceção para o usuario na tela, mas disponibiliza no codigo o trace.
			Mensagem: ${exception.message}
			Trace: 
			<c:forEach items="${exception.stackTrace}" var="stk">
				${stk}
			</c:forEach>
			
		 -->
	</section>
</tags:pageTemplate>