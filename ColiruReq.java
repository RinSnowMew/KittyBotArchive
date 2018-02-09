/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.net.dv8tion;

/**
 *
 * @author Wisp
 */
public class ColiruReq 
{
	// Sends the string provided off to be compiled online.
	// Ofc
	public static Response compileMessageCPP(String msg)
	{
		// Escape characters that need escaping
		msg = msg.replace("\\n", "\\\n");
		msg = msg.replace("\n", "\\n");
		msg = msg.replace("\"", "\\\"");
		msg = msg.replace("\t", " ");
		
		// Send the compilation request!
		Response res = HTTPUtils.SendPOSTRequest("http://coliru.stacked-crooked.com/compile"
				, "{ \"cmd\": \"g++ main.cpp && ./a.out\", \"src\": \"" + msg + "\" }");
		
		System.out.println(res.getContent());
		
		return res;
	}
}
