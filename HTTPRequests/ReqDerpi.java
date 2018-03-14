package main.java.net.dv8tion.HTTPRequests;

import main.java.net.dv8tion.HTTPRequests.HTTPUtils;
import com.google.gson.Gson;
import main.java.net.dv8tion.Response;

public class ReqDerpi 
{
//	Response answer = new Response(); 
	private final String mainURL = "https://derpibooru.org/search.json?q=";
	Gson gson = new Gson();
	
	public Response getDerpi(String query) 
	{
		query = query.replace(" ", "+");
		DerpiAPI queryResults = gson.fromJson(HTTPUtils.SendPOSTRequest(mainURL + query).getContent(), DerpiAPI.class);
		
		return new Response(queryResults.getSource_url());
	}
}
