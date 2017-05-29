package io.github.craftqq;

import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public class EventAdapter extends TS3EventAdapter
{
	private static char split = '�';
	public EventAdapter()
	{

	}

	@Override
	public void onTextMessage(TextMessageEvent e)
	{
		String uid = e.getInvokerUniqueId();
		String message = e.getMessage();
		if (message.startsWith("!"))
		{
			String[] parts = message.split("�");
			String command = parts[0];
			if (parts.length == 1)
			{
				//call command with null array
			} else
			{
				//get the parameters, call command with parameters
				String[] parameters = new String[parts.length - 1];
				for (int i = 1; i < parts.length; i++)
				{
					parameters[i - 1] = parts[i];
				}
			}
		}
	}
}
