package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.Permission;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import io.github.awsdcrafting.commands.Command;

import java.util.List;
/**
 * Created by scisneromam on 07.07.2017.
 */
public class UnMute extends Command
{

	public UnMute()
	{
		super("UnMute", "UnMutes a previously muted client","!unmute <mode> <name/id> [ignoreCase]", new String[]{"EntMute"}, 100);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		//expected: mode name/id
		if (args.length < 2)
		{
			api.sendPrivateMessage(e.getInvokerId(), "Syntax: " + syntax);
		} else
		{
			int extra = 0;
			for (int i = 0; i < args.length; i++)
			{
				if (args[i].startsWith("-"))
				{
					String[] extraBefehle = args[i].split("-");
					for (int x = 1; x < extraBefehle.length; x++)
					{
						if (extraBefehle[x].equals(""))
						{

						} else
						{

						}
					}
				}

			}
			boolean ignoreCase;
			if (args.length < 3 + extra)
			{
				ignoreCase = false;
			} else
			{
				ignoreCase = Boolean.parseBoolean(args[3 + extra]);
			}

			String mode = args[0 + extra];
			String[] clientNames = args[1 + extra].split(",");
			String message = "UnMuted Clients: ";
			int groupID = 0;
			if (mode.equalsIgnoreCase("client"))
			{
				for (int i = 0; i < clientNames.length; i++)
				{
					try
					{
						int dbID = Integer.parseInt(clientNames[i]);
						boolean worked1 = api.deleteClientPermission(dbID, "i_client_needed_talk_power");
						boolean worked2 = api.deleteClientPermission(dbID, "i_client_talk_power");
						if (worked1&&worked2)
						{
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						}
					} catch (NumberFormatException nFM)
					{
						Client client = api.getClientByNameExact(clientNames[i], ignoreCase);
						int dbID = 0;
						if (client != null)
						{
							dbID = client.getDatabaseId();
						} else
						{
							client = api.getClientByUId(clientNames[i]);
							if (client != null)
							{
								dbID = client.getDatabaseId();
							}
						}
						System.out.println(dbID);
						boolean worked1 = api.deleteClientPermission(dbID, "i_client_needed_talk_power");
						boolean worked2 = api.deleteClientPermission(dbID, "i_client_talk_power");
						System.out.println(worked1 && worked2);
						if (worked1 && worked2)
						{
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						}
					}
				}
			} else if (mode.equalsIgnoreCase("servergroup"))
			{
				for (int i = 0; i < clientNames.length; i++)
				{
					try
					{
						int dbID = Integer.parseInt(clientNames[i]);
						for (ServerGroup group : api.getServerGroupsByClientId(dbID))
						{
							if (group.getName().contains("mute"))
							{
								groupID = group.getId();
							}
						}
						if (api.removeClientFromServerGroup(groupID, dbID))
						{
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						}
					} catch (NumberFormatException nFM)
					{
						Client client = api.getClientByNameExact(clientNames[i], ignoreCase);
						int dbID = 0;
						if (client != null)
						{
							dbID = client.getDatabaseId();
						} else
						{
							client = api.getClientByUId(clientNames[i]);
							if (client != null)
							{
								dbID = client.getDatabaseId();
							}
						}
						for (ServerGroup group : api.getServerGroupsByClientId(dbID))
						{
							if (group.getName().contains("mute"))
							{
								groupID = group.getId();
							}
						}
						System.out.println(dbID);
						boolean worked = api.removeClientFromServerGroup(groupID, dbID);
						if (worked)
						{
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						}
					}

				}

			}

			api.sendPrivateMessage(e.getInvokerId(), message);
		}
	}

	@Override
	public String help()
	{
		return null;
	}
}
