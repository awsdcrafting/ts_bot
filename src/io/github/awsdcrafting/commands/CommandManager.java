package io.github.awsdcrafting.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import io.github.awsdcrafting.commands.impl.*;
import main.Main;

public class CommandManager
{

	private List<Command> allCommands = new ArrayList<Command>();
	private List<Command> modCommands = new ArrayList<Command>();
	private List<Command> adminCommands = new ArrayList<Command>();
	private List<Command> enabledCommands = new ArrayList<Command>();

	private int adminLevel = 200;
	private int modLevel = 100;

	private String[] chat_Prefix /*= "! . + #".split(" ");*/ = new String[]{"!", ".", "+", "#"};
	// public String chat_Prefix = ".";

	public CommandManager()
	{
		// Commands are added here
		//alphabetical
		//a
		addCommand(new Abfuck());
		//b
		//c
		addCommand(new CKick());
		addCommand(new Config());
		//d
		//e
		//f
		//g
		//h
		addCommand(new Help());
		//i
		//j
		addCommand(new Join());
		//k
		addCommand(new Kick());
		//l
		//m
		addCommand(new Msg());
		addCommand(new Msgme());
		addCommand(new Mute());
		//n
		//o
		//p
		addCommand(new Poke());
		//q
		addCommand(new Quit());
		//r
		//s
		addCommand(new Spam());
		//t
		addCommand(new Test());
		//u
		addCommand(new UnMute());
		//v
		//w
		//x
		//y
		//z

	}

	public List<Command> getAllCommandsList()
	{
		return allCommands;
	}

	public List<Command> getEnabledCommandsList()
	{
		return enabledCommands;
	}

	public String[] getChat_Prefix()
	{
		return chat_Prefix;
	}

	public void addCommand(Command cmd)
	{
		this.allCommands.add(cmd);
		this.enabledCommands.add(cmd);
		if (cmd.getPermissionLevel() >= adminLevel)
		{
			this.adminCommands.add(cmd);
		} else if (cmd.getPermissionLevel() >= modLevel)
		{
			this.modCommands.add(cmd);
		}
	}

	public int getAdminLevel()
	{
		return adminLevel;
	}

	public int getModLevel()
	{
		return modLevel;
	}

	public Command getEnabledCommandByName(String name)
	{
		for (int i = 0; i < enabledCommands.size(); i++)
		{
			if (enabledCommands.get(i).getName().equalsIgnoreCase(name))
			{
				return enabledCommands.get(i);
			}
		}
		return null;
	}

	public Command getCommandByName(String name)
	{
		for (int i = 0; i < allCommands.size(); i++)
		{
			if (allCommands.get(i).getName().equalsIgnoreCase(name))
			{
				return allCommands.get(i);
			}
		}
		return null;
	}

	//todo rewrite permission system
	public boolean executeTextMessageEvent(TS3Api api, TextMessageEvent e, String text)
	{
		int x = 0;
		for (int i = 0; i < chat_Prefix.length; i++)
		{
			if (!text.startsWith(chat_Prefix[i]))
			{
				x++;
			}
		}
		if (x == chat_Prefix.length)
		{
			return false;
		}
		text = text.substring(1);
		String[] arguments = text.split(" ");

		for (Command cmd : this.enabledCommands)
		{
			if (cmd.getName().equalsIgnoreCase(arguments[0]))
			{

				Client client = api.getClientByUId(e.getInvokerUniqueId());

				int[] groups = client.getServerGroups();
				for (int i = 0; i < groups.length; i++)
				{
					if (cmd.getPermissionLevel() >= main.Main.commandManager.adminLevel)
					{
						if (main.Main.adminGroups.contains(groups[i]))
						{
							String[] args = (String[]) Arrays.copyOfRange(arguments, 1, arguments.length);
							cmd.execute(api, e, args);
							// api.sendPrivateMessage(e.getInvokerId(),
							// "admin");
							return true;
						}
					} else if (cmd.getPermissionLevel() >= main.Main.commandManager.modLevel)
					{
						if (Main.modGroups.contains(groups[i]) || Main.adminGroups.contains(groups[i]))
						{
							String[] args = (String[]) Arrays.copyOfRange(arguments, 1, arguments.length);
							cmd.execute(api, e, args);
							// api.sendPrivateMessage(e.getInvokerId(), "mod");
							return true;
						}
					} else
					{
						String[] args = (String[]) Arrays.copyOfRange(arguments, 1, arguments.length);
						cmd.execute(api, e, args);
						// api.sendPrivateMessage(e.getInvokerId(), "nothing" +
						// cmd.getPermissionLevel());
						return true;
					}

				}
				api.sendPrivateMessage(e.getInvokerId(), "You do not have permissions to use the command " + cmd.getName() + "! :C");
				api.sendPrivateMessage(e.getInvokerId(), "needed permission-level: " + cmd.getPermissionLevel());
				return false;
			}

			String[] alias = cmd.getAlias();
			for (int i = 0; i < alias.length; i++)
			{
				if (alias[i].equalsIgnoreCase(arguments[0]))
				{
					for (Client client : api.getClients())
					{
						int apiClientID = client.getId();
						int clientID = e.getInvokerId();

						if (clientID == apiClientID)
						{
							int[] groups = client.getServerGroups();
							for (int i1 = 0; i1 < groups.length; i1++)
							{
								if (cmd.getPermissionLevel() == main.Main.commandManager.adminLevel)
								{
									if (main.Main.adminGroups.contains(groups[i1]))
									{
										String[] args = (String[]) Arrays.copyOfRange(arguments, 1, arguments.length);
										cmd.execute(api, e, args);
										return true;
									}
								} else if (cmd.getPermissionLevel() == main.Main.commandManager.modLevel)
								{
									if (main.Main.modGroups.contains(groups[i1]) || Main.adminGroups.contains(groups[i]))
									{
										String[] args = (String[]) Arrays.copyOfRange(arguments, 1, arguments.length);
										cmd.execute(api, e, args);
										return true;
									}
								} else
								{
									String[] args = (String[]) Arrays.copyOfRange(arguments, 1, arguments.length);
									cmd.execute(api, e, args);
									return true;
								}

							}
							api.sendPrivateMessage(e.getInvokerId(), "You do not have permissions to use this command! :C");
							api.sendPrivateMessage(e.getInvokerId(), "needed permission-level: " + cmd.getPermissionLevel());
							return false;
						}
					}
				}
			}
		}
		return false;
	}
}
