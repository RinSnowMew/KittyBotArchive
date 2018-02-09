package main.java.net.dv8tion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.dv8tion.jda.core.entities.Role;

public class IgnoreList 
{
	HashMap<String, String> names = new HashMap<String, String>();
	HashMap<String, String> roles = new HashMap<String, String>();
	public IgnoreList()
	{
		String [] words;
		try 
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("IgnoredNames.txt")));
		    String line;
		    while ((line = br.readLine()) != null) 
		    {
		    	words = line.split(" ");
		    	names.put(words[0],words[1]);
		    }
		    br.close();
		    br = new BufferedReader(new InputStreamReader(new FileInputStream("IgnoredRoles.txt")));
		    while ((line = br.readLine()) != null) 
		    {
		    	words = line.split(" ");
		    	roles.put(words[0],words[1]);
		    }
		    br.close();
		} catch (FileNotFoundException e) 
		{
			System.out.println("Ignored names or roles file not found");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void addName(String name, String server)
	{
		names.put(name,server);
	}
	public void removeName(String name, String server)
	{
		if(names.containsKey(name))
		{
			names.remove(name, server);
		}	
	}
	
	public void addRole(String role, String server)
	{
		roles.put(role, server);
	}
	public void removeRole(String role, String server)
	{
		if(roles.containsKey(role))
		{
			roles.remove(role, server);
		}
	}
	
	public boolean checkName(String name, String server)
	{
		for(int i = 0; i < names.size(); i++)
		{
			if(names.containsKey(name) && server.equals(names.get(name)))
			{
				return true; 
			}
		}
		return false;
	}
	
	public boolean checkRole(List <Role> rolesIn, String server)
	{ 
		for(int i = 0; i < roles.size(); i++)
		{
			
			for(int j = 0; j <rolesIn.size(); j ++)
			{
				if(roles.containsKey(rolesIn.get(j).getId()) && server.equals(roles.get(rolesIn.get(j).getId())))
				{
					return true; 
				}
			}
		}
		return false;
	}
	
	public void writeToFile() throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("IgnoredNames.txt", "UTF-8");
		Set<?> set = names.entrySet(); 
		Iterator<?> iterator = set.iterator();
		while(iterator.hasNext()) 
		{
	         @SuppressWarnings("rawtypes")
			Map.Entry mentry = (Map.Entry)iterator.next();
	        writer.println(mentry.getKey() + " " + mentry.getValue());
	    }
		writer.close();
		
		writer = new PrintWriter("IgnoredRoles.txt", "UTF-8");
		Set<?> set2 = roles.entrySet(); 
		iterator = set2.iterator();
		while(iterator.hasNext()) 
		{
	         @SuppressWarnings("rawtypes")
			Map.Entry mentry = (Map.Entry)iterator.next();
	        writer.println(mentry.getKey() + " " + mentry.getValue());
	    }
		writer.close();
	}
}
