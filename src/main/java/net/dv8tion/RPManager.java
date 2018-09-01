package main.java.net.dv8tion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class RPManager 
{
	HashMap<String, RPLogger> logs = new HashMap<String, RPLogger>();
	public RPManager()
	{
	}
	
	public void newRP(String channel)
	{
		logs.put(channel, new RPLogger());
	}
	
	public void addLine(String channel, String line)
	{
		if(logs.containsKey(channel))
		{
			logs.get(channel).addLine(line);
		}
	}
	
	public File endRP(String channel, String name)
	{
		try {
			return logs.get(channel).writeFile(name);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
