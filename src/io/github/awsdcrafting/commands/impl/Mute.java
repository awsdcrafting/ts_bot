package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.Permission;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import io.github.awsdcrafting.commands.Command;
import main.Main;

import java.util.List;
import java.util.Map;
/**
 * Created by scisneromam on 05.07.2017.
 */
public class Mute extends Command
{

	public Mute()
	{
		super("Mute", "Mutes the named Client", "!mute <mode> <name/id> [ignoreCase]  Available modes: server, client", new String[]{"Mute"}, 100);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{

		if (args.length < 2)
		{
			/*for(int i = 0;i<syntax.length();i++){
				if(i==0){
					api.sendPrivateMessage(e.getInvokerId(), "Syntax: " + syntax);
				}else{
					api.sendPrivateMessage(e.getInvokerId(), syntax);
				}
			}*/
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

			String mode = args[0 + extra];
			String[] clientNames = args[1 + extra].split(",");
			String message = "Muted Clients: ";
			boolean ignoreCase;
			if (args.length < 3 + extra)
			{
				ignoreCase = false;
			} else
			{
				ignoreCase = Boolean.parseBoolean(args[3 + extra]);
			}

			int groupID = 0;
			for (int i = 0; i < clientNames.length; i++)
			{
				int dbID = 0;
				try
				{
					dbID = Integer.parseInt(clientNames[i]);
				} catch (NumberFormatException nFM)
				{
					Client client = api.getClientByNameExact(clientNames[i], ignoreCase);
					dbID = 0;
					if (client != null)
					{
						//nothing to do here
						//dbID = client.getDatabaseId();
					} else
					{
						client = api.getClientByNameExact(clientNames[i].replace("-", " "), ignoreCase);
						if (client != null)
						{
							//nothing to do here
							//dbID = client.getDatabaseId();
						} else
						{
							client = api.getClientByUId(clientNames[i]);
							if (client != null)
							{
								//nothing to do here
								//dbID = client.getDatabaseId();
							}
						}
					}
					if (client != null)
					{
						dbID = client.getDatabaseId();
					} else
					{
						//no client found
					}
					System.out.println(dbID);

					if (mode.equalsIgnoreCase("client"))
					{

						try
						{
							api.addClientPermission(dbID, "i_client_needed_talk_power", 9999, true);
							api.addClientPermission(dbID, "i_client_talk_power", -1, true);
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						} catch (TS3CommandFailedException ignored)
						{

						}
					} else if (mode.equalsIgnoreCase("servergroup") || mode.equalsIgnoreCase("server"))
					{
						boolean muteGroupExists = false;
						List<ServerGroup> serverGroups = api.getServerGroups();
						for (int x = 0; x < serverGroups.size(); x++)
						{
							ServerGroup group = serverGroups.get(x);
							boolean contains = group.getName().toLowerCase().contains("muted");
							boolean doBreak = false;
							if (contains)
							{
								boolean containsMute = false;
								int muteInt = 0;
								int neededMuteInt = 0;
								for (Permission permission : api.getServerGroupPermissions(group.getId()))
								{
									if (permission.getName().equalsIgnoreCase("i_client_talk_power"))
									{
										muteInt = permission.getValue();
									} else if (permission.getName().equalsIgnoreCase("i_client_needed_talk_power"))
									{
										neededMuteInt = permission.getValue();
									}
								}
								if (neededMuteInt > muteInt)
								{
									containsMute = true;
								}
								if (containsMute)
								{
									muteGroupExists = true;
									groupID = group.getId();
									doBreak = true;
								}
							}
							if (doBreak)
							{
								break;
							}
						}
						if (!muteGroupExists)
						{
							int code = api.addServerGroup("botGenerated_muted");
							if (code == 0)
							{
								for (ServerGroup group : api.getServerGroups())
								{
									if (group.getName().equalsIgnoreCase("botGenerated_muted"))
									{
										groupID = group.getId();
									}
								}
							}
							api.addServerGroupPermission(groupID, "i_client_talk_power", -1, true, true);
							api.addServerGroupPermission(groupID, "i_client_needed_talk_power", 9999, false, true);
						}

						try
						{
							api.addClientToServerGroup(groupID, dbID);
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						} catch (TS3CommandFailedException ignored)
						{

						}

					} else if (mode.equalsIgnoreCase("channel"))
					{
						//unfinished
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
