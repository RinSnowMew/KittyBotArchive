package main.java.net.dv8tion;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import http.ReqColiru;
import http.ReqDerpi;
import http.ReqE621;
import http.ReqJDoodle;
import http.ReqWolfram;
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
	ReqDerpi derpsearch = new ReqDerpi();
	ReqJDoodle doodleSearch = new ReqJDoodle();
	
	
	// Called from the overridden function to encourage cleaner command 
	// parsing. Returns a string which is the message sent back to users.
	public String comSent(Message message, Member member, String cliid)
	{		
		final String line = CleanLine(message.getContentRaw());
		final String msg_id = message.getGuild().getId();
		
		// Language filter
		 if(words.check(line) && listed.contains(msg_id))
		 {
			message.delete().queue();
			
			return "Message deleted for inappropiate language.";
		 }
		
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
		final String msg_id = message.getGuild().getId();
		
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
		//final String line = CleanLine(message.getContentRaw());
		//final String msg_id = message.getGuild().getId();
		
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
			final String helpString = "**Hi! My name is KittyBot! I can do lots of things!**\n"
			+ "`help` - Shows this help page!\n"
			+ "`info` - Kittybot developer info!\n"
			+ "`givefishy` - Ping command - gives kittybot a fishy.\n"
			+ "`praise` - Ping command - kitty praises beans.\n"
			+ "`boop <@user 1 ... @user n>` - Ping command - Also boops all `@users` specified in the command line.\n"
			+ "`roll <number of dice>d<dice sides>` - Rolls n s-sided dice in `n`d`s` format. Limit of 100 dice, and 100 sides.\n"
			+ "`choose <option 1, ..., option n>` - Kittybot will randomly choose an entry in a comma-separated list of choices.\n"
			+ "`points` - Tells the person issuing the command how many points they have.\n"
			+ "`bet` - Try your luck with slots based betting to get more points! Each play costs 100 points.\n"
			+ "`highlow` - Try your luck guessing a number of points! Each play costs 100 points.\n"
			+ "`rpstart` - Begins logging messages in the channel the command was issued in.\n"
			+ "`rpend <optional name>` - Ends logging messages in the specified channel, and hands back the rp log file in `RP.txt`, or `<optional name>.txt`.\n"
			+ "`vote <number>` - Vote on a specified option number in the currently active poll.\n"
			+ "`getPoll` - Gets the current poll and displays the options.\n"
			+ "`getResults` - Gets the current results of the current poll, but doesn't end the poll.\n"
			+ "`wolfram <query>` - Asks wolfram about the specified query, and posts the short response.\n"
			+ "`c++ <code>` - Compiles specified C/C++ code with gcc (Coliru) compiler. Highlighting and code snippet support are ignored.\n"
			+ "`java <code>` - Compiles specified Java with java 1.8+ (JDoodle) compiler. Highlighting and code snippet characters may not be ignored.\n"
			+ "`e621 <query>` - Searches E621 using the provided query. Provides, by default, `1` random image result.\n"
			+ "`derp <query>` - Searches derpibooru using the provided query. Provides `1` random image result.";

			return new Response(helpString);
			// return new Response("Hi! My name is KittyBot! I can do lots of things! "
			// 				  + "\nIf you " + triggers.get(msg_id) + "boop I'll boop you right back!"
			// 				  + "\nIf you " + triggers.get(msg_id) + "roll I'll need a dice like this 1d4 or 10d7"
			// 				  + "\nTo see your current amount of beans just " + triggers.get(msg_id) + "points"
			// 				  + "\nAnd if you wanna bet 100 beans for a chance for more you can always " + triggers.get(msg_id) + "bet"
			// 				  + "\nIf you give me some stuff to " + triggers.get(msg_id) + "choose from just remember to put commas in between"
			// 				  + "\nAnd if you want me to keep track of your RP you can " + triggers.get(msg_id) + "rpstart just don't forget to " + triggers.get(message.getGuild().getId()) + "rpend"
			// );
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
		
		if(command[0].equalsIgnoreCase("highlow")) 
		{
						return new Response(points.highlow(message.getAuthor().getId(), msg_id, command)); 
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
		
		final String searchE621Command = "e621";
		if(command[0].equalsIgnoreCase(searchE621Command))
		{
			String input = message.getContentRaw();
			input = input.substring(triggers.get(msg_id).length() + searchE621Command.length());
			return ReqE621.searchForResults(input);
		}
		
		final String searchJdoodleCommand = "jdoodle";
		if(command[0].equalsIgnoreCase(searchJdoodleCommand))
		{
			String input = message.getContentRaw();
			input = input.substring(triggers.get(msg_id).length() + searchJdoodleCommand.length());
			return doodleSearch.getJDoodle(input);
		}
		
		if(command[0].equalsIgnoreCase("givefishy"))
		{
			return new Response("Thanks, " + message.getAuthor().getAsMention() + "!\n*noms on da fishy!* ^~^");
		}
		
		if(command[0].equalsIgnoreCase("praise"))
		{
			return new Response("PRAISE THE BEANS!");
		}
		
		final String searchDerpiCommand = "derp";
		if(command[0].equalsIgnoreCase("derp"))
		{
			String input = message.getContentRaw();
			input = input.substring(triggers.get(msg_id).length() + searchDerpiCommand.length());
			return derpsearch.getDerpi(input);
		}
		
		// No response.
		return new Response();
	}
	
	
	// These commands can only be called by a moderator.
	Response ProcessCommandsMod(Message message, Member member, String cliid, String[] command)
	{
		//final String line = CleanLine(message.getContentRaw());
		final String msg_id = message.getGuild().getId();

		if(command[0].equalsIgnoreCase("modHelp"))
		{
			final String modHelpString = "**Only authorized users can ask Kittybot to do the following:**\n"
			+ "`modHelp` - Shows this help page!\n"
			+ "`stopList` - Disables the blacklist feature.\n"
			+ "`startList` - Enables the blacklist feature which trims messages containing inappropriate words.\n"
			+ "`mute` - Mutes kittybot.\n"
			+ "`unmute` - Unmutes kittybot.\n"
			+ "`addpoints <@user> <number>` - Adds points.\n"
			+ "`removepoints <@user> <number>` - Removes points from the specified user.\n"
			+ "`ignore <@user>` - Kittybot will ignore commands from a specified user (Overrides role ignores).\n"
			+ "`unignore <@user>` - Kittybot will no longer ignore commands from specified user (Does not override role ignores).\n"
			+ "`ignorerole <#role>` - Kittybot will ignore commands from the entire specified role.\n"
			+ "`unignorerole <#role>` - Kittybot will no longer ignore commands from the entire specified role (Overridden by per user ignores).\n"
			+ "`auth <@user>` - Promotes the specified user to a kittybot mod, allowing access to mod commands.\n"
			+ "`deauth <@user>` - Demotes the specified user from a kittybot mod role.\n"
			+ "`authrole <#role>` - Promotes role to be kittybot mods, allowing access to mod commands.\n"
			+ "`deauthrole <#role>` - Demotes role from kittybot mod.\n"
			+ "`changeTrigger <new trigger character>` - Changes the character you use to issue commands to kittybot. By default, `!`.\n"
			+ "`startPoll <option 1, ..., option n>` - Creates a poll with a series of comma separated options.\n"
			+ "`addchoice <option n + 1>` - Adds a choice to the poll after initial creation.\n"
			+ "`endPoll` - Ends the poll and displays the results in chat.\n"
			+ "`E621Limit <#>` - Sets the maximum number of images that are grabbed when searching E621. By default, `1`.";

			return new Response(modHelpString);
		}

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
			try
			{
				points.addPoints(message.getMentionedUsers().get(0).getId()
					, msg_id, Integer.parseInt(command[2]));
				return new Response("You gave some beans to " + message.getMentionedUsers().get(0).getAsMention() + "!");
			}
			catch(IndexOutOfBoundsException e)
			{ 
				return new Response("I don't understand ;n;"
				+ "\n`!addpoints @user amount`");
			}
		}

		//Removes points from user
		if(command[0].equalsIgnoreCase("removepoints"))
		{
			points.subPoints(message.getMentionedUsers().get(0).getId()
				, msg_id, Integer.parseInt(command[2]));
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

		// Change the character that is used to tell KittyBot there's a command
		if(command[0].equalsIgnoreCase("changeTrigger"))
		{
			if(command.length < 2)
			{
				return new Response("No trigger given.");
			}

			changeTrigger(msg_id, command[1]);
			return new Response("Trigger changed to: " + command[1]);
		}

		// Polling and associated
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
		
		// E621 Config commands
		if(command[0].equalsIgnoreCase("E621Limit"))
		{
			// Create and parse int.
			int new_limit = 0;
			try
			{ 
				new_limit = Integer.parseInt(command[1]); 
			}
			catch (Exception e) 
			{ 
				return new Response("Eeep! That doesn't make sense! ;3;"); 
			}

			return ReqE621.SetMaxSearchResults(new_limit);
		}
		
		// Return default
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