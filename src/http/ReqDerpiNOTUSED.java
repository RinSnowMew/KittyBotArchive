package http;


import java.util.ArrayList;

import com.google.gson.Gson;

import main.java.net.dv8tion.Response;

public class ReqDerpiNOTUSED 
{
//	Response answer = new Response(); 
	private final String mainURL = "https://derpibooru.org/search.json?q=";
	private static final Gson jsonParser_ = new Gson();
	
	private class DerpiResponseObjectInner
	{
		// public variables matching the case and the type we want for JSON.
		// There are many more fields, but if we don't provide some it just
		// doesn't bother parsing them.
		public String image;
//		public String id;
	}
	
	private class DerpiResponseObjectOuter
	{
		ArrayList<DerpiResponseObjectInner> search = new ArrayList <DerpiResponseObjectInner>();
	}
	
	public Response getDerpi(String query) 
	{
		query = query.trim();
		query = query.replace(" ", "+");
		Response res = HTTPUtils.SendGETRequest(mainURL + query);
		if(res.isValid())
		{
			DerpiResponseObjectOuter obj = jsonParser_.fromJson(res.getContent(), 
					DerpiResponseObjectOuter.class);
			String output = "";
			
			output += "http:" + obj.search.get(0).image;
			res.setContent(output);
		}
		else
		{
			res.setContent(mainURL + query + "&random_image");
		}
		
		return res;
	}
}