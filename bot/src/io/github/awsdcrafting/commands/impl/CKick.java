package io.github.awsdcrafting.commands.impl;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import io.github.awsdcrafting.commands.Command;

public class CKick extends Command
{
	
	public CKick()
	{

		super("CKick", "Kicks the named client from the channel",
				new String[]{"CKick"}, 200);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args){
	
		String clientName = args[0];
		System.out.println("clientName: " + clientName);
		String kickGrund = "";
		if(api.getClientByNameExact(args[0] + " " + args[1], false)!=null)
		{
			clientName = args[0] + " " + args[1];
			String arg = clientName + ",.,";
			for(int i = 2;i<args.length;i++)
			{
				arg += args[i] + ",.,";
			}
			args = arg.split(",.,");
		}
		if (args.length == 1)
		{
			kickGrund = "Du wurdest gekickt!";
		}else
		{
			for(int i = 1;i<args.length;i++)
			{
				kickGrund += args[i] + " ";
			}
		}
		
		try
		{
			int clID = Integer.parseInt(clientName);
			for (Client client : api.getClients())
			{
				System.out
						.println("apiClientName: " + client.getNickname());
				int apiClientID = client.getId();
				
				if (apiClientID == clID)
				{
					System.out.println("ClientID: " + clID);
					api.kickClientFromChannel(kickGrund, clID);
					api.sendChannelMessage(clientName + " wurde gekickt!");
				}
			}
		} catch (NumberFormatException e1)
		{
			for (Client client : api.getClients())
			{
				System.out
						.println("apiClientName: " + client.getNickname());
				String apiClientName = client.getNickname();
				if (apiClientName.equals(clientName))
				{
					int clientID = client.getId();
					System.out.println("ClientID: " + clientID);
					api.kickClientFromChannel(kickGrund, clientID);
					api.sendChannelMessage(clientName + " wurde gekickt!");
				}
			}
		}

	}
	
	

	@Override
	public String help()
	{
		// TODO Auto-generated method stub
		return null;
	}		
	
}
