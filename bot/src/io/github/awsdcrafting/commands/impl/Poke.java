package io.github.awsdcrafting.commands.impl;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import io.github.awsdcrafting.commands.Command;

public class Poke extends Command
{

	public Poke()
	{
		super("Poke", "Pokes the named client.", new String[]{"Poke"}, 0);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		String clientName = args[0];
		System.out.println("clientName: " + clientName);
		String msgMSG = "";
		
		if(args.length==1){
			msgMSG = "Du Wurdest angestupst";
		}else{
			for (int i = 1; i < args.length; i++)
			{
				msgMSG += args[i] + " ";
			}
		}
		
		
		
		try
		{
			int clID = Integer.parseInt(clientName);
			for (Client client : api.getClients())
			{
				System.out.println("apiClientName: " + client.getNickname());
				int apiClientID = client.getId();
				int dbID = client.getDatabaseId();

				if (apiClientID == clID)
				{
					int clientID = client.getId();
					System.out.println("ClientID: " + clientID);
					api.pokeClient(clientID, e.getInvokerName()
							+ " hat dich angeschrieben: " + msgMSG);
				} else if (dbID == clID)
				{
					int clientID = client.getId();
					System.out.println("ClientID: " + clientID);
					api.pokeClient(clientID, e.getInvokerName()
							+ " hat dich angeschrieben: " + msgMSG);
				}
			}
		} catch (NumberFormatException e1)
		{
			for (Client client : api.getClients())
			{
				System.out.println("apiClientName: " + client.getNickname());
				String apiClientName = client.getNickname();
				String uID = client.getUniqueIdentifier();
				if (apiClientName.equals(clientName))
				{
					int clientID = client.getId();
					System.out.println("ClientID: " + clientID);
					api.pokeClient(clientID, e.getInvokerName()
							+ " hat dich angeschrieben: " + msgMSG);
				} else if (uID.equals(clientName))
				{
					int clientID = client.getId();
					System.out.println("ClientID: " + clientID);
					api.pokeClient(clientID, e.getInvokerName()
							+ " hat dich angeschrieben: " + msgMSG);
				}
			}
		}

	}

	@Override
	public String help()
	{
		return null;
		// TODO Auto-generated method stub

	}

}
