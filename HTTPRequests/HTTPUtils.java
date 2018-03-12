package main.java.net.dv8tion.HTTPRequests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import main.java.net.dv8tion.Response;


/**
 * Adjustments to the excellent code provided via:
 * https://www.journaldev.com/7148/java-httpurlconnection-example-java-http-request-get-post
 * @author Wisp
 */
public class HTTPUtils {

	// Configuration for requests
	private static final String USER_AGENT = "KittyBot/1.0";
	
	
	/*
	// Example requests!
	public static void main(String[] args) throws IOException 
	{
		// GET request example
		{
			Response res = SendGETRequest("http://rw0.pw/");
			if(res.isValid())
				System.out.println(res.getContent());
		}
		
		// POST request example, using wolfram alpha!
		{
			Response res = SendPOSTRequest("http://api.wolframalpha.com/v1/result", "appid=DEMO&i=How+far+is+Los+Angeles+from+New+York%3f&units=metric");
			if(res.isValid())
				System.out.println(res.getContent());				
		}
	}
	*/

	
	// Sends a GET request for the specified URL.
	public static Response SendGETRequest(String url)
	{
		try
		{
			// Get and open a connection, stored in connection object
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// Set ourselves as if we're "vaguely netscape"
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", USER_AGENT);
			
			// Handle response
			return (handleHttpResponse(con));
		}
		catch(IOException e)
		{
			return new Response();
		}
	}
	
	
	// Sends a POST request to the desired URL.
	// There are two forms of this function, one of which takes 
	public static Response SendPOSTRequest(String url) { return SendPOSTRequest(url, ""); }
	public static Response SendPOSTRequest(String url, String params) 
	{	
		try
		{
			// Get and open a connection, stored in connection object
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			
			// Set ourselves as if we're "vaguely netscape"
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);

			// For POST only - Configure parameters
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();
			os.write(params.getBytes());
			os.flush();
			os.close();

			// Handle response
			return (handleHttpResponse(con));
		}
		catch(IOException e)
		{
			return new Response();
		}
	}	
		
		
	// Inernal static function for parsing HTTP Responses based on the 
	// connection object provided by java. Assumes you only want to parse 
	// valid connections, others are discarded.
	private static Response handleHttpResponse(HttpURLConnection con) throws IOException
	{
		// Check response code and act on it accordingly.
		int responseCode = con.getResponseCode();

		// If we succeeded...
		if (responseCode == HttpURLConnection.HTTP_OK) 
		{ 
			// Variable declaration
			String inputLine;
			StringBuffer response = new StringBuffer();
			BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));

			// Read all lines from the response
			inputLine = in.readLine();
			while (inputLine != null) {
				response.append('\n');
				response.append(inputLine);
				inputLine = in.readLine();
			}

			// Close and return our data
			in.close();
			return new Response(response.toString());
		} else {
			System.out.println(con.getRequestMethod() + " responded with " + responseCode + " instead of 200.");
			return new Response(responseCode);
		}
	}
}


