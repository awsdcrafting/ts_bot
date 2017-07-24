package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.awsdcrafting.commands.Command;
/**
 * Created by Michael on 31.05.2017.
 */
public class Spam extends Command
{
	boolean infinite = false;
	public Spam()
	{
		super("Spam", "Spams!","!Spam <stop/mode <count/infinite> <message>>", new String[]{"Spam"}, 100);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{

		String mode = "channel";
		int count = 10;
		String message = "spam";
		if (args.length < 1)
		{
			api.sendPrivateMessage(e.getInvokerId(), "Syntax: " + syntax);
		} else
		{
			if (args[0].equalsIgnoreCase("stop"))
			{
				infinite = false;
			} else if (args.length < 3)
			{
				api.sendPrivateMessage(e.getInvokerId(), "Syntax: !Spam <stop/mode <count/infinite> <message>>");
			} else
			{
				if (args[0].equalsIgnoreCase("channel") || args[0].equalsIgnoreCase("server"))
				{
					mode = args[0];
				}

				try
				{
					count = Integer.parseInt(args[1]);
					if (count <= 0)
					{
						infinite = true;
					}
				} catch (NumberFormatException nFM)
				{
					if (args[1].equalsIgnoreCase("infinite") || args[1].equalsIgnoreCase("unendlich"))
					{
						infinite = true;
					} else
					{
						api.sendPrivateMessage(e.getInvokerId(), "Syntax: !Spam <stop/mode [COLOR=#aa0000]<count/infinite>[/COLOR] <message>>");
						count = 0;
					}
				}

				message = args[2];
				for(int i = 3;i<args.length;i++){
					message += " " + args[i];
				}

				if (infinite)
				{
					while (infinite)
					{
						if (mode.equalsIgnoreCase("server"))
						{
							api.sendServerMessage(message);
						}
						if (mode.equalsIgnoreCase("channel"))
						{
							api.sendChannelMessage(message);
						}
					}
				} else
				{
					for (int i = 0; i < count; i++)
					{
						if (mode.equalsIgnoreCase("server"))
						{
							api.sendServerMessage(message);
						}
						if (mode.equalsIgnoreCase("channel"))
						{
							api.sendChannelMessage(message);
						}
					}
				}

			}
		}
	}
	@Override
	public String help()
	{
		return null;
	}
}
