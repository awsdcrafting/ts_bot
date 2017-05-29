package io.github.awsdcrafting.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class DateiLeser
{

	public static ArrayList leseDateiAsArrayList(String dateiName)
	{
		ArrayList<String> al = new ArrayList<String>();
		try
		{
			File file = new File(dateiName);
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			while (s != null)
			{
				al.add(s);
				s = br.readLine();
			}
			br.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return al;
	}

	public static ArrayList leseDateiAsArrayList(File file)
	{
		ArrayList<String> al = new ArrayList<String>();
		try
		{
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s = br.readLine();
			while (s != null)
			{
				al.add(s);
				s = br.readLine();
			}
			br.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return al;
	}
}
