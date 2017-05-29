package io.github.awsdcrafting.commands.impl;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import io.github.awsdcrafting.commands.Command;

public class Msgme extends Command
{
	public Msgme()
	{
		super("Msgme", "Messages the invoker.", new String[]{"Msgme"}, 0);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		api.sendPrivateMessage(e.getInvokerId(), "Hallo :D");

	}

	@Override
	public String help()
	{
		return null;
		// TODO Auto-generated method stub

	}

}
