package io.github.awsdcrafting.configSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import io.github.awsdcrafting.utils.DateiLeser;
import io.github.awsdcrafting.utils.DateiSchreiber;

public class ConfigManager
{
	private File defaultConfigFile;
	private ArrayList<String> config = new ArrayList<String>();
	private ArrayList<String> defaultConfig = DateiLeser.leseDateiAsArrayList("defaultConfig.txt");
	
	
	
	public ConfigManager()
	{
		defaultConfigFile = new File("Config/Config.cfg");

	}
	
	public ArrayList<String> getConfig()
	{
		return config;
	}

	public void load()
	{
		load(defaultConfigFile);
	}

	public void load(String path)
	{
		load(new File(path));
	}

	public void load(File file)
	{
		ArrayList<String> config = DateiLeser.leseDateiAsArrayList(file);
		for(int i = 0;i<config.size();i++){
			if(config.get(i).startsWith("#")||config.get(i).equals("")){
				continue;
			}
			this.config.add(config.get(i));

		}
	}
	
	public void save()
	{
		save(defaultConfigFile);
	}
	
	public void save(String path)
	{
		save(new File(path));
	}
	
	public void save(File file)
	{
		DateiSchreiber.schreibeArrayList(config, file);
	}
	
	
}
