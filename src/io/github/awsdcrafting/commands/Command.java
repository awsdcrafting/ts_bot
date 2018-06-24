package io.github.awsdcrafting.commands;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public abstract class Command implements Comparable<Command>
{

	protected String name;
	protected String description;
	protected String syntax;
	protected String[] alias;
	protected String help;
	protected int permissionLevel;

	protected int[] allowedGroups;
	protected String[] allowedUIDS;

	public Command(String name, String description, String syntax, String[] alias, int permissionLevel)
	{
		this.name = name;
		this.description = description;
		this.syntax = syntax;
		this.alias = alias;
		this.permissionLevel = permissionLevel;
	}

	public abstract void execute(TS3Api api, TextMessageEvent e, String[] args);

	public abstract String help();

	public int compareTo(Command command)
	{
		return this.name.compareTo(command.getName());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHelp()
	{

		return help;
	}

	public void setHelp(String help)
	{
		this.help = help;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String[] getAlias()
	{

		return alias;
	}

	public void setAlias(String[] alias)
	{

		this.alias = alias;
	}

	public int getPermissionLevel()
	{

		return permissionLevel;
	}

	public String getSyntax(){
		return syntax;
	}

	public void setPermissionLevel(int permissionLevel)
	{
		this.permissionLevel = permissionLevel;
	}

	public boolean sendMessage(TS3Api api, int messageMode, int id, String message)
	{
		if (messageMode == 1)
		{
			return api.sendPrivateMessage(id, message);
		}
		if (messageMode == 2)
		{
			return api.sendChannelMessage(id, message);
		}
		if (messageMode == 3)
		{
			return api.sendServerMessage(id, message);
		}
		return false;
	}

	public boolean sendMessage(TS3Api api, int messageMode, int id, String message, boolean moveback, int mbID)
	{
		boolean ret = false;
		if (messageMode == 1)
		{
			ret = api.sendPrivateMessage(id, message);

		}
		if (messageMode == 2)
		{
			ret = api.sendChannelMessage(id, message);
			if (moveback)
			{
				api.moveQuery(mbID);
			}
		}
		if (messageMode == 3)
		{
			ret = api.sendServerMessage(id, message);
			if (moveback)
			{
				api.selectVirtualServerByPort(mbID);
			}
		}
		return ret;
	}

	public boolean sendSyntax(TS3Api api, TextMessageEvent e){
		boolean ret = true;
		for(int i = 0;i<syntax.length();i++){
			if(i==0){
				ret = ret && api.sendPrivateMessage(e.getInvokerId(),"Syntax: " + syntax);
			}else{
				ret = ret && api.sendPrivateMessage(e.getInvokerId(),syntax);
			}
		}
		return ret;
	}

}