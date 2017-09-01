package br.com.transactions;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import br.com.transactions.constants.Constants;

@Configuration
@Profile(Constants.PROFILE_LOCAL)
@PropertySource({ "classpath:application.properties" })
public class DatasourceConfigLocal {

	@Autowired
	Environment env;
	
	@Bean
	@Primary
	public DataSource dataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(env.getProperty("datasource.driverClassName"));
		ds.setUrl(env.getProperty("datasource.jdbcUrl"));
		ds.setUsername(env.getProperty("datasource.username"));
		ds.setPassword(env.getProperty("datasource.password"));
		
		return ds;
	}
}
