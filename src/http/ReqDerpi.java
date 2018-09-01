package http;

import com.google.gson.Gson;

import Ref;
import main.java.net.dv8tion.Response;

public class ReqDerpi
{
//	Response answer = new Response(); 
	private final String mainURL = "https://derpibooru.org/search.json?q=";
	private static final Gson jsonParser_ = new Gson();
	
	private class DerpiResponseObject
    {
        public String image;
    }
    
    private class InitialRequest
    {
        public int id;
    }

    public Response getDerpi(String query) 
	{
    	query = query.trim();
		query = query.replace(" ", ",");
        Response res = HTTPUtils.SendGETRequest(mainURL + query + "&random_image=1&key=" + Ref.derpiKey);
        
        if(res.isValid())
        {
            // Use class evaluation on an array of the response object to be able to hold multiple.
            InitialRequest obj = jsonParser_.fromJson(res.getContent(), InitialRequest.class);
            String output = "";
            if(res.isValid())
            {
                Response res2 = HTTPUtils.SendGETRequest("https://derpibooru.org/" + obj.id + ".json");
                output += "<https://derpibooru.org/" + obj.id;
                DerpiResponseObject obj_for_real = jsonParser_.fromJson(res2.getContent(), DerpiResponseObject.class);
                
                res.setContent(output + ">\nhttp:" + obj_for_real.image);
            }
        }
        
		
		return res;
	}
}