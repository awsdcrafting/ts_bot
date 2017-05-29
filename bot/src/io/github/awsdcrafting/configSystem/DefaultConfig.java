package io.github.awsdcrafting.configSystem;

import java.util.Properties;

public class DefaultConfig extends Properties
{

	public static final DefaultConfig defaultConfig = new DefaultConfig();

	private DefaultConfig()
	{
		put("admin_groups", "0");
		put("mod_groups", "0");
	}

}
