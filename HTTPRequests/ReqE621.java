/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.dv8tion.HTTPRequests;
import com.google.gson.Gson;
import main.java.net.dv8tion.Response;

/**
 * This is the e621 request class, designed for form and parse requests that
 * use the e621 API, and is entirely static. 
 * 
 * If you ever find this class randomly not working,
 * it may be a good idea to make sure the user agent string is set in 
 * HTTPUtils to something other than a browser emulating string, or the default
 * java one.
 * 
 * @author Wisp
 */
public class ReqE621 
{   
	  ///////////////////////////////////////
	 // Internal JSON class and variables //
	//////////////////////////////////.////
	private final static Gson jsonParser_ = new Gson();
	private static int maxSearchResults_ = 1;
	private class E621ResponseObject
	{
		// public varaibles matching the case and the type we want for JSON.
		// There are many more fields, but if we don't provide some it just
		// doesn't bother parsing them.
		public int id;
		public String tags;
		public String file_url;
		public String source;
	}

	
	
	  ////////////////////
	 // Static methods //
	////////////////////
	// Requests a specific image, then returns a few.
	public static Response searchForResults(String input)
	{
		// Clean up request and replace problematic characters for the query string.
		input = input.trim();
		input = input.replace("+", "%2B");
		input = input.replace(" ", "%20");
		
		// Configure and send request
		Response res = HTTPUtils.SendPOSTRequest("https://e621.net/post/index.json"
			, "tags=" + input + "&limit=" + maxSearchResults_ + "&rating=safe");
		
		// Confirm the request is valid. Other cases checking for EC_E621 error codes
		// as defined in Response.java may be good.
		if(res.isValid())
		{
			// Use class evaluation on an array of the response object to be able to hold multiple.
			E621ResponseObject[] obj = jsonParser_.fromJson(res.getContent(), E621ResponseObject[].class);
			
			// For now, we really just wanna display images and their source. 
			// Append them all separately to a response string w/ some flavor text.
			String output = "I headed over to e621, and here's what I found!\n";
			for(int i = 0; i < obj.length; ++i)
				output += "• " + obj[i].file_url + " \n  source: <" + obj[i].source + ">\n";
			
			// Set the response information.
			res.setContent(output);
			return res;
		}
		
		//Print out any issues.
		System.out.println("There as an issue with the e621 response: " + res);
		return new Response("I had some trouble searching e621 for that ^^;");
	}
	
	// Safely sets the maximum number of searches to something erasonable
	public static Response SetMaxSearchResults(int num_results) 
	{
		Response res = new Response("I set the e621 image response limit to " + num_results + "!");
		
		if(num_results < 1)
			return new Response("Please enter a number bigger than 0!");
		else if(num_results > 10)
			return new Response("...That may be too many. How about something more reasonable, like 5?");
		
		// Set the result accordingly
		maxSearchResults_ = num_results; 
		return res;
	}
	
	// Returns the internal variable that limits the number of posts e621 will hand back
	public static int GetMaxSearchResults()
	{ 
		return maxSearchResults_; 
	}
}