package main.java.net.dv8tion;

public class WolfRamReq 
{
	Response answer = new Response();
	
	public WolfRamReq()
	{
		
	}
	
	public Response askWRA(String input) 
	{
		input = input.trim();
		input = input.replace("+", "%2B");
		System.out.println("appid=" +Ref.wolfRamID + "&i=" + input);
		answer = HTTPUtils.SendPOSTRequest("http://api.wolframalpha.com/v1/result", "appid=" +Ref.wolfRamID + "&i=" + input);
		System.out.println(answer.getContent()
				);
		
		if(answer.getContent().equalsIgnoreCase("No short answer available"))
		{
			return new Response("Kitty can't find that ;3;");
		}
		return answer;
	}
}
