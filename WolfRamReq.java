package main.java.net.dv8tion;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public class WolfRamReq 
{
	WAEngine engine = new WAEngine();
	WAQuery query = engine.createQuery();
	WAQueryResult result;
	
	public WolfRamReq()
	{
		engine.setAppID(Ref.wolfRamID);
		engine.addFormat("plaintext");
	}
	
	public String askWRA(String input) throws WAException
	{
		query.setInput(input);
		result = engine.performQuery(query);
		String answer = "```"; 
		
		if(result.isError())
		{
			return "Ahh I can't find that! Wolfram said " + result.getErrorMessage(); 
		}
		else 
		{
			if(!result.isSuccess())
			{
				return "Wolfram didn't understand that ;3;";
			}
		}
		
		for(WAPod pod : result.getPods())
		{
			if(!pod.isError())
			{
				for(WASubpod subpod : pod.getSubpods())
				{
					for(Object element : subpod.getContents())
					{
						if(element instanceof WAPlainText && answer.length() < 50)
						{
							answer += pod.getTitle() + "\n";
							answer += ((WAPlainText)element).getText() + "\n";
						}
					}
				}
			}
		}
		
		return answer+"```"; 
	}
}
