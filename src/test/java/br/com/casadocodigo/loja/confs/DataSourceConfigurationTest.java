package br.com.casadocodigo.loja.confs;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataSourceConfigurationTest {

	@Bean
	@Profile("test") // datasource para test que as partes da aplicacao que nomeiam esse perfil a utilizem
	public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/casadocodigo_test");
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
	}
}
