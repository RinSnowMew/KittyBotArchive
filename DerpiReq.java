package main.java.net.dv8tion;

import main.java.net.dv8tion.HTTPRequests.HTTPUtils;
import com.google.gson.Gson;

public class DerpiReq 
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
