package kr.co.bizframe.esb.mng.config;

import static kr.co.bizframe.esb.mng.type.Constants.CONFIG_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;
import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = { "kr.co.bizframe.esb.mng.*" })
public class AppConfig {

	private Logger logger = Logger.getLogger(getClass());

	@Bean(name = TRACE_DB_KEY + SESSIONFACTORY_NAME)
	public LocalEntityManagerFactoryBean getTraceSessionFactory() {
	      LocalEntityManagerFactoryBean factory = new LocalEntityManagerFactoryBean();
	      factory.setPersistenceUnitName(TRACE_DB_KEY);
	      return factory;
	}


	@Bean(name = CONFIG_DB_KEY + SESSIONFACTORY_NAME)
	public LocalEntityManagerFactoryBean getConfigSessionFactory() {
	      LocalEntityManagerFactoryBean factory = new LocalEntityManagerFactoryBean();
	      factory.setPersistenceUnitName(CONFIG_DB_KEY);
	      return factory;
	}
}
