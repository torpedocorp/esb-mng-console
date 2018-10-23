package kr.co.bizframe.esb.mng.config;

import static kr.co.bizframe.esb.mng.type.Constants.CONFIG_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.SESSIONFACTORY_NAME;
import static kr.co.bizframe.esb.mng.type.Constants.TRACE_DB_KEY;
import static kr.co.bizframe.esb.mng.type.Constants.TRANSACTIONMANAGER_NAME;
import static org.hibernate.cfg.AvailableSettings.C3P0_ACQUIRE_INCREMENT;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_MAX_STATEMENTS;
import static org.hibernate.cfg.AvailableSettings.C3P0_MIN_SIZE;
import static org.hibernate.cfg.AvailableSettings.C3P0_TIMEOUT;
import static org.hibernate.cfg.AvailableSettings.DRIVER;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_AUTO;
import static org.hibernate.cfg.AvailableSettings.HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR;
import static org.hibernate.cfg.AvailableSettings.PASS;
import static org.hibernate.cfg.AvailableSettings.SHOW_SQL;
import static org.hibernate.cfg.AvailableSettings.URL;
import static org.hibernate.cfg.AvailableSettings.USER;
import static org.hibernate.cfg.AvailableSettings.DIALECT;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource(value = { "classpath:db.properties" })
@EnableTransactionManagement
@ComponentScan(basePackages = { "kr.co.bizframe.esb.mng.*" })
public class AppConfig {

	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Environment env;

	public LocalSessionFactoryBean getSessionFactory(String key) {
		LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();

		Properties props = new Properties();
		// Setting JDBC properties
		props.put(DRIVER, env.getProperty(key + ".db.driver"));
		props.put(URL, env.getProperty(key + ".db.url"));
		props.put(USER, env.getProperty(key + ".db.user"));
		props.put(PASS, env.getProperty(key + ".db.password"));
		
		// Setting Hibernate properties
		props.put(DIALECT, env.getProperty(key + ".hibernate.dialect", "org.hibernate.dialect.DerbyDialect"));
		props.put(SHOW_SQL, env.getProperty(key + ".hibernate.show_sql", "false"));
		props.put(HBM2DDL_AUTO, env.getProperty(key + ".hibernate.hbm2ddl.auto"));
		//props.put(HBM2DDL_IMPORT_FILES, env.getProperty(key + ".hibernate.hbm2ddl.import_files"));
		props.put(HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR, "org.hibernate.tool.hbm2ddl.MultipleLinesSqlCommandExtractor");

		// Setting C3P0 properties
		props.put(C3P0_MIN_SIZE, env.getProperty("hibernate.c3p0.min_size"));
		props.put(C3P0_MAX_SIZE, env.getProperty("hibernate.c3p0.max_size"));
		props.put(C3P0_ACQUIRE_INCREMENT, env.getProperty("hibernate.c3p0.acquire_increment"));
		props.put(C3P0_TIMEOUT, env.getProperty("hibernate.c3p0.timeout"));
		props.put(C3P0_MAX_STATEMENTS, env.getProperty("hibernate.c3p0.max_statements"));

		factoryBean.setHibernateProperties(props);
		factoryBean.setPackagesToScan("kr.co.bizframe.esb.mng.model." + key);

		return factoryBean;
	}

	@Bean(name = TRACE_DB_KEY + SESSIONFACTORY_NAME)
	public LocalSessionFactoryBean getTraceSessionFactory() {
		return getSessionFactory(TRACE_DB_KEY);
	}

	@Bean(name = CONFIG_DB_KEY + SESSIONFACTORY_NAME)
	public LocalSessionFactoryBean getConfigSessionFactory() {
		return getSessionFactory(CONFIG_DB_KEY);
	}

	public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();
		transactionManager.setSessionFactory(sessionFactory);
		return transactionManager;
	}

	@Bean(name = TRACE_DB_KEY + TRANSACTIONMANAGER_NAME)
	public HibernateTransactionManager getTraceTransactionManager() {
		return getTransactionManager(getTraceSessionFactory().getObject());
	}

	@Bean(name = CONFIG_DB_KEY + TRANSACTIONMANAGER_NAME)
	public HibernateTransactionManager getConfigTransactionManager() {
		return getTransactionManager(getConfigSessionFactory().getObject());
	}
}
