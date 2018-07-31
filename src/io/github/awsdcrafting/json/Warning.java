package io.github.awsdcrafting.json;
/**
 * Project: ts_bot
 * Created by scisneromam on 30.07.2018.
 *
 * @author scisneromam
 * ---------------------------------------------------------------------
 * Copyright © 2018 | scisneromam | All rights reserved.
 */
public class Warning
{
	public String reason;
	public int level;

	public Warning(String reason, int level)
	{
		this.reason = reason;
		this.level = level;
	}

	public String getReason()
	{
		return reason;
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}

	public int getLevel()
	{
		return level;
	}

	public void setLevel(int level)
	{
		this.level = level;
	}

	@Override
	public String toString()
	{
		return "Warning{" + "reason='" + reason + "'" + ", level=" + level + "}";
	}
}
