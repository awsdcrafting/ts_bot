package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.awsdcrafting.commands.Command;
import main.Main;

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
		} else
		{
			if (args[0].equalsIgnoreCase("set"))
			{
				if (args.length < 2)
				{
					api.sendPrivateMessage(e.getInvokerId(), "Sets the config");
					api.sendPrivateMessage(e.getInvokerId(), "default or a setting");
				} else
				{
					if (args[1].equalsIgnoreCase("default"))
					{
						Main.configManager.setDefaultConfig();
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
