package br.com.casadocodigo.loja.infra;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSaver {
	
	@Autowired
	HttpServletRequest request;

	// este método fará de fato o envio do arquivo para uma pasta no servidor
	public String write (String baseFolder, MultipartFile file) {
		try {
			String realPath = request.getServletContext().getRealPath("/"+baseFolder); // obtenho o caminho absoluto do diretório que o usuario esta acessando no servidor e concateno com a nova pasta onde ficará o arquivo
			String path = realPath + "/" + file.getOriginalFilename();
			file.transferTo(new File(path));
			return baseFolder + "/" + file.getOriginalFilename(); // para eu encontrar o arquivo, preciso gravar no banco de dados apenas o caminho relativo, pois acessarei pelo contexto do projeto
		} catch (IllegalStateException | IOException e) {
			throw new RuntimeException(e);
		}
	}
}
