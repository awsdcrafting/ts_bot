package io.github.awsdcrafting.commands.impl;

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

		super("Help", "Returns all Commands with their description.",
				new String[]{"Help"}, 0);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		if (args.length > 0)
		{
			Command command = main.Main.commandManager
					.getEnabledCommandByName(args[0]);
			
			if (command != null)
			{
				String helpMessage = command.getHelp();
				int helpPower = command.getPermissionLevel();
				if (helpMessage == null || helpMessage.equalsIgnoreCase(""))
				{
					helpMessage = command.getDescription();
					if (helpPower >= main.Main.commandManager.getAdminLevel()){
						helpMessage += " Admin Command!";
					}else if (helpPower >= main.Main.commandManager.getModLevel())
					{
						helpMessage += " Mod Command!";
					}else{
						helpMessage += " Everyone can use this command!";
					}
				}
				if (e.getTargetMode().getIndex() != 2)
				{
					api.sendPrivateMessage(e.getInvokerId(), helpMessage);
				} else
				{
					api.sendChannelMessage(helpMessage);
				}
			}
		} else
		{
			List<Command> commands = main.Main.commandManager
					.getEnabledCommandsList();
			Collections.sort(commands);

			if (e.getTargetMode().getIndex() != 2)
			{
				api.sendPrivateMessage(e.getInvokerId(), "BOT Help");
			} else
			{
				api.sendChannelMessage("BOT Help");
			}
			String prefixes = "";
			for (int i = 0; i < main.Main.commandManager.chat_Prefix.length; i++)
			{
				prefixes += main.Main.commandManager.chat_Prefix[i] + " ";
			}
			if (e.getTargetMode().getIndex() != 2)
			{
				api.sendPrivateMessage(e.getInvokerId(),
						"allowed Chat Prefixes: " + prefixes);
				api.sendPrivateMessage(e.getInvokerId(),
						"Enabled Commands and their Description:");
				for (int i = 0; i < commands.size(); i++);
			} else
			{
				api.sendChannelMessage(
						"[B]allowed Chat Prefixes: [/B]" + prefixes);
				api.sendChannelMessage(
						"[B]Enabled Commands and their Description:[/B]");
			}
			for (int i = 0; i < commands.size(); i++)
			{
				if (e.getTargetMode().getIndex() != 2)
				{
					api.sendPrivateMessage(e.getInvokerId(),
							commands.get(i).getName() + " : "
									+ commands.get(i).getDescription());
				} else
				{
					api.sendChannelMessage(commands.get(i).getName() + " : "
							+ commands.get(i).getDescription());
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
