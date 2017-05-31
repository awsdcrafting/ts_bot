package io.github.awsdcrafting.configSystem;

import java.util.ArrayList;
public class DefaultConfig
{
	ArrayList<String> config;
	public DefaultConfig()
	{
		config = new ArrayList<String>();
	}

	public ArrayList<String> setConfig(){
		ArrayList<String> defaultConfig = new ArrayList<String>();
		defaultConfig.add("#Default Config");
		defaultConfig.add("");
		defaultConfig.add("#admin Groups separated by a comma");
		defaultConfig.add("admin_groups=0");

		// end
		config = defaultConfig;
		return config;
	}
}
