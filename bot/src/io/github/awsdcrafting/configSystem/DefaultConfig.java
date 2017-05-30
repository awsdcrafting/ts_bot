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
		config.add("#Default Config");
		config.add("");
		config.add("#admin Groups separated by a comma");
		config.add("admin_groups=0");

		// end
		return config;
	}
}
