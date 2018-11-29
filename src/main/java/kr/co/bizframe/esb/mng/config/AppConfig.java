/*                                                                              
 * Copyright 2018 Torpedo corp.                                                 
 *                                                                              
 * bizframe esb-mng-console project licenses this file to you under the Apache License,     
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:                   
 *                                                                              
 *   http://www.apache.org/licenses/LICENSE-2.0                                 
 *                                                                              
 * Unless required by applicable law or agreed to in writing, software          
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT  
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the     
 * License for the specific language governing permissions and limitations      
 * under the License.                                                           
 */ 

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
