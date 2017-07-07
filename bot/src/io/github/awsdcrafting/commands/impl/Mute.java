package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.Permission;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import io.github.awsdcrafting.commands.Command;
import main.Main;

import java.util.List;
import java.util.Map;
/**
 * Created by Michael on 05.07.2017.
 */
public class Mute extends Command
{

	public Mute()
	{
		super("Mute", "Mutes the named Client", new String[]{"Mute"}, 100);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		//expected: mode name/id
		if (args.length < 2)
		{
			api.sendPrivateMessage(e.getInvokerId(), "Syntax: !mute <mode> <name/id> [ignoreCase]");
		} else
		{
			boolean ignoreCase;
			if(args.length<3){
				ignoreCase=false;
			}else{
					ignoreCase = Boolean.parseBoolean(args[3]);
			}
			String mode = args[0];
			String[] clientNames = args[1].split(",");
			String message = "Muted Clients: ";
			boolean muteGroupExists = false;
			int groupID = 0;
			if (mode.equalsIgnoreCase("server"))
			{
				List<ServerGroup> serverGroups = api.getServerGroups();
				for (int i = 0; i < serverGroups.size(); i++)
				{
					ServerGroup group = serverGroups.get(i);
					boolean contains = group.getName().toLowerCase().contains("muted");
					boolean doBreak = false;
					if (contains)
					{
						boolean containsMute=false;
						int muteInt = 0;
						int neededMuteInt = 0;
						for(Permission permission : api.getServerGroupPermissions(group.getId())){
							if(permission.getName().equalsIgnoreCase("i_client_talk_power")){
								muteInt = permission.getValue();
							}else if(permission.getName().equalsIgnoreCase("i_client_needed_talk_power")){
								neededMuteInt = permission.getValue();
							}
						}
						if(neededMuteInt>muteInt){
							containsMute=true;
						}
						if(containsMute)
						{
							muteGroupExists = true;
							groupID = group.getId();
							 doBreak = true;
						}
					}
					if(doBreak){
						break;
					}
				}
				if (!muteGroupExists)
				{
					groupID = api.addServerGroup("botGenerated_muted");
					if(groupID<=0){
						for(ServerGroup group : api.getServerGroups()){
							if(group.getName().equalsIgnoreCase("botGenerated_muted")){
								groupID = group.getId();
							}
						}
					}
					api.addServerGroupPermission(groupID, "i_client_talk_power", -1, true, true);
					api.addServerGroupPermission(groupID, "i_client_needed_talk_power", 9999, false, true);
				}
				for(int i = 0;i<clientNames.length;i++){
					try{
						int dbID = Integer.parseInt(clientNames[i]);
						if(api.addClientToServerGroup(groupID,dbID)){
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
						System.out.println(dbID);
						boolean worked = api.addClientToServerGroup(groupID,dbID);
						System.out.println(worked);
						if(worked){
							message += api.getDatabaseClientInfo(dbID).getNickname() + " ";
						}
					}

				}

			}


			api.sendPrivateMessage(e.getInvokerId(),message);
		}

	}
	@Override
	public String help()
	{
		return null;
	}
}
