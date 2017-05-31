package io.github.awsdcrafting.commands.impl;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import io.github.awsdcrafting.commands.Command;
import main.Main;

public class Test extends Command
{

	public Test()
	{
		super("Test", "Test.", new String[]{"Test"}, 500);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		api.sendPrivateMessage(e.getInvokerId(), "test");
		String ausgabe = "";
		for (Client client : api.getClients())
		{
			int apiClientID = client.getId();
			int clientID = e.getInvokerId();

			if (clientID == apiClientID)
			{
				int[] groups = client.getServerGroups();
				for (int i = 0; i < groups.length; i++)
				{
					ausgabe += groups[i] + " ";
				}

				ausgabe += "\nadmingroups: ";
				for (int i = 0; i < Main.adminGroups.size(); i++)
				{
					ausgabe += Main.adminGroups.get(i) + " ";
				}
			}
		}
		api.sendPrivateMessage(e.getInvokerId(), ausgabe);
	}

	@Override
	public String help()
	{
		return null;
		// TODO Auto-generated method stub

	}

}
