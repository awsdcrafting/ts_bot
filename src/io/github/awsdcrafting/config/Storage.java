package io.github.awsdcrafting.config;
/**
 * Created by scisneromam on 12.02.2018.
 * Stores some Classes
 */
public class Storage
{

	private static Storage instance;

	public static Storage getInstance()
	{
		if (instance == null)
		{
			instance = new Storage();
		}
		return instance;
	}

	private ConfigManager configManager;

	public ConfigManager getConfigManager()
	{
		return configManager;
	}

	private String botOwnerUUID = "PVvMDq6HTgvEP/0lhRMKPhVNyPY=";

	public String getBotOwnerUUID()
	{
		return botOwnerUUID;
	}
	public void setBotOwnerUUID(String botOwnerUUID)
	{
		this.botOwnerUUID = botOwnerUUID;
	}

}
