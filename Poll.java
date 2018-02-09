package main.java.net.dv8tion;

import java.util.ArrayList;
import java.util.HashMap;

public class Poll 
{
	String question;
	ArrayList <String> choices = new ArrayList<String>();
	HashMap <String, Integer> pVote = new HashMap<String,Integer>();
	HashMap <Integer, Integer> votes = new HashMap<Integer,Integer>();
	int totalVotes = 0;
	
	public Poll(String quest)
	{
		question = quest; 
	}
	
	public String addChoice(String newChoice)
	{
		choices.add(newChoice);
		votes.put(choices.size(), 0);
		return "New choice: " + newChoice;
	}
	
	public String getQuest()
	{
		String choi = "";
		for(int i = 0; i < choices.size(); i++)
		{
			choi += i+1 + ". " + choices.get(i) + "\n";
		}
		return "The poll is: " + question + "?\nAnd the answers are: \n" + choi; 
	}
	
	public String vote(int vote, String ID)
	{
		if(pVote.containsKey(ID))
		{
			return "You already voted for: " + choices.get(pVote.get(ID)-1);
		}
		if(vote > choices.size() || vote < 1)
		{
			return "That is not a choice"; 
		}
		
		pVote.put(ID, vote);
		votes.put(vote, votes.get(vote) + 1);
		totalVotes ++;
		return "You voted for " + choices.get(vote-1);
	}
	
	public String results()
	{
		String res = "";
		for(int i = 1; i <= choices.size(); i++)
		{
			res += votes.get(i) + " people voted for " + choices.get(i-1) + " which is " + 
		(int)(((double)votes.get(i)) / ((double)totalVotes) * 100) + "% of the vote.\n";
		}
		return res;
	}
	
	
}
