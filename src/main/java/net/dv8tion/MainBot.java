package main.java.net.dv8tion;


import javax.security.auth.login.LoginException;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.guild.GuildJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import offline.Ref;


public class MainBot extends ListenerAdapter 
{
	public static JDA epi;
	
	static Command command = new Command(); 
		
	public static void main (String [] args) 
			throws LoginException, IllegalArgumentException, InterruptedException
	{
			epi = new JDABuilder(AccountType.BOT).setToken(Ref.Token).buildBlocking();
			epi.getPresence().setGame(Game.playing("with an outlet"));
			epi.addEventListener(new MainBot());
			command.makeTriggers(epi.getGuilds()); 
	}
	
	@Override
	public void onGuildMessageReceived(GuildMessageReceivedEvent event)
	{
		//Ignores herself
		if(event.getAuthor().getName().equals("KittyBot") 
				|| event.getMessage().getContentRaw().length() < 1)
			return;
		
		String message = command.comSent(event.getMessage(), event.getMember(), Ref.CliID);
		if(!message.equals(""))
		{
			event.getChannel().sendMessage(message).queue();
		}	
	}
	
	@Override 
	public void onGuildJoin(GuildJoinEvent event)
	{
		command.serverJoin(event.getGuild().getId(), event.getGuild().getMembers());
	}
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) 
	{
		command.userJoin(event.getUser().getId());
	}

	/*
	 * (non-Javadoc)
	 * @see net.dv8tion.jda.core.hooks.ListenerAdapter
	 * #onPrivateMessageReceived(net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent)
	 */
	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event)
	{
		if(event.getAuthor().getName().equals("KittyBot"))
			return;
		if(event.getAuthor().getName().equals("RinTheSnowMew"))
		{
			
		}
		System.out.println(event.getAuthor().getName()+"'s touching me");		
	}
	
}
