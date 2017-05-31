package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.awsdcrafting.commands.Command;
import main.Main;

import java.util.ArrayList;

public class Config extends Command
{

	public Config()
	{
		super("Config", "Changes the Config", new String[]{"Config"}, 100);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		if (args.length < 1)
		{
			api.sendPrivateMessage(e.getInvokerId(), help());
			api.sendPrivateMessage(e.getInvokerId(), "Syntax: <default/reload/setting name <new value>>");

		} else
		{
			if (args[0].equalsIgnoreCase("set"))
			{
				if (args.length < 2)
				{
					api.sendPrivateMessage(e.getInvokerId(), "Sets the config");
					api.sendPrivateMessage(e.getInvokerId(), "Syntax: !config <default/reload/setting name <new value>>");
				} else
				{
					if (args.length == 3)
					{
						if (args[1].equalsIgnoreCase("default"))
						{
							Main.configManager.setDefaultConfig();
						} else
						{

							if (args[1].equalsIgnoreCase("reload"))
							{
								Main.configManager.load();
								Main.setConfig();
							} else
							{
								api.sendPrivateMessage(e.getInvokerId(), "Wrong Syntax!");
								api.sendPrivateMessage(e.getInvokerId(), "!config <default/reload/setting_name <new_value>>");
							}
						}
					} else
					{
						if (args.length == 4)
						{
							Main.configManager.load();
							ArrayList<String> config = Main.configManager.getConfig();
							String setting_Name = args[2];
							String setting_Value = args[3];

							for (int i = 0; i < config.size(); i++)
							{
								if(config.get(i).equalsIgnoreCase(setting_Name)){
									config.set(i,setting_Name+"="+setting_Value);
								}
							}

						}
					}
				}
			}
		}
	}

	@Override
	public String help()
	{
		return getDescription();
	}
}
