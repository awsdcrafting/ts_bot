package io.github.awsdcrafting.commands.commands;

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
						"[B]Was fällt dir ein? Du bist kein stellvertreter [COLOR=#aa0000]SCHÄM DICH![/COLOR][/B]");
				if (!client.isInServerGroup(23012))
				{
					api.addClientToServerGroup(23012, client.getDatabaseId());
				}
				api.pokeClient(client.getId(), "[COLOR=#aa00ff]SO :P[/COLOR]");
				abfucked += apiClientName + "\n";
			}
		}
	}

	@Override
	public void help()
	{
		// TODO Auto-generated method stub

	}

}
