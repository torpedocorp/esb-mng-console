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

package kr.co.bizframe.esb.mng.type;

public class Constants {
	public static final String TRACE_DB_KEY = "trace";
	public static final String CONFIG_DB_KEY = "config";
	public static final String TRANSACTIONMANAGER_NAME = "TransactionManager";
	public static final String SESSIONFACTORY_NAME = "SessionFactory";

	public static final String SESSION_STATUS = "ESB_MNG_Status";
	public static final String SESSION_USERID = "ESB_MNG_Userid";
	public static final String SESSION_PASSWD = "ESB_MNG_Passwd";
	public static final String ADMIN_USER_ID = "admin";
	public static final String ADMIN_USER_PASS = "0DPiKuNIrrVmD8IUCuw1hQxNqZc=";
	
	public static final String APP_ID_PATTERN = "([a-z-_A-Z-_\\s0-9]*)";
	public static final String CMD_STATUS_PATTERN = "status=\\[([A-Z]*)\\]";
	public static final String MAS_APP_ID_PATTERN = "kr.co.bizframe.mas:id=" + APP_ID_PATTERN;
	public static final String CMD_APP_ID_PATTERN = "application id=\\[" + APP_ID_PATTERN + "\\]";	
	
	public static final String TOTAL = "total";
	public static final String SUCCESS = "success";
	public static final String FAIL = "fail";
	
}
