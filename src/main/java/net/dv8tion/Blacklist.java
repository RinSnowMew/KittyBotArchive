package main.java.net.dv8tion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Blacklist 
{
	ArrayList <String> words = new ArrayList<String>();
	public Blacklist()
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("wordblacklist.txt")));
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		    	words.add(line);
		    }
		    br.close();
		} catch (FileNotFoundException e) 
		{
			System.out.println("Blacklist file not found");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean check(String line)
	{
		for(int i = 0; i < words.size(); i++)
		{
			if(line.contains(words.get(i)))
			{
				return true;
			}
		}
		return false;
	}
}
