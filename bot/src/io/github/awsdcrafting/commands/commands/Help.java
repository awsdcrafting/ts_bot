package io.github.awsdcrafting.commands.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import io.github.awsdcrafting.commands.Command;
import io.github.awsdcrafting.commands.CommandManager;

public class Help extends Command
{

	public Help()
	{
		super("Help", "Returns all Commands with their description.", new String[]{"Help"},0);
	}

	@Override
	public void execute(TS3Api api,TextMessageEvent e,String[] args)
	{
		List<Command> commands = main.Main.commandManager.getEnabledCommandsList();
		Collections.sort(commands);
		api.sendPrivateMessage(e.getInvokerId(), "BOT Help");
		String prefixes = "";
		for(int i = 0; i<main.Main.commandManager.chat_Prefix.length;i++){
			prefixes += main.Main.commandManager.chat_Prefix[i] + " ";
		}
		api.sendPrivateMessage(e.getInvokerId(), "allowed Chat Prefixes: " + prefixes);
		api.sendPrivateMessage(e.getInvokerId(), "Enabled Commands and their Description:");
		for (int i = 0; i < commands.size(); i++)
		{
			api.sendPrivateMessage(e.getInvokerId(),commands.get(i).getName() + " : " + commands.get(i).getDescription());
		}
	}

	@Override
	public void help()
	{
		// TODO Auto-generated method stub
		
	}


}
