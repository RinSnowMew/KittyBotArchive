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
    // Internal variables
	ArrayList <String> mute = new ArrayList <>(); 
	ArrayList <String> listed = new ArrayList <>();
	HashMap <String, String> triggers = new HashMap<>();
	RPManager manager = new RPManager();
	PointCounter points = new PointCounter();
	IgnoreList ignored = new IgnoreList();
	AuthList authed = new AuthList();
	Blacklist words = new Blacklist();
	PollManager polls = new PollManager();
	ReqWolfram request = new ReqWolfram(); 
	
	
	// Called from the overridden function to encourage cleaner command 
	// parsing. Returns a string which is the message sent back to users.
	public String comSent(Message message, Member member, String cliid)
	{
		final String line = CleanLine(message.getContentRaw());
        final String msg_id = message.getGuild().getId();
        
		// Do any upkeep that should happen before we get to the commands.
		{
			Response res = ProcessPreCommand(message, member, cliid);
			if(res.isValid())
				return res.getContent();
		}

		// Manager upkeep
		manager.addLine(message.getChannel().getId(), message.getAuthor().getName() + " " + line);
		
		// Command cleanup and parsing
		String [] command = message.getContentRaw().split(" |\n|\t");
		
		// Confirm we have the trigger to run the command
		if(!(command[0].startsWith(triggers.get(msg_id))))
			return "";

		// Remove Trigger
		command[0] = command[0].substring(triggers.get(msg_id).length());
		
		//Start of Dev command
		if(message.getAuthor().getId().equals("147407528874082304"))
		{
			Response res = ProcessCommandsDev(message, member, cliid, command);
			if(res.isValid())
				return res.getContent();
		}
		
		// Confirm moderator status. If moderator, parse the command.
		// The two mods pre-approved are RinTheSnowMew and Reverie Wisp for
		// testing purposes.
		if(isAuth(member, msg_id) 
		|| message.getAuthor().getId().equals("147407528874082304")
		|| message.getAuthor().getId().equals("145720924325412865"))
		{
			Response res = ProcessCommandsMod(message, member, cliid, command);
			if(res.isValid())
				return res.getContent();
		}

		// Confirm not muted. 
		if(mute.contains(msg_id) || isIgnore(member, msg_id))
			return "";
                
		//Start of non-mod commands
		{
			Response res = ProcessCommandsNonMod(message, member, cliid, command);
			if(res.isValid())
				return res.getContent();
		}
                
		// If we're here, there was an issue.
		System.out.println("Problem with message: " + message.getContentRaw());
		return "";
	}
	
	
	
	
	// Cleans special characters off of lines for italics. Returns the cleaned
	// line. If nothing was removed, line stays as it was.
	String CleanLine(String line)
	{
		if((line.charAt(0) == '_' && line.charAt(line.length()-1) == '_') 
				||(line.charAt(0) == '*' && line.charAt(line.length()-1) == '*'))
		{
			line = line.substring(1,line.length()-1);
		}
		
		return line;
	}
	
	
	// Should be called before the command is processed otherwise.
	// Note that there is no command array argument for this function.
	Response ProcessPreCommand(Message message, Member member, String cliid)
	{
		final String line = CleanLine(message.getContentRaw());
        final String msg_id = message.getGuild().getId();
              
		// Language filter
		if(words.check(line) && listed.contains(msg_id))
		{
			message.delete().queue();
			return new Response("Message deleted for inappropiate language.");
		}
		
		// If not ignored, we should add to the bean tracker!
		if(!isIgnore(member, msg_id))
		{
			points.addPoints(message.getAuthor().getId(), msg_id, 1);
		}
		
		// Default return
		return new Response();
	}
	
	
	// Developer only commands. Used mostly for testing.
	Response ProcessCommandsDev(Message message, Member member, String cliid, String[] command)
	{
//		final String line = CleanLine(message.getContentRaw());
//      final String msg_id = message.getGuild().getId();
		
		if(command[0].equals("save"))
		{
			try {
				points.writeToFile();
				ignored.writeToFile();
				authed.writeToFile();
				return new Response("Saved successfully!");

			} 
			catch (FileNotFoundException e) { } 
			catch (UnsupportedEncodingException e) { }
		}
			
		if(command[0].equalsIgnoreCase("invitelink"))
		{
			return new Response("https://discordapp.com/oauth2/authorize?&client_id=" + cliid + "&scope=bot&permissions=0");
		}
		
		// Default to no response
		return new Response();
	}
	
    Response ProcessCommandsNonMod(Message message, Member member, String cliid, String[] command)
    {
        final String line = CleanLine(message.getContentRaw());
        final String msg_id = message.getGuild().getId();
        

        // Help output
		//@TODO: Move to external file.
        if(command[0].equalsIgnoreCase("help"))
        {
			return new Response("Hi! My name is KittyBot! I can do lots of things! "
							  + "\nIf you " + triggers.get(msg_id) + "boop I'll boop you right back!"
							  + "\nIf you " + triggers.get(msg_id) + "roll I'll need a dice like this 1d4 or 10d7"
							  + "\nTo see your current amount of beans just " + triggers.get(msg_id) + "points"
							  + "\nAnd if you wanna bet 100 beans for a chance for more you can always " + triggers.get(msg_id) + "bet"
							  + "\nIf you give me some stuff to " + triggers.get(msg_id) + "choose from just remember to put commas in between"
							  + "\nAnd if you want me to keep track of your RP you can " + triggers.get(msg_id) + "rpstart just don't forget to " + triggers.get(message.getGuild().getId()) + "rpend"
			);
        } 

		// Info output
		//@TODO: Move to external file.
        if(command[0].equalsIgnoreCase("info"))
        {
			return new Response("Developed by Rin. \nRepository is <https://github.com/RinSnowMew/KittyBot> "
							+ "\nIcon by the very talented Meep\nhttp://www.furaffinity.net/view/25966081/"
							+ "\nhttp://d.facdn.net/art/meep/1515216535/1515216535.meep_kittybot.png");
        }
        if(command[0].equalsIgnoreCase("boop"))
        {
			if(message.getMentionedUsers().isEmpty())
			{
				return new Response("*Boops " +message.getAuthor().getAsMention() + " right back!*");
			}
			return new Response("*Boops " + message.getMentionedUsers().get(0).getAsMention() + " everywhere!*");
        }

        //Method call and response 
        if(command[0].equalsIgnoreCase("roll"))
        {
			return new Response(rollDice(command));
        }

        if(command[0].equalsIgnoreCase("choose"))
        {
			return new Response(choose(message.getContentRaw()));
        }

        if(command[0].equalsIgnoreCase("points"))
        {
			if(message.getGuild().getName().equalsIgnoreCase("meeples peeples"))
			{
				if(message.getMentionedMembers().isEmpty())
				{
					return new Response("You have " + points.getPoints(message.getAuthor().getId(), msg_id) + " <:beens:396127956428390401>!");
				}
				
				return new Response(message.getMentionedMembers().get(0).getEffectiveName() + " has " + points.getPoints(message.getMentionedMembers().get(0).getUser().getId(), message.getGuild().getId()) + " <:beens:396127956428390401>!");
			}
			if(message.getMentionedMembers().isEmpty())
			{
				return new Response("You have " + points.getPoints(message.getAuthor().getId(), msg_id) + " points!");
			}
			
			return new Response(message.getMentionedMembers().get(0).getEffectiveName() + " has " + points.getPoints(message.getMentionedMembers().get(0).getUser().getId(), message.getGuild().getId()) + " points!");
        }

        if(command[0].equalsIgnoreCase("bet"))
        {
			return new Response(points.betStart(message.getAuthor().getId(),msg_id));
        }

        if(command[0].equalsIgnoreCase("rpstart"))
        {
			manager.newRP(message.getChannel().getId());
			return new Response("RP Started");
        }

        if(command[0].equalsIgnoreCase("rpend"))
        {
			if(command.length == 1)
			{
				message.getChannel().sendFile(manager.endRP(message.getChannel().getId(), "RP")).queue();
				return new Response("RP ended, there's your log!");
			}
			else
			{
				message.getChannel().sendFile(manager.endRP(message.getChannel().getId(), command[1])).queue();
				return new Response("RP ended, there's your log!");
			}
        }

        if(command[0].equalsIgnoreCase("vote"))
        {
			if(isInteger(command[1]))
			{
				return new Response(polls.newVote(msg_id, message.getAuthor().getId(), Integer.parseInt(command[1])));
			}

			return new Response("Please enter a real number choice"); 
        }

        if(command[0].equalsIgnoreCase("getPoll"))
        {
			return new Response(polls.getQuestion(msg_id));
        }

        if(command[0].equalsIgnoreCase("getResults"))
        {
			return new Response(polls.getResults(msg_id));
        }

        if(command[0].equalsIgnoreCase("wolfram"))
        {
        	return (request.askWRA(line.substring(triggers.get(msg_id).length() + 7)));
		}
		
		// Compile a single c++ file
		final String compileCommandCpp1 = "c++";
		final String compileCommandCpp2 = "g++";
		if(command[0].equalsIgnoreCase(compileCommandCpp1) || command[0].equalsIgnoreCase(compileCommandCpp2))
		{
			// Clean input and strip grave character
			String input = message.getContentRaw();
			input = input.substring(triggers.get(msg_id).length() + compileCommandCpp1.length());
			input = input.replace("`", " ");
			input = input.trim();
			
			// Delete syntax highlighting characters
			if(input.charAt(0) == 'c' || input.charAt(0) == 'C')
				input = input.substring(1);
			
			// Issue request
			return ReqColiru.compileMessageCPP(input);
		}
		
		if(command[0].equalsIgnoreCase("e621"))
			return ReqE621.searchForResults(command[1].trim());
		
		// No response.
		return new Response();
    }
    
    
    // These commands can only be called by a moderator.
    Response ProcessCommandsMod(Message message, Member member, String cliid, String[] command)
    {
        //final String line = CleanLine(message.getContentRaw());
        final String msg_id = message.getGuild().getId();


		//Opts in and out of Blacklist
		if(command[0].equalsIgnoreCase("stopList"))
		{
				listed.remove(msg_id);
				return new Response("Blacklist disabled"); 
		}
		if(command[0].equalsIgnoreCase("startList"))
		{
				listed.add(msg_id);
				return new Response("Blacklist enabled");
		}


		//Mutes and unmutes bot
		if(command[0].equalsIgnoreCase("mute"))
		{
				mute.add(msg_id);
				return new Response("Being quiet now.");
		}
		if(command[0].equalsIgnoreCase("unmute"))
		{
				if(mute.contains(msg_id))
				{
						mute.remove(msg_id);
				}
				return new Response("I can talk again!");
		}

		//Adds points to user
		if(command[0].equalsIgnoreCase("addpoints"))
		{
				points.addPoints(message.getMentionedUsers().get(0).getId(),
								msg_id, Integer.parseInt(command[2]));
				return new Response("You gave some beans to " + message.getMentionedUsers().get(0).getAsMention() + "!");
		}

		//Removes points from user
		if(command[0].equalsIgnoreCase("removepoints"))
		{
				points.subPoints(message.getMentionedUsers().get(0).getId(),
								msg_id, Integer.parseInt(command[2]));
				return new Response("You took some beans from " + message.getMentionedUsers().get(0).getAsMention() + "!");
		}

		//Ignore & un-ignore separate users
		if(command[0].equalsIgnoreCase("ignore"))
		{
				if(message.getMentionedUsers().isEmpty())
				{
						return new Response("No user provided.");
				}
				ignored.addName(message.getMentionedUsers().get(0).getId(), msg_id); 
				return new Response("Ignored " + message.getMentionedUsers().get(0).getName());
		}
		if(command[0].equalsIgnoreCase("unignore"))
		{
				if(message.getMentionedUsers().isEmpty())
				{
						return new Response("No user provided.");
				}
				ignored.removeName(message.getMentionedUsers().get(0).getId(), msg_id); 
				return new Response("Unignored " + message.getMentionedUsers().get(0).getName());
		}

		//Ignore & un-ignore separate roles
		if(command[0].equalsIgnoreCase("ignorerole"))
		{
				if(message.getMentionedRoles().isEmpty())
				{
						return new Response("No role provided.");
				}
				ignored.addRole(message.getMentionedRoles().get(0).getId(), msg_id); 
				return new Response("Ignored " + message.getMentionedRoles().get(0).getName());
		}
		if(command[0].equalsIgnoreCase("unignorerole"))
		{
				if(message.getMentionedRoles().isEmpty())
				{
						return new Response("No role provided.");
				}
				ignored.removeRole(message.getMentionedRoles().get(0).getId(), msg_id); 
				return new Response("Unignored " + message.getMentionedRoles().get(0).getName());
		}

		//Authorize users
		if(command[0].equalsIgnoreCase("auth"))
		{
				if(message.getMentionedUsers().isEmpty())
				{
						return new Response("No user provided.");
				}
				authed.addName(message.getMentionedUsers().get(0).getId(), msg_id); 
				return new Response("Authorized " + message.getMentionedUsers().get(0).getName());
		}
		if(command[0].equalsIgnoreCase("deauth"))
		{
				if(message.getMentionedUsers().isEmpty())
				{
						return new Response("No user provided.");
				}
				authed.removeName(message.getMentionedUsers().get(0).getId(), msg_id); 
				return new Response("Deauthorized " + message.getMentionedUsers().get(0).getName());
		}

		//Authorize and de-auth separate roles
		if(command[0].equalsIgnoreCase("authrole"))
		{
				if(message.getMentionedRoles().isEmpty())
				{
						return new Response("No role provided.");
				}
				authed.addRole(message.getMentionedRoles().get(0).getId(), msg_id); 
				return new Response("Authorized " + message.getMentionedRoles().get(0).getName());
		}
		if(command[0].equalsIgnoreCase("deauthrole"))
		{
				if(message.getMentionedRoles().isEmpty())
				{
						return new Response("No role provided.");
				}
				authed.removeRole(message.getMentionedRoles().get(0).getId(), msg_id); 
				return new Response("Deauthorized " + message.getMentionedRoles().get(0).getName());
		}

		if(command[0].equalsIgnoreCase("changeTrigger"))
		{
				if(command.length < 2)
				{
						return new Response("No trigger given.");
				}

				changeTrigger(msg_id, command[1]);
				return new Response("Trigger changed to: " + command[1]);
		}

		//Polling start and end 
		if(command[0].equalsIgnoreCase("startPoll"))
		{
				String poll_content = message.getContentRaw().substring(11);
				polls.newPoll(msg_id, poll_content);
				return new Response("New question added! Don't forget to add choices!");
		}

		if(command[0].equalsIgnoreCase("endPoll"))
		{
				return new Response(polls.endPoll(msg_id));
		}

		if(command[0].equalsIgnoreCase("addchoice"))
		{
				return new Response(polls.addChoice(msg_id, message.getContentRaw().substring(11)));
		}

        return new Response();
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
