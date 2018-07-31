package io.github.awsdcrafting.json;
import java.util.ArrayList;
import java.util.List;
/**
 * Project: ts_bot
 * Created by scisneromam on 30.07.2018.
 *
 * @author scisneromam
 * ---------------------------------------------------------------------
 * Copyright © 2018 | scisneromam | All rights reserved.
 */
public class WarnData
{
	public String uid;
	public int warnings;
	public int warnLevel;
	public List<Warning> warningList;

	public WarnData(String uid)
	{
		this.uid = uid;
		this.warnings = 0;
		this.warnLevel = 0;
		this.warningList = new ArrayList<>();
	}

	public String getUid()
	{
		return uid;
	}

	public void setUid(String uid)
	{
		this.uid = uid;
	}

	public int getWarnings()
	{
		return warnings;
	}

	public void setWarnings(int warnings)
	{
		this.warnings = warnings;
	}

	public int getWarnLevel()
	{
		return warnLevel;
	}

	public void setWarnLevel(int warnLevel)
	{
		this.warnLevel = warnLevel;
	}
	public List<Warning> getWarningList()
	{
		return warningList;
	}

	public void setWarningList(List<Warning> warningList)
	{
		this.warningList = warningList;
	}

	@Override
	public String toString()
	{
		return "WarnData{" + "uid='" + uid + '\'' + ", warnings=" + warnings + ", warningList=" + warningList + '}';
	}
}
