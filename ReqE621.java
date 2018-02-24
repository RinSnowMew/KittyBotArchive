/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.dv8tion;
import com.google.gson.Gson;

/**
 *
 * @author Wisp
 */
public class ReqE621 
{   
	public ReqE621() { } 
	
	// Requests a specific image, then returns a few.
	public Response searchForResults(String input)
	{
		// Clean up request and replace problematic characters.
		final int limit = 3;
		input = input.trim();
		input = input.replace("+", "%2B");
		input = input.replace(" ", "%20");
		
		
		// Configure and send request
		Response res = HTTPUtils.SendPOSTRequest("https://e621.net/post/index.json", "tags=" + input + "&limit=" + limit);
		if(res.isValid())
		{
			String out = res.getContent();
			// parse 'out' as JSON response! Note: Comes as array object.
		}
		
		// Return result!
		return res;
	}
}
