package io.github.awsdcrafting.commands.impl;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.google.gson.Gson;
import io.github.awsdcrafting.commands.Command;
import io.github.awsdcrafting.json.WarnData;
import io.github.awsdcrafting.json.WarnDataSet;
import io.github.awsdcrafting.json.Warning;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
/**
 * Created by scisneromam on 04.07.2018.
 */
public class Warn extends Command
{
	public Warn()
	{
		super("Warn", "warn a user or get all warnings", "!warn <<<add> <name|uid> [level] <reason>> | <<list> <name|uid>>>", new String[]{}, 100);
	}
	@Override
	public void execute(TS3Api api, TextMessageEvent e, String[] args)
	{

		if (args.length == 0)
		{
			api.sendPrivateMessage(e.getInvokerId(), syntax);
		} else
		{
			if (args[0].equalsIgnoreCase("add"))
			{
				if (args.length <= 2)
				{
					api.sendPrivateMessage(e.getInvokerId(), "!warn add <name|uid> [level] <reason>");
				} else
				{
					int level = 1;
					int start = 2;
					if (args.length >= 4)
					{
						try
						{
							level = Integer.parseInt(args[2]);
							start++;
							if (level == 0)
							{
								level = 1;
							}
						} catch (NumberFormatException ignored)
						{

						}
					}

					StringBuilder stringBuilder = new StringBuilder();
					for (int i = start; i < args.length; i++)
					{
						stringBuilder.append(args[i]).append(" ");
					}
					String reason = stringBuilder.toString().trim();

					Client client = api.getClientByNameExact(name, true);
					if (client == null)
					{
						try
						{
							client = api.getClientByUId(args[1]);
						} catch (TS3CommandFailedException e1)
						{
							api.sendPrivateMessage(e.getInvokerId(), "Couldnt find client by " + args[1]);
						}
					}
					if (client != null)
					{
						String uid = client.getUniqueIdentifier();
						try
						{
							WarnData warnData = readWarnDataFromJson(uid);
							int oldLevel = warnData.warnLevel;
							if (oldLevel == 0)
							{
								api.kickClientFromServer(reason, client.getId());
							} else
							{
								api.banClient(client.getId(), (long) Math.pow(2, oldLevel) * 3600, reason);
							}

							warnData.warnLevel += level;
							warnData.warnings++;
							warnData.warningList.add(new Warning(reason, level));

						} catch (IOException e1)
						{
							api.sendPrivateMessage(e.getInvokerId(), "An error occurred writing the warning");
							e1.printStackTrace();
						}
					} else
					{
						api.sendPrivateMessage(e.getInvokerId(), "Couldnt find client by " + args[1]);
					}

				}
			} else if (args[0].equalsIgnoreCase("list"))
			{
				if (args.length == 1)
				{
					api.sendPrivateMessage(e.getInvokerId(), "!warn list <name|uid>");
				} else
				{
					Client client = api.getClientByNameExact(name, true);
					if (client == null)
					{
						try
						{
							client = api.getClientByUId(args[1]);
						} catch (TS3CommandFailedException e1)
						{
							api.sendPrivateMessage(e.getInvokerId(), "Couldnt find client by " + args[1]);
						}
					}
					if (client != null)
					{
						String uid = client.getUniqueIdentifier();
						try
						{
							WarnData warnData = readWarnDataFromJson(uid);
							String output = "\n";

							output += "Name: " + client.getNickname() + "\n";
							output += "UID: " + warnData.getUid() + "\n";
							output += "Warnings: " + warnData.getWarnings() + "\n";
							output += "WarnLevel: " + warnData.getWarnLevel() + "\n";
							output += "All Warnings:\n";
							output += String.join("\n", warnData.warningList.stream().map(Warning::toString).collect(Collectors.toList()));
							api.sendPrivateMessage(e.getInvokerId(), output);

						} catch (FileNotFoundException e1)
						{
							api.sendPrivateMessage(e.getInvokerId(), "An error occurred reading the warnings");
							e1.printStackTrace();
						}
					} else
					{
						api.sendPrivateMessage(e.getInvokerId(), "Couldnt find client by " + args[1]);
					}
				}

			} else
			{
				api.sendPrivateMessage(e.getInvokerId(), syntax);
			}
		}

	}

	private void writeWarningToJson(Warning warning, String uuid) throws IOException
	{

		File warningFile = getWarningFile();
		WarnDataSet warnDataSet;
		Gson gson = new Gson();
		if (!warningFile.exists())
		{
			warnDataSet = new WarnDataSet(new ArrayList<>());
		} else
		{
			warnDataSet = gson.fromJson(new FileReader(warningFile), WarnDataSet.class);
		}

		boolean warnDataExisted = false;
		for (WarnData warnData : warnDataSet.warnDataList)
		{
			if (warnData.uid.equals(uuid))
			{
				warnData.warningList.add(warning);
				warnData.warnings++;
				warnDataExisted = true;
				break;
			}
		}
		if (!warnDataExisted)
		{
			WarnData warnData = new WarnData(uuid);
			warnData.warningList.add(warning);
			warnData.warnings++;
			warnData.warnLevel += warning.level;
			warnDataSet.warnDataList.add(warnData);
		}

		String toWrite = gson.toJson(warnDataSet);

		FileWriter writer = new FileWriter(warningFile);
		writer.write(toWrite);
	}

	private WarnData readWarnDataFromJson(String uuid) throws FileNotFoundException
	{
		File warningFile = getWarningFile();
		if (!warningFile.exists())
		{
			return new WarnData(uuid);
		}

		Gson gson = new Gson();
		WarnDataSet warnDataSet = gson.fromJson(new FileReader(warningFile), WarnDataSet.class);

		for (WarnData warnData : warnDataSet.warnDataList)
		{
			if (warnData.uid.equals(uuid))
			{
				return warnData;
			}
		}
		return new WarnData(uuid);
	}

	private String getPath()
	{
		String[] pathArgs = System.getProperty("java.class.path").split("/");
		StringBuilder path = new StringBuilder();
		for (int i = 0; i < pathArgs.length - 1; i++)
		{
			path.append(pathArgs[i]).append("/");
		}
		return path.toString();

	}

	private File getWarningFile()
	{
		String path = getPath();

		return new File(path, "warns.json");
	}

	@Override
	public String help()
	{
		return null;
	}

}
