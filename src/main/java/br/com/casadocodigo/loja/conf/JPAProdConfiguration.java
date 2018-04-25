package br.com.casadocodigo.loja.conf;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Profile("prod")
public class JPAProdConfiguration {
	
	@Autowired
	private Environment environment; // vou utlizar este objeto do Spring para pegar as configuracoes, via variavel de ambiente, de banco de dados do servidor de hospedagem, as quais não tenho controle
	
	@Bean
	public Properties additionalProperties() {
		Properties props = new Properties(); // propriedades do próprio hibernate
		props.setProperty("hibernate.dialect" , "org.hibernate.dialect.PostgreSQLDialect");
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
		return props;
	}

	@Bean
	public DataSource dataSource() throws URISyntaxException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(); //configurando o data source com usuario, senha, localizacao do BD e driver
		dataSource.setDriverClassName("org.postgresql.Driver");

		URI dbUri = new URI(environment.getProperty("DATABASE_URL")); // o servidor Heroku disponibiliza as conf de BD através desta variavel e no formato URI usuario:senha@host:port/path, evitando manipular String
		dataSource.setUrl("jdbc:postgresql://" + dbUri.getHost() + ":" + dbUri.getPort() + dbUri.getPath());
		dataSource.setUsername(dbUri.getUserInfo().split(":")[0]);
		dataSource.setPassword(dbUri.getUserInfo().split(":")[1]);
		return dataSource;
	}

}
