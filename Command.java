package main.java.net.dv8tion;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;

public class Command 
{
	ArrayList <String> mute = new ArrayList <String>(); 
	ArrayList <String> listed = new ArrayList <String>();
	HashMap <String, String> triggers = new HashMap<String, String>();
	RPManager manager = new RPManager();
	PointCounter points = new PointCounter();
	IgnoreList ignored = new IgnoreList();
	AuthList authed = new AuthList();
	Blacklist words = new Blacklist();
	PollManager polls = new PollManager();
	
	public String comSent(Message message, Member member, String cliid)
	{
		String line = message.getContentRaw();
		if(words.check(line) && listed.contains(message.getGuild().getId()))
		{
			message.delete().queue();
			return "Message deleted for inappropiate language.";
		}
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
		
		if(!(command[0].startsWith(triggers.get(message.getGuild().getId()))))
		{
			return "";
		}
		command[0] = command[0].substring(triggers.get(message.getGuild().getId()).length());
		
		
		//Start of Dev command
		if(message.getAuthor().getName().equals("RinTheSnowMew"))
		{
			if(command[0].equals("save"))
			{
				try {
					points.writeToFile();
					ignored.writeToFile();
					authed.writeToFile();
					return "Saved successfully!";
					
				} catch (FileNotFoundException e) 
				{
				} catch (UnsupportedEncodingException e) 
				{
				}
			}
			
			if(command[0].equalsIgnoreCase("invitelink"))
			{
				return "https://discordapp.com/oauth2/authorize?&client_id=" + cliid + "&scope=bot&permissions=0";
			}
		}
		
		
		//Start of Mod commands
		if(isAuth(member,message.getGuild().getId()) 
				|| message.getAuthor().getName().equals("RinTheSnowMew"))
		{
			//Opts in and out of Blacklist
			if(command[0].equalsIgnoreCase("stopList"))
			{
				listed.remove(message.getGuild().getId());
				return "Blacklist disabled"; 
			}
			
			if(command[0].equalsIgnoreCase("startList"))
			{
				listed.add(message.getGuild().getId());
				return "Blacklist enabled";
			}
			
			
			//Mutes and unmutes bot
			if(command[0].equalsIgnoreCase("mute"))
			{
				mute.add(message.getGuild().getId());
				return "Being quiet now.";
			}
			if(command[0].equalsIgnoreCase("unmute"))
			{
				if(mute.contains(message.getGuild().getId()))
				{
					mute.remove(message.getGuild().getId());
				}
				return "I can talk again!";
			}
			
			//Adds points to user
			if(command[0].equalsIgnoreCase("addpoints"))
			{
				points.addPoints(message.getMentionedUsers().get(0).getId(),
						message.getGuild().getId(), Integer.parseInt(command[2]));
				return "You gave some beans to " + message.getMentionedUsers().get(0).getAsMention() + "!";
			}
			
			//Removes points from user
			if(command[0].equalsIgnoreCase("removepoints"))
			{
				points.subPoints(message.getMentionedUsers().get(0).getId(),
						message.getGuild().getId(), Integer.parseInt(command[2]));
				return "You took some beans from " + message.getMentionedUsers().get(0).getAsMention() + "!";
			}
			
			//Ignore & un-ignore separate users
			if(command[0].equalsIgnoreCase("ignore"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				ignored.addName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Ignored " + message.getMentionedUsers().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("unignore"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				ignored.removeName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Unignored " + message.getMentionedUsers().get(0).getName();
			}
			
			//Ignore & un-ignore separate roles
			if(command[0].equalsIgnoreCase("ignorerole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				ignored.addRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Ignored " + message.getMentionedRoles().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("unignorerole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				ignored.removeRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Unignored " + message.getMentionedRoles().get(0).getName();
			}
			
			//Authorize users
			if(command[0].equalsIgnoreCase("auth"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				authed.addName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Authorized " + message.getMentionedUsers().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("deauth"))
			{
				if(message.getMentionedUsers().isEmpty())
				{
					return "No user provided.";
				}
				authed.removeName(message.getMentionedUsers().get(0).getId(), message.getGuild().getId()); 
				return "Deauthorized " + message.getMentionedUsers().get(0).getName();
			}
			
			//Authorize and de-auth separate roles
			if(command[0].equalsIgnoreCase("authrole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				authed.addRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Authorized " + message.getMentionedRoles().get(0).getName();
			}
			if(command[0].equalsIgnoreCase("deauthrole"))
			{
				if(message.getMentionedRoles().isEmpty())
				{
					return "No role provided.";
				}
				authed.removeRole(message.getMentionedRoles().get(0).getId(), message.getGuild().getId()); 
				return "Deauthorized " + message.getMentionedRoles().get(0).getName();
			}
			
			if(command[0].equalsIgnoreCase("changeTrigger"))
			{
				if(command.length < 2)
				{
					return "No trigger given.";
				}
				
				changeTrigger(message.getGuild().getId(), command[1]);
				return "Trigger changed to: " + command[1];
			}
			
			//Polling start and end 
			if(command[0].equalsIgnoreCase("startPoll"))
			{
				line = message.getContentRaw().substring(11);
				polls.newPoll(message.getGuild().getId(), line);
				return "New question added! Don't forget to add choices!";
			}
			
			if(command[0].equalsIgnoreCase("endPoll"))
			{
				return polls.endPoll(message.getGuild().getId());
			}
			
			if(command[0].equalsIgnoreCase("addchoice"))
			{
				return polls.addChoice(message.getGuild().getId(), message.getContentRaw().substring(11));
			}
		}
		
		//Start of non-mod commands
		if(mute.contains(message.getGuild().getId()) || isIgnore(member, message.getGuild().getId()))
		{
			return "";
		}
		//Help
		if(command[0].equalsIgnoreCase("help"))
		{
			return "Hi! My name is KittyBot! I can do lots of things! "
					+ "\nIf you !boop I'll boop you right back!"
					+ "\nIf you !roll I'll need a dice like this 1d4 or 10d7"
					+ "\nTo see your current amount of beans just !points"
					+ "\nAnd if you wanna bet 100 beans for a chance for more you can always !bet"
					+ "\nIf you give me some stuff to !choose from just remember to put commas in between"
					+ "\nAnd if you want me to keep track of your RP you can !rpstart just don't forget to !rpend";
		} 
		
		if(command[0].equalsIgnoreCase("info"))
		{
			return "Developed by Rin. \nRepository is <https://github.com/RinSnowMew/KittyBot> "
					+ "\nIcon by the very talented Meep\nhttp://www.furaffinity.net/view/25966081/"
					+ "\nhttp://d.facdn.net/art/meep/1515216535/1515216535.meep_kittybot.png";
		}
		if(command[0].equalsIgnoreCase("boop"))
		{
			if(message.getMentionedUsers().isEmpty())
			{
				return "*Boops " +message.getAuthor().getAsMention() + " right back!*";
			}
			return "*Boops " + message.getMentionedUsers().get(0).getAsMention() + " everywhere!*";
		}
		
		//Method call and response 
		
		if(command[0].equalsIgnoreCase("roll"))
		{
			return rollDice(command);
		}
		
		if(command[0].equalsIgnoreCase("choose"))
		{
			return choose(message.getContentRaw());
		}
		
		if(command[0].equalsIgnoreCase("points"))
		{
			if(message.getGuild().getName().equalsIgnoreCase("meeples peeples"))
			{
				return "You have " + points.getPoints(message.getAuthor().getId(), message.getGuild().getId()) + " <:beens:396127956428390401>!";
			}
			return "You have " + points.getPoints(message.getAuthor().getId(), message.getGuild().getId()) + " beans!";
		}
		
		if(command[0].equalsIgnoreCase("bet"))
		{
			return points.betStart(message.getAuthor().getId(),message.getGuild().getId());
		}
		
		if(command[0].equalsIgnoreCase("rpstart"))
		{
			manager.newRP(message.getChannel().getId());
			return "RP Started";
		}
		
		if(command[0].equalsIgnoreCase("rpend"))
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
		
		if(command[0].equalsIgnoreCase("vote"))
		{
			if(isInteger(command[1]))
			{
				return polls.newVote(message.getGuild().getId(), message.getAuthor().getId(), Integer.parseInt(command[1]));
			}
			return "Please enter a real number choice"; 
		}
		
		if(command[0].equalsIgnoreCase("getPoll"))
		{
			return polls.getQuestion(message.getGuild().getId());
		}
		
		if(command[0].equalsIgnoreCase("getResults"))
		{
			return polls.getResults(message.getGuild().getId());
		}
		
		System.out.println("Problem with message: " + message.getContentRaw());
		
		return "";
//		return "I dun know how to respond so have a :fish:"; 
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
	
	private String choose(String line)
	{
		line = line.substring(7);
		String [] choices = line.split(",");
		if(choices.length == 1)
		{
			return "I can't choose from *one* thing!";
		}
		
		return "I chooooooose " + choices[(int) (Math.random()*choices.length)] + "!";
	}
	
	
	private String rollDice(String [] command)
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
	private static boolean isInteger(String str) 
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
	
	public void makeTriggers(List <Guild> servers)
	{
		for(int i = 0; i < servers.size(); i++)
		{
			triggers.put(servers.get(i).getId(), "!");
		}
	}
	
	private void changeTrigger(String ID, String trig)
	{
		triggers.put(ID, trig);
	}
}
