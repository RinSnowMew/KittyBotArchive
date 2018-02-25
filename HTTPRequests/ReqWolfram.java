package main.java.net.dv8tion.HTTPRequests;

import main.java.net.dv8tion.Ref;
import main.java.net.dv8tion.Response;

public class ReqWolfram 
{	
	public ReqWolfram() { }
	
	public Response askWRA(String input) 
	{
		// Clean up request and replace problematic characters.
		input = input.trim();
		input = input.replace("+", "%2B");
		
		// Configure and send request
		Response answer = HTTPUtils.SendPOSTRequest("https://api.wolframalpha.com/v1/result","appid=" + Ref.wolfRamID + "&i=" + input);
		
		// Trim content and see if no answer was availables, or see if we got a
		// not-implemented error, which is what wolfram likes to hand back
		if(answer.getContent().trim().equalsIgnoreCase("No short answer available")
	    || answer.getErrorCode() == Response.EC_HTTP_501_NOT_IMPLEMENTED)
		{
			return new Response("Kitty can't find that ;3;");
		}
		
		// Return result!
		return answer;
	}
}
