package io.github.awsdcrafting.poll;
import io.github.awsdcrafting.utils.DateiLeser;
import io.github.awsdcrafting.utils.DateiSchreiber;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by scisneromam on 21.08.2017.
 */
public class PollManager
{
	private File pollListFile;
	private HashMap<String, Umfrage> pollMap;

	public PollManager()
	{
		setup();
	}

	public void setup()
	{
		pollListFile = new File("Poll/polls.txt");
		ArrayList<String> pollList = DateiLeser.leseDateiAsArrayList(pollListFile);

	}

	public String createPoll(String name, String frage, String[] antworten)
	{
		String oldName = name;
		int id = 1;
		while (doesNameExist(name))
		{
			name = oldName + id;
			id++;
		}
		for (int i = 0; i < antworten.length; i++)
		{
			antworten[i] = antworten[i] + "|:|" + "0";
		}
		Umfrage umfrage = new Umfrage(name, frage, antworten);
		pollMap.put(umfrage.getName().toLowerCase(), umfrage);
		ArrayList<String> umfragenWerte = new ArrayList<>();
		umfragenWerte.add(name);
		umfragenWerte.add("false");
		umfragenWerte.add(frage);
		for (int i = 0; i < antworten.length; i++)
		{
			umfragenWerte.add(antworten[i]);
		}
		DateiSchreiber.schreibeArrayList(umfragenWerte, umfrage.getName().toLowerCase());
		return name;
	}

	public boolean doesNameExist(String name)
	{
		if (pollMap.containsKey(name.toLowerCase()))
		{
			return true;
		}
		return false;
	}

}
