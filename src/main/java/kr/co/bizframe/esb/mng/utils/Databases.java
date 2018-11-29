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

package kr.co.bizframe.esb.mng.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Databases {
	public static void closeQuietly(Connection conn) {
		if (conn == null)
			return;
		try {
			conn.close();
		} catch (SQLException sqle) {			
			System.out.println("Could not close connection " + sqle.getMessage());
		}
	}

	public static void closeQuietly(Statement st) {
		if (st == null)
			return;
		try {
			st.close();
		} catch (SQLException sqle) {
			System.out.println("Could not close prepared statement " + sqle.getMessage());
		}
	}

	public static void closeQuietly(ResultSet rs) {
		if (rs == null)
			return;
		try {
			rs.close();
		} catch (SQLException sqle) {
			System.out.println("Could not close result set " + sqle.getMessage());
		}
	}
}
