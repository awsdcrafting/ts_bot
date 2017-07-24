package io.github.awsdcrafting.commands.impl;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import io.github.awsdcrafting.commands.Command;
import main.Main;

public class Kick extends Command
{

	public Kick()
	{
		super("Kick", "Kicks the named client from the server, or all others", "unvollständig", new String[]{"Kick"}, 200);
	}

	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		boolean all = false;
		boolean starts = false;

		if (args.length < 1)
		{
			api.sendPrivateMessage(e.getInvokerId(), help());
		} else
		{

			String clientName = args[0];
			// suche leute mit leerzeichen im nickname
			// int j = 1;
			// while((api.getClientByNameExact(clientName, false) ==
			// null)&&(j<args.length))
			// {
			// clientName +=args[j++];
			// }
			// String[] argsNeu =new String[args.length - j];
			// int k = 0;
			// while(j<args.length)
			// {
			// argsNeu[k++] = args[j++];
			// }

			if (clientName.equalsIgnoreCase("all") || clientName.equalsIgnoreCase("*") || clientName.equalsIgnoreCase("alle"))
			{
				all = true;
			}

			if (clientName.startsWith("starts:_"))
			{
				starts = true;
				clientName = clientName.split("_", 2)[1];
			}

			System.out.println("clientName: " + clientName);
			String kickGrund = "";

			if (args.length == 1)
			{
				kickGrund = "Du wurdest gekickt!";
			} else
			{
				// hat er ein leerzeichen im nickname
				// if(api.getClientByNameExact(args[0] + " " + args[1], false)
				// != null)
				// {
				// clientName = args[0] + " " + args[1];
				// String arg = clientName + ",.,";
				// for(int i = 2;i<args.length;i++)
				// {
				// arg += args[i] + ",.,";
				// }
				// args = arg.split(",.,");
				// }
				for (int i = 1; i < args.length; i++)
				{
					kickGrund += args[i] + " ";
				}
			}
			if (all)
			{
				for (Client client : api.getClients())
				{
					System.out.println("apiClientName: " + client.getNickname());
					int clID = client.getId();
					if (clID != e.getInvokerId() && clID != Main.botIDm)
					{
						System.out.println("ClientID: " + clID);
						api.kickClientFromServer(kickGrund, clID);
						api.sendChannelMessage(client.getNickname() + " wurde gekickt!");
					}
				}
			} else
			{
				try
				{
					int clID = Integer.parseInt(clientName);
					for (Client client : api.getClients())
					{
						System.out.println("apiClientName: " + client.getNickname());
						int apiClientID = client.getId();
						int dbID = client.getDatabaseId();
						//workaround should be changed!!
						if (starts)
						{
							String sCLID = clID + "";
							String sDBID = dbID + "";
							String sApiClientID = apiClientID + "";
							if (sDBID.startsWith(sCLID) || sApiClientID.startsWith(sCLID))
							{
								System.out.println("ClientID: " + apiClientID);
								api.kickClientFromServer(kickGrund, apiClientID);
								api.sendChannelMessage(client.getNickname() + " wurde gekickt!");
							}
						} else
						{
							if (apiClientID == clID || dbID == clID)
							{
								System.out.println("ClientID: " + apiClientID);
								api.kickClientFromServer(kickGrund, apiClientID);
								api.sendChannelMessage(clientName + " wurde gekickt!");
							}
						}
					}
				} catch (NumberFormatException e1)
				{
					for (Client client : api.getClients())
					{
						System.out.println("apiClientName: " + client.getNickname());
						String apiClientName = client.getNickname();
						String uID = client.getUniqueIdentifier();

						if (starts)
						{
							if (apiClientName.startsWith(clientName) || uID.startsWith(clientName))
							{
								int clientID = client.getId();
								System.out.println("ClientID: " + clientID);
								api.kickClientFromServer(kickGrund, clientID);
								api.sendChannelMessage(client.getNickname() + " wurde gekickt!");
							}
						} else
						{
							if (apiClientName.equals(clientName) || uID.equals(clientName))
							{
								int clientID = client.getId();
								System.out.println("ClientID: " + clientID);
								api.kickClientFromServer(kickGrund, clientID);
								api.sendChannelMessage(clientName + " wurde gekickt!");
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
		return getName() + ": " + getDescription();
		// TODO Auto-generated method stub

	}

}
