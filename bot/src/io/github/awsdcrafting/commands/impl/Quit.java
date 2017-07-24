package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import io.github.awsdcrafting.commands.Command;
import main.Main;
/**
 * Created by Michael on 31.05.2017.
 */
public class Quit extends Command
{

	public Quit()
	{
		super("Quit", "Makes the bot quit","!quit", new String[]{"Exit","botquit"}, 200);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{
		System.exit(0);
	}
	@Override
	public String help()
	{
		return null;
	}
}
