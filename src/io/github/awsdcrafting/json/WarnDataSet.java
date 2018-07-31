package io.github.awsdcrafting.json;
import java.util.List;
/**
 * Project: ts_bot
 * Created by scisneromam on 30.07.2018.
 *
 * @author scisneromam
 * ---------------------------------------------------------------------
 * Copyright © 2018 | scisneromam | All rights reserved.
 */
public class WarnDataSet
{
	public List<WarnData> warnDataList;

	public WarnDataSet(List<WarnData> warnDataList)
	{
		this.warnDataList = warnDataList;
	}

	public List<WarnData> getWarnDataList()
	{
		return warnDataList;
	}

	public void setWarnDataList(List<WarnData> warnDataList)
	{
		this.warnDataList = warnDataList;
	}
}
