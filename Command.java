package main.java.net.dv8tion;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class Command 
{
	RPManager manager = new RPManager();
	ArrayList <String> mute = new ArrayList <String>(); 
	boolean logging = false; 
	PointCounter points = new PointCounter();
	IgnoreList ignored = new IgnoreList();
	AuthList authed = new AuthList();
	
	public String comSent(Message message, Member member)
	{
		String line = message.getContentRaw();
		if(!isIgnore(member, message.getGuild().getId()))
		{
			points.addPoints(message.getAuthor().getId(), message.getGuild().getId(), 1);
		}
		if((line.charAt(0) == '_' && line.charAt(line.length()-1) == '_') 
				||(line.charAt(0) == '*' && line.charAt(line.length()-1) == '*'))
		{
			line = line.substring(1,line.length()-1);
		}
		manager.addLine(message.getChannel().getId(), message.getAuthor().getName() + " " + line);
		
		String [] command = message.getContentRaw().split(" ");
		
		if(!command[0].startsWith("!"))
		{
			return "";
		}
		
		//Start of Dev command
		if(command[0].equals("!save") && message.getAuthor().getName().equals("RinTheSnowMew"))
		{
			try {
				points.writeToFile();
				ignored.writeToFile();
				return "Saved successfully!";
				
			} catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
			}
		}
		
		//Start of Mod commands
		if(isAuth(member,message.getGuild().getId()) 
				|| message.getAuthor().getName().equals("RinTheSnowMew"))
		{
			//Mutes and unmutes bot
			if(command[0].equals("!mute"))
			{
				mute.add(message.getGuild().getId());
				return "Being quiet now.";
			}
			if(command[0].equals("!unmute"))
			{
				if(mute.contains(message.getGuild().getId()))
				{
					mute.remove(message.getGuild().getId());
				}
				return "I can talk again!";
			}
			
			//Adds points to user
			if(command[0].equalsIgnoreCase("!addpoints"))
			{
				points.addPoints(message.getMentionedUsers().get(0).getId(),
						message.getGuild().getId(), Integer.parseInt(command[2]));
				return "You gave some beans to " + message.getMentionedUsers().get(0).getAsMention() + "!";
			}
			
			//Removes points from user
			if(command[0].equalsIgnoreCase("!removepoints"))
			{
				points.subPoints(message.getMentionedUsers().get(0).getId(),
						message.getGuild().getId(), Integer.parseInt(command[2]));
				return "You took some beans from " + message.getMentionedUsers().get(0).getAsMention() + "!";
			}
			
			//Ignore & un-ignore separate users
			if(command[0].equalsIgnoreCase("!ignore"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				ignored.addName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Ignored " + message.getMentionedUsers().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("!unignore"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				ignored.removeName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Unignored " + message.getMentionedUsers().get(0).getName();
			}
			
			//Ignore & un-ignore separate roles
			if(command[0].equalsIgnoreCase("!ignorerole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				ignored.addRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Ignored " + message.getMentionedRoles().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("!unignorerole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				ignored.removeRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Unignored " + message.getMentionedRoles().get(0).getName();
			}
			
			//Authorize users
			if(command[0].equalsIgnoreCase("!auth"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				authed.addName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Authorized " + message.getMentionedUsers().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("!deauth"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				authed.removeName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Deauthorized " + message.getMentionedUsers().get(0).getName();
			}
			
			//Authorize and de-auth separate roles
			if(command[0].equalsIgnoreCase("!authrole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				authed.addRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Authorized " + message.getMentionedRoles().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("!deauthrole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				authed.removeRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Deauthorized " + message.getMentionedRoles().get(0).getName();
			}
			
			
		}
		
		//Start of non-mod commands
		if(mute.contains(message.getGuild().getId()) || isIgnore(member, message.getGuild().getId()))
		{
			return "";
		}
		//Help
		if(command[0].equalsIgnoreCase("!help"))
		{
			return "Hi! My name is KittyBot! I can do lots of things! "
					+ "\nIf you !boop I'll boop you right back!"
					+ "\nIf you !roll I'll need a dice like this 1d4 or 10d7"
					+ "\nTo see your current amount of beans just !points"
					+ "\nAnd if you wanna bet 100 beans for a chance for more you can always !bet"
					+ "\nIf you give me some stuff to !choose from just remember to put commas in between"
					+ "\nAnd if you want me to keep track of your RP you can !rpstart just don't forget to !rpend";
		} 
		
		if(command[0].equalsIgnoreCase("!info"))
		{
			return "Developed by Rin. \nRepository is https://github.com/omnicons/ye-olde-botte";
		}
		if(command[0].equals("!boop"))
		{
			return "*Boops " +message.getAuthor().getAsMention() + " right back!*";
		}
		
		//Method call and response 
		
		if(command[0].equalsIgnoreCase("!roll"))
		{
			return rollDice(command);
		}
		
		if(command[0].equalsIgnoreCase("!choose"))
		{
			return choose(message.getContentRaw());
		}
		
		if(command[0].equalsIgnoreCase("!points"))
		{
			if(message.getGuild().getName().equalsIgnoreCase("meeples peeples"))
			{
				return "You have " + points.getPoints(message.getAuthor().getId(), message.getGuild().getId()) + " :beens:!";
			}
			return "You have " + points.getPoints(message.getAuthor().getId(), message.getGuild().getId()) + " beans!";
		}
		
		if(command[0].equalsIgnoreCase("!bet"))
		{
			return points.betStart(message.getAuthor().getId(),message.getGuild().getId());
		}
		
		if(command[0].equalsIgnoreCase("!rpstart"))
		{
			manager.newRP(message.getChannel().getId());
			return "RP Started";
		}
		
		if(command[0].equalsIgnoreCase("!rpend"))
		{
			if(command.length == 1)
			{
				message.getChannel().sendFile(manager.endRP(message.getChannel().getId(), "RP")).queue();
				return "RP ended, there's your log!";
			}
			else
			{
				message.getChannel().sendFile(manager.endRP(message.getChannel().getId(), command[1])).queue();
				return "RP ended, there's your log!";
			}
		}
		
		return "I dun know how to respond so have a :fish:"; 
	}
	
	
	
	private boolean isAuth(Member person, String server)
	{
		if(authed.checkName(person.getUser().getId(),server) 
				|| authed.checkRole(person.getRoles(),server))
		{
			return true; 
		}
		return false; 
	}
	
	private boolean isIgnore(Member person, String server)
	{
		if(ignored.checkName(person.getUser().getId(),server) 
				|| ignored.checkRole(person.getRoles(),server))
		{
			return true; 
		}
		return false; 
	}
	
	public String choose(String line)
	{
		line = line.substring(7);
		String [] choices = line.split(",");
		if(choices.length == 1)
		{
			return "I can't choose from *one* thing!";
		}
		
		return "I chooooooose " + choices[(int) (Math.random()*choices.length)] + "!";
	}
	
	
	public String rollDice(String [] command)
	{
		int numofdice = 0;
		int dicesize = 0;
		String dicenum [] = command[1].split("d");
		if(!(isInteger(dicenum[0]) && isInteger(dicenum[1])))
		{
			
			return "Enter actual numbers please.";
		}
		numofdice = Integer.parseInt(dicenum[0]);
		dicesize = Integer.parseInt(dicenum[1]);
		int total = 0;
		int roll = 0;
		String nums = "";
		//checks for lower than 1 dice or dice size 
		if(numofdice < 1 || dicesize < 1 || numofdice > 100 || dicesize > 100)
		{
			return "*drowns in dice*";
		}
		for(int i = 0; numofdice > i; i ++)
		{
			roll = (int) (Math.random()*dicesize) + 1;
			nums += roll + " ";
			total += roll;
		}
		return "Rolls are: " + nums + "\n" + "Total is: " + total;
	}
	
	/*
	 * checks if String is Int if not returns false 
	 */
	public static boolean isInteger(String str) 
	{
	    if (str == "") 
	    {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') 
	    {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') 
	        {
	            return false;
	        }
	    }
	    return true;
	}
}
