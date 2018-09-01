package main.java.net.dv8tion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import offline.Ref;

public class SQLManager 
{
	Connection conn = null;
	
	public SQLManager()
	{
	    try {
	        conn =
	           DriverManager.getConnection(Ref.SQLURL, Ref.SQLDatabase, Ref.SQLPassword);

	        // Do something with the Connection
	        System.out.println("Connected to Database");
	        
	    } catch (SQLException ex) {
	        // handle any errors
	        System.out.println("SQLException: " + ex.getMessage());
	        System.out.println("SQLState: " + ex.getSQLState());
	        System.out.println("VendorError: " + ex.getErrorCode());
	    }
	}
	
	public void serverJoin(String serverID)
	{
		java.sql.Statement stat = null;
		String query = "SHOW TABLES LIKE '%" 
				+ serverID + "%'";
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(query);
			if(!rs.next())
			{
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
	}
	
	public void populateList(String [] IDs, String serverID) throws SQLException 
	{
		String query = "INSERT INTO BASE_" + serverID + 
				" (USERID, POINTS, AUTH, IGNORED) " +
				"VALUES (" + IDs[0] + ", 0, 0, 0)";
		
		for(int i = 1; i < IDs.length; i ++) 
		{
			query += ",("+IDs[i] + ", 0, 0, 0)";
		}
		query += ";";
		java.sql.Statement stat = conn.createStatement();
		stat.execute(query);
		System.out.println(query);
	}
	
	public Response closeDatabase() throws SQLException
	{
		conn.close();
		Response closer = new Response("Connection Closed");
		return closer;
	}
}
