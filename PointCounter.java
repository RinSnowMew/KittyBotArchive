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
import java.util.Map;
import java.util.Set;

public class PointCounter 
{
	HashMap<String, Integer> pointmap = new HashMap<String, Integer>();
	public PointCounter() 
	{
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("KiraNums.txt")));
		    String line;
		    String [] person;
		    while ((line = br.readLine()) != null) 
		    {
		        person = line.split(" ");
		        pointmap.put(person[0], Integer.parseInt(person[1]));
		    }
		    br.close();
		} catch (FileNotFoundException e) 
		{
			System.out.println("Point file not found");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void addPoints(String user, String server,  int num)
	{
		if(pointmap.containsKey(server + user))
		{
			pointmap.put(server + user, pointmap.get(server + user)+num);
		}
		else
		{
			pointmap.put(server + user, num);
		}
	}
	
	public int getPoints(String user, String server)
	{
		if(pointmap.containsKey(server + user))
		{
			return pointmap.get(server + user);
		}
		return 0;
	}
	
	public String betStart(String user, String server)
	{
		String end = "";
		if(pointmap.containsKey(server + user))
		{
			if(pointmap.get(server + user)>100)
			{
				pointmap.put(server + user, pointmap.get(server + user)-100);
			}
			else
			{
				return "You no has 'nuff beans! D:";
			}
		}
		else
		{
			return "User not available.";
		}
		int [] nums = betting();
		if(nums [0] == nums [1] && nums[0] == nums[2])
		{
			if(nums[0] == 5)
			{
				end = "You win the jackpot!!! Plus 1500 beannnnsssss!"
						+ " \nYour slots are:";
				pointmap.put(server + user, pointmap.get(server + user)+1500);
			}
			else
			{
				if(nums [0] == 2)
				{
					end = "Peach Win! Plus 555 beans! \nYour slots are:";
					pointmap.put(server + user, pointmap.get(server + user)+555);
				}
				else
				{
					end = "You win! Plus 500 beans! \nYour slots are:";
					pointmap.put(server + user, pointmap.get(server + user)+500);
				}
			}
			
		}
		else
		{
			end = "You lost ;3; \nYour slots are:";
		}
		for(int i = 0; i < 3; i ++)
		{
			switch(nums[i])
			{
			case 1:
				end += ":potato:";
				break;
			case 2:
				end += ":peach:";
				break;
			case 3:
				end += ":avocado:";
				break;
			case 4:
				end += ":banana:";
				break;
			case 5:
				end += ":egg:";
				break;			
			}
		}
		return end + "\n You now have "+ pointmap.get(server + user) + " beans!"; 
	}
	
	private int [] betting()
	{
		int [] nums = new int [3];
		for(int i = 0; i < 3; i ++)
		{
			nums[i] = (int) (Math.random()*5) + 1;
		}
		return nums; 
	}
	
	//Mod Only
	public void subPoints(String user, String server, int num)
	{
		if(pointmap.containsKey(server + user))
		{
			pointmap.put(server + user, pointmap.get(server + user)-num);
		}
	}
	
	public void writeToFile() throws FileNotFoundException, UnsupportedEncodingException
	{
		PrintWriter writer = new PrintWriter("KiraNums.txt", "UTF-8");
		Set<?> set = pointmap.entrySet(); 
		Iterator<?> iterator = set.iterator();
		while(iterator.hasNext()) 
		{
	         @SuppressWarnings("rawtypes")
			Map.Entry mentry = (Map.Entry)iterator.next();
	        writer.println(mentry.getKey() + " " + mentry.getValue());
	    }
		writer.close();
		
	}
	
	
}
