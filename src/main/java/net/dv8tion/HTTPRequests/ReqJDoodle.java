/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package HTTPRequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

import main.java.net.dv8tion.Ref;
import main.java.net.dv8tion.Response;

/**
 *
 * @author Rin
 */
public class ReqJDoodle 
{

	private static final Gson jsonParser_ = new Gson();
	private class JDoodleObject
	{
		public String output;
		public String cpuTime;
	}
	
	public Response getJDoodle(String query)
	{
		String result = "";
		String lang = "java"; 
		String version = "0";
		query = query.replace("\\n", "\\\\n");
		query = query.replace("\\t", "\\\\t");
		query = query.replace("\n", "\\n");
		query = query.replace("\"", "\\\"");
		query = query.replace("\t", "\\t");

		String input = "{\"clientId\": \"" + Ref.jdoodleID + "\",\"clientSecret\":\"" + Ref.jdoodleSecret + "\",\"script\":\"" + query +
	            "\",\"language\":\"" + lang + "\",\"versionIndex\":\"" + version + "\"} ";
		
		 try {
	            URL url = new URL("https://api.jdoodle.com/v1/execute");
	            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	            connection.setDoOutput(true);
	            connection.setRequestMethod("POST");
	            connection.setRequestProperty("Content-Type", "application/json");
	            
	            //result += input + "\n";

	            OutputStream outputStream = connection.getOutputStream();
	            outputStream.write(input.getBytes());
	            outputStream.flush();

	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            	result = "Please check your inputs : HTTP error code : "+ connection.getResponseCode();
	                return new Response(result);
	            }

	            BufferedReader bufferedReader;
	            bufferedReader = new BufferedReader(new InputStreamReader((connection.getInputStream())));
	            
	            String output;
	            String fullOut = ""; 
	            result += "This is what happened! \n";
	            while ((output = bufferedReader.readLine()) != null) 
	            {
	            	fullOut += output + "\n";
	            }
	            JDoodleObject doodle = jsonParser_.fromJson(fullOut, JDoodleObject.class);
	            
	            result += doodle.output + "\n";
	            result += "The CPU time was " + doodle.cpuTime + "\n";
	            connection.disconnect();
	            return new Response(result);
		 } catch (MalformedURLException e) 
		 {
			 return new Response("You probably shouldn't be seeing this");
	     } catch (IOException e) 
		 {
	         return new Response("You probably shouldn't be seeing this");
	     }
		 
	}
}
