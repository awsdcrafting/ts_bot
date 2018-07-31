package io.github.awsdcrafting.config;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.io.File;
import java.io.IOException;
import java.util.List;
/**
 * Created by scisneromam on 12.02.2018.
 */
public class ConfigManager
{
	private String confDirPath;
	private File confDir;
	private Configurations configs = new Configurations();

	public ConfigManager()
	{

		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.replace("\\", "/").lastIndexOf("/"));
		confDirPath = absolutePath + "/" + "DCBotConfig";
		confDir = new File(confDirPath);
		//System.out.println(confDir.getAbsolutePath());
		if (!confDir.isDirectory())
		{
			confDir.delete();
		}
		confDir.mkdirs();

	}

	public Configuration getConfig(String guildID, String filePath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		return configs.properties(new File(confDirPath + "/" + guildID + "/" + filePath));
	}

	public boolean configHasKey(String guildID, String filePath, String settingPath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		Configuration config = configs.properties(new File(confDirPath + "/" + guildID + "/" + filePath));
		return config.containsKey(settingPath);
	}

	public String getConfDirPath()
	{
		return confDirPath;
	}

	public String getStringConfigSetting(String guildID, String filePath, String settingPath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		Configuration config = configs.properties(new File(confDirPath + "/" + guildID + "/" + filePath));
		return config.getString(settingPath);
	}

	public String getStringConfigSetting(long guildID, String filePath, String settingPath) throws ConfigurationException
	{
		return getStringConfigSetting(guildID + "", filePath, settingPath);
	}

	public int getIntConfigSetting(String guildID, String filePath, String settingPath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		Configuration config = configs.properties(new File(confDirPath + "/" + guildID + "/" + filePath));
		return config.getInt(settingPath);
	}

	public int getIntConfigSetting(long guildID, String filePath, String settingPath) throws ConfigurationException
	{
		return getIntConfigSetting(guildID + "", filePath, settingPath);
	}

	public long getLongConfigSetting(String guildID, String filePath, String settingPath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		Configuration config = configs.properties(new File(confDirPath + "/" + guildID + "/" + filePath));
		return config.getLong(settingPath);
	}

	public long getLongConfigSetting(long guildID, String filePath, String settingPath) throws ConfigurationException
	{
		return getLongConfigSetting(guildID + "", filePath, settingPath);
	}

	public double getDoubleConfigSetting(String guildID, String filePath, String settingPath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		Configuration config = configs.properties(new File(confDirPath + "/" + guildID + "/" + filePath));
		return config.getDouble(settingPath);
	}

	public double getDoubleConfigSetting(long guildID, String filePath, String settingPath) throws ConfigurationException
	{
		return getDoubleConfigSetting(guildID + "", filePath, settingPath);
	}

	public List<Object> getListConfigSetting(String guildID, String filePath, String settingPath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		Configuration config = configs.properties(new File(confDirPath + "/" + guildID + "/" + filePath));
		return config.getList(settingPath);
	}

	public List<Object> getListConfigSetting(long guildID, String filePath, String settingPath) throws ConfigurationException
	{
		return getListConfigSetting(guildID + "", filePath, settingPath);
	}

	public void setConfigSetting(String guildID, String filePath, String settingPath, Object value) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder(checkFile(confDirPath + "/" + guildID + "/" + filePath));
		Configuration config = builder.getConfiguration();
		config.setProperty(settingPath, value);
		builder.save();
	}

	public void setConfigSetting(long guildID, String filePath, String settingPath, Object value) throws ConfigurationException
	{
		setConfigSetting(guildID + "", filePath, settingPath, value);
	}

	public void addToConfigSetting(String guildID, String filePath, String settingPath, Object value) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder(checkFile(confDirPath + "/" + guildID + "/" + filePath));
		Configuration config = builder.getConfiguration();
		config.addProperty(settingPath, value);
		builder.save();
	}

	public void addToConfigSetting(long guildID, String filePath, String settingPath, Object value) throws ConfigurationException
	{
		addToConfigSetting(guildID + "", filePath, settingPath, value);
	}

	public void clearConfigSetting(String guildID, String filePath, String settingPath) throws ConfigurationException
	{
		if (!filePath.endsWith(".properties"))
		{
			filePath += ".properties";
		}
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder(checkFile(confDirPath + "/" + guildID + "/" + filePath));
		Configuration config = builder.getConfiguration();
		config.clearProperty(settingPath);
		builder.save();
	}

	public void clearConfigSetting(long guildID, String filePath, String settingPath) throws ConfigurationException
	{
		clearConfigSetting(guildID + "", filePath, settingPath);
	}

	public void removeFromConfigSetting(String guildID, String filePath, String settingPath, Object value) throws ConfigurationException
	{
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder = configs.propertiesBuilder(checkFile(confDirPath + "/" + guildID + "/" + filePath));
		Configuration config = builder.getConfiguration();
		List<Object> list = config.getList(settingPath);
		list.remove(value);
		config.setProperty(settingPath, list);
		builder.save();
	}

	public void removeFromConfigSetting(long guildID, String filePath, String settingPath, Object value) throws ConfigurationException
	{
		removeFromConfigSetting(guildID + "", filePath, settingPath, value);
	}

	private File checkFile(String path)
	{
		if (!path.endsWith(".properties"))
		{
			path += ".properties";
		}

		File file = new File(path);
		if (!file.exists())
		{
			if (!file.getParentFile().exists())
			{
				file.getParentFile().mkdirs();
			}
			try
			{
				file.createNewFile();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return file;

	}

}
