package main.java.net.dv8tion;

import java.util.HashMap;

public class PollManager 
{
	HashMap <String, Poll> polls = new HashMap <String, Poll>();
	PollManager()
	{
		
	}
	
	public void newPoll(String serverID, String polling)
	{
		polls.put(serverID, new Poll(polling));
	}
	
	public String endPoll(String serverID)
	{
		if(polls.containsKey(serverID))
		{
			polls.remove(serverID);
			return "Poll ended!";
		}
		return "No poll running.";
	}
	
	public String newVote(String serverID, String userID, int vote)
	{
		if(!polls.containsKey(serverID))
		{
			return "There is no poll running!";
		}
		return polls.get(serverID).vote(vote, userID);
	}
	
	public String addChoice(String serverID, String choice)
	{
		return polls.get(serverID).addChoice(choice);
	}
	
	public String getQuestion(String serverID)
	{
		if(!polls.containsKey(serverID))
		{
			return "There is no poll running!";
		}
		return polls.get(serverID).getQuest();
	}
	
	public String getResults(String serverID)
	{
		if(!polls.containsKey(serverID))
		{
			return "There is no poll running!";
		}
		return polls.get(serverID).results();
	}
}
