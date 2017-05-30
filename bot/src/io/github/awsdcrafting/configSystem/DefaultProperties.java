package io.github.awsdcrafting.configSystem;

import java.util.Properties;

public class DefaultProperties extends Properties
{

	public static final DefaultProperties defaultConfig = new DefaultProperties();

	private DefaultProperties()
	{
		put("admin_groups", "0");
		put("mod_groups", "0");
	}

	public void test()
	{

	}

}
