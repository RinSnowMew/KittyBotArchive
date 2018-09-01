package main.java.net.dv8tion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLManager 
{
	Connection conn = null;
	
	public SQLManager()
	{
		
	    try {
	        conn =
	           DriverManager.getConnection("jdbc:mysql://localhost/kittybot", "kittysql", "purple sql");

	        // Do something with the Connection
	        System.out.println("Connected to Database");
	        
	    } catch (SQLException ex) {
	        // handle any errors
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	    }
	}
	
	public boolean serverJoin(String serverID)
	{
		java.sql.Statement stat = null;
		String query = "SHOW TABLES LIKE '%" 
				+ serverID + "%'";
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			if(!rs.next())
			{
				System.out.print("RUNNING QUERY");
				query = "CREATE TABLE `kittybot`.`BASE_" + serverID + "` ("
						+ "  `USERID` VARCHAR(20) NOT NULL,"
						+ "  `POINTS` INT NULL,"
						+ "  `AUTH` TINYINT NULL,"
						+ "  `IGNORED` TINYINT NULL,"
						+ "  PRIMARY KEY (`USERID`));";
				stat.execute(query);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return false;
	}
	
	public Response closeDatabase() throws SQLException
	{
		conn.close();
		Response closer = new Response("Connection Closed");
		return closer;
	}
}
