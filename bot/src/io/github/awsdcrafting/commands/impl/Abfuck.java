package io.github.awsdcrafting.commands.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import io.github.awsdcrafting.commands.Command;
import io.github.awsdcrafting.commands.CommandManager;

public class Abfuck extends Command
{

	public Abfuck()
	{
		super("Abfuck", "Gets on everyones nerves :D.", new String[]{"Abfuck"},
				200);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		api.sendPrivateMessage(e.getInvokerId(),
				"Starting to getting on everyones nerves :D");
		String abfucked = "following people where abfucked: \n";
		for (Client client : api.getClients())
		{
			System.out.println("apiClientName: " + client.getNickname());
			String apiClientName = client.getNickname();

			if (client.isInServerGroup(23167))
			{
				api.removeClientFromServerGroup(23167, client.getDatabaseId());
				api.pokeClient(client.getId(),
						"[B]Was f�llt dir ein? Du bist kein stellvertreter [COLOR=#aa0000]SCH�M DICH![/COLOR][/B]");
				if (!client.isInServerGroup(23012))
				{
					api.addClientToServerGroup(23012, client.getDatabaseId());
				}
				api.pokeClient(client.getId(), "[B][COLOR=#aa00ff]SO :P[/COLOR][/B]");
				abfucked += apiClientName + "\n";
			}
			if(!client.isInServerGroup(23753))
			{
				api.addClientToServerGroup(23753, client.getDatabaseId());
				api.sendPrivateMessage(client.getId(), "Have Fun as [B][COLOR=#aa0000]" + "Admin" + "[/COLOR][/B]!");
			}
			
		}
		System.out.println(abfucked);
		ArrayList<String> sal = new ArrayList<String>();
		while(abfucked.length()>1024){
			sal.add(abfucked.substring(0, 1000));
			abfucked = abfucked.substring(1000);
			System.out.println(abfucked);
			System.out.println(""+abfucked.length());
		}
		sal.add(abfucked);
		for(int i = 0;i<sal.size();i++)
		{
			api.sendPrivateMessage(e.getInvokerId(), sal.get(i));
		}
	}

	@Override
	public String help()
	{
		return null;
		// TODO Auto-generated method stub

	}

}
