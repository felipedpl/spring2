package br.com.casadocodigo.loja.conf;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// nesta classe vamos informar ao hibernate as configurações para ele gerenciar o BD, como qual BD, usuario, senha...
@EnableTransactionManagement // habilita o gerenciamento de transações a ser feito pelo Spring
public class JPAConfiguration {
	
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource, Properties additionalProperties) {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		factoryBean.setDataSource(dataSource);        
        factoryBean.setJpaProperties(additionalProperties);
        factoryBean.setPackagesToScan("br.com.casadocodigo.loja.models"); // informo o pacote com as entidades persistiveis (o hibernate usa pra criar a estrutura)
        
        return factoryBean;
	}

	@Bean
	@Profile("dev")
	public Properties additionalProperties() {
		Properties props = new Properties(); // propriedades do próprio hibernate
		props.setProperty("hibernate.dialect" , "org.hibernate.dialect.MySQL5Dialect");
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.hbm2ddl.auto", "update");
		return props;
	}

	@Bean
	@Profile("dev")
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource(); //configurando o data source com usuario, senha, localizacao do BD e driver
		dataSource.setUsername("root");
		dataSource.setPassword("");
		dataSource.setUrl("jdbc:mysql://localhost:3306/casadocodigo");
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		return dataSource;
	}
	
	// gerenciador de transações. Por segurança, o Hibernate pede que toda operação seja feita através de uma transação.
	// a partir deste bean, o Spring vai fornecer a transação para o Entity Manager do Hibernate.
	@Bean
	public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
	    return new JpaTransactionManager(emf);
	}
}
