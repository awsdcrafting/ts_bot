package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.Permission;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import io.github.awsdcrafting.commands.Command;

import java.util.List;
/**
 * Created by Michael on 07.07.2017.
 */
public class UnMute extends Command
{

	public UnMute()
	{
		super("UnMute", "UnMutes a previously muted client", new String[]{"EntMute"}, 100);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		//expected: mode name/id
		if (args.length < 2)
		{
			api.sendPrivateMessage(e.getInvokerId(), "Syntax: !unmute <mode> <name/id> [ignoreCase]");
		} else
		{
			boolean ignoreCase;
			if (args.length < 3)
			{
				ignoreCase = false;
			} else
			{
				ignoreCase = Boolean.parseBoolean(args[3]);
			}
			String mode = args[0];
			String[] clientNames = args[1].split(",");
			String message = "UnMuted Clients: ";
			int groupID = 0;

			if (mode.equalsIgnoreCase("server"))
			{
				for(int i = 0;i<clientNames.length;i++){
					try{
						int dbID = Integer.parseInt(clientNames[i]);
						for(ServerGroup group :api.getServerGroupsByClientId(dbID)){
							if(group.getName().contains("mute")){
								groupID=group.getId();
							}
						}
						if(api.removeClientFromServerGroup(groupID,dbID)){
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						}
					}catch(NumberFormatException nFM){
						Client client = api.getClientByNameExact(clientNames[i], ignoreCase);
						int dbID = 0;
						if(client!=null){
							dbID = client.getDatabaseId();
						}else{
							client = api.getClientByUId(clientNames[i]);
							if(client!=null)
							{
								dbID = client.getDatabaseId();
							}
						}
						for(ServerGroup group :api.getServerGroupsByClientId(dbID)){
							if(group.getName().contains("mute")){
								groupID=group.getId();
							}
						}
						System.out.println(dbID);
						boolean worked = api.removeClientFromServerGroup(groupID,dbID);
						if(worked){
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
