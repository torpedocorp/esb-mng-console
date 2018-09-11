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
