package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.awsdcrafting.commands.Command;
import main.Main;

import java.util.ArrayList;
import java.util.Random;
/**
 * Created by scisneromam on 15.11.2017.
 */
public class Join extends Command
{

	public Join()
	{
		super("Join", "Sets the join mode", "unfinished", new String[]{"Join"}, 500);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{

		if (args.length < 1)
		{
			api.sendPrivateMessage(e.getInvokerId(), "Syntax: " + syntax);
		} else
		{
			switch (args[0].toLowerCase())
			{
			case "mode":
			{
				if (args.length < 2)
				{
					api.sendPrivateMessage(e.getInvokerId(), "Join Mode: " + Main.store.getJoinMode());
				} else
				{
					switch (args[1].toLowerCase())
					{
					case "inviteonly":
						Main.store.setJoinMode("inviteonly");
						break;
					case "all":
						Main.store.setJoinMode("all");
						break;
					default:
						api.sendPrivateMessage(e.getInvokerId(), "Unknown mode");
						break;
					}

				}
			}
			break;

			case "invite":{
				api.sendPrivateMessage(e.getInvokerId(), "Invitecode: " + generateInviteCode());
			}
			break;

			default:{
				boolean invited = false;
				ArrayList<String> inviteCodes = Main.store.getInviteCodes();
				for(int i = 0;i<inviteCodes.size();i++){
					if(args[0].equals(inviteCodes.get(i))){
						invited = true;
						Main.notInvited.remove(e.getInvokerId());
						Main.store.removeInviteCode(args[0]);
						break;
					}
				}
				if(!invited){
					api.sendPrivateMessage(e.getInvokerId(), "Syntax: " + syntax);
				}
			}
			break;

			}
		}

	}

	public String generateInviteCode()
	{
		String candidateChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < 7; i++)
		{
			sb.append(candidateChars.charAt(random.nextInt(candidateChars.length())));
		}

		return sb.toString();
	}

	@Override
	public String help()
	{
		return null;
	}
}
