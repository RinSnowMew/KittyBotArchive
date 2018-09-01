package main.java.net.dv8tion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class RPLogger 
{
	ArrayList<String> logs = new ArrayList<String>();
	String chan;
	public RPLogger()
	{
		
	}

	public void addLine(String line)
	{
		logs.add(line);
	}
	
	public File writeFile(String name) 
			throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter(name+".txt", "UTF-8");
		while(!logs.isEmpty())
		{
			 writer.println(logs.get(0));
			 logs.remove(0);
		}
		writer.close();
		return new File(name + ".txt"); 
	}
}
