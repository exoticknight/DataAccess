package dao;

import java.sql.*;
import java.util.Vector;

public class DataAccess {
	private String url;
	private Statement stmt = null;
	private Connection conn = null;

	/**
	 * constructor
	 */
	public DataAccess(String database_name) {
		// try to load driver
		try {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("can't find driver!");
			e.printStackTrace();
		}

		// prepare the url
		this.url = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ="
				+ database_name;
	}

	private void getConnection() {
		try {
			// connect to the database
			this.conn = DriverManager.getConnection(this.url);

			// create a Statement
			this.stmt = this.conn.createStatement();

		} catch (SQLException e) {
			System.out.println("fail to connect to database");
			e.printStackTrace();
		}
	}

	/**
	 * query
	 */
	public Vector<String[]> query(String sql) {
		try {
			System.out.println(sql);
			getConnection();
			ResultSet rs = stmt.executeQuery(sql);
			Vector<String[]> records = null;
			// get the value from ResultSet, save to records
			if (rs.next()) {
				records = new Vector<String[]>();
				ResultSetMetaData rsmd = rs.getMetaData();
				int cols = rsmd.getColumnCount();
				String[] header = new String[cols];
				for (int i = 0; i < cols; ++i) {
					header[i] = rsmd.getColumnName(i + 1);
				}
				records.addElement(header);
				do {
					String[] currentRow = new String[cols];
					for (int i = 0; i < cols; ++i) {
						currentRow[i] = rs.getString(i + 1);
					}
					records.addElement(currentRow);
				} while (rs.next());
			}
			rs.close();
			this.closeConnection();
			return records;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * insert/delete/modify
	 */
	public int exec(String sql) {
		int flag = -1;
		try {
			System.out.println(sql);
			ResultSet rs = null;
			getConnection();
			flag = stmt.executeUpdate(sql);
			rs = stmt.executeQuery("SELECT @@identity");
			if (rs.next()) {
				int temp = rs.getInt(1);
				flag = temp != 0 ? temp : flag; 
				System.out.println(flag);
			}
			rs.close();
			this.closeConnection();
			return flag;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return flag;
		}
	}

	/**
	 * close the database
	 */
	public void closeConnection() {

		// close Statement
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			System.out.println(e + ":stmt error");
		} finally {
			// close connection
			try {
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e + ":conn error");
			} finally {
				System.out.println("database closed.");
			}
		}
	}
}
