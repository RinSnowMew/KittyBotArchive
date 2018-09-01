/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import main.java.net.dv8tion.Response;

/**
 *
 * @author Wisp
 */
public class ReqColiru 
{
	// Sends the string provided off to be compiled online.
	// Ofc
	public static Response compileMessageCPP(String msg)
	{
		// Escape characters that need escaping.
		// Primarily types of whitespace.
		msg = msg.replace("\\n", "\\\\n");
		msg = msg.replace("\\t", "\\\\t");
		msg = msg.replace("\n", "\\n");
		msg = msg.replace("\"", "\\\"");
		msg = msg.replace("\t", "\\t");
		
		// Send the compilation request!
		Response res = HTTPUtils.SendPOSTRequest("http://coliru.stacked-crooked.com/compile"
				, "{ \"cmd\": \"g++ main.cpp && ./a.out\", \"src\": \"" + msg + "\" }");
		
		// If we got a valid response...
		if(res.isValid())
		{
			// Format the response recieved!
			String out = res.getContent();
			
			// Clean up length if it's way too long
			final int reasonableLimit = 1000;
			if(out.length() > reasonableLimit)
			{
				// Limit the output
				out = out.substring(0, reasonableLimit);
				out += "\n\n...```\n...yea, I think that's enough output for now! ^^;";
			}
			else
			{
				// Non-extra output finish.
				out += "\n```";
			}
			
			// Format output
			out = "Here's what happened when I went to compiled that! ```\n" + out ;
			return new Response(out);
		}
		
		// Return what we got back.
		return res;
	}
}
