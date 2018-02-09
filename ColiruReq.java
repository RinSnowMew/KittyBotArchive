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
		Response res = HTTPUtils.SendPOSTRequest("http://coliru.stacked-crooked.com/compile"
				, "{ \"cmd\": \"g++ main.cpp && ./a.out\", \"src\": \"" + msg + "\" }");
		
		return res;
	}
}
