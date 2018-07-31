package io.github.awsdcrafting.utils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
/**
 * Created by scisneromam on 24.04.2018.
 */
public class Logger
{

	private int maxLoggers = 20;
	private HashMap<String, PrintWriter> printWriterHashMap = new HashMap<>();
	private HashMap<PrintWriter, String> pathHashMap = new HashMap<>();
	private HashMap<PrintWriter, LocalDateTime> printWriterLocalDateTimeHashMap = new HashMap<>();

	private PrintWriter openWriter(String filePath)
	{

		if (printWriterHashMap.get(filePath) == null)
		{

			try
			{
				File file = new File(filePath.substring(0, filePath.replace("\\", "/").lastIndexOf("/")));
				if (!file.exists())
				{
					file.mkdirs();
					file.createNewFile();
				}
				System.out.println("Opening new writer for " + filePath);
				FileOutputStream fos = new FileOutputStream(filePath, true);
				OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
				BufferedWriter bw = new BufferedWriter(osw);
				PrintWriter out = new PrintWriter(bw, true);
				printWriterHashMap.put(filePath, out);
				pathHashMap.put(out, filePath);
				if (printWriterHashMap.size() > maxLoggers)
				{
					LocalDateTime oldest = null;
					PrintWriter toRemove = null;
					String toRemove2 = null;
					for (PrintWriter key : printWriterLocalDateTimeHashMap.keySet())
					{
						if (oldest == null)
						{
							oldest = printWriterLocalDateTimeHashMap.get(key);
							toRemove = key;
							toRemove2 = pathHashMap.get(key);
						} else
						{
							if (printWriterLocalDateTimeHashMap.get(key).isBefore(oldest))
							{
								oldest = printWriterLocalDateTimeHashMap.get(key);
								toRemove = key;
								toRemove2 = pathHashMap.get(key);
							}
						}
					}

					printWriterLocalDateTimeHashMap.remove(toRemove);
					printWriterHashMap.remove(pathHashMap.get(toRemove));
					printWriterHashMap.remove(toRemove2);
				}
				printWriterLocalDateTimeHashMap.put(out, LocalDateTime.now());
				return out;
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		} else
		{
			if (printWriterHashMap.size() > maxLoggers)
			{
				LocalDateTime oldest = null;
				PrintWriter toRemove = null;
				String toRemove2 = null;
				for (PrintWriter key : printWriterLocalDateTimeHashMap.keySet())
				{
					if (oldest == null)
					{
						oldest = printWriterLocalDateTimeHashMap.get(key);
						toRemove = key;
						toRemove2 = pathHashMap.get(key);
					} else
					{
						if (printWriterLocalDateTimeHashMap.get(key).isBefore(oldest))
						{
							oldest = printWriterLocalDateTimeHashMap.get(key);
							toRemove = key;
							toRemove2 = pathHashMap.get(key);
						}
					}
				}

				if (toRemove != null)
				{
					toRemove.close();
					printWriterLocalDateTimeHashMap.remove(toRemove);
					printWriterHashMap.remove(pathHashMap.get(toRemove));
					printWriterHashMap.remove(toRemove2);
				}
			}
			printWriterLocalDateTimeHashMap.put(printWriterHashMap.get(filePath), LocalDateTime.now());
			return printWriterHashMap.get(filePath);
		}
		return null;
	}

	public void logWithLog4J(Class clazz, String msg)
	{
		LogManager.getLogger(clazz).log(Level.INFO, msg);
	}

	public void log(Class clazz, String filePath, String msg)
	{

		if (!filePath.endsWith(".log"))
		{
			filePath += ".log";
		}
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.replace("\\", "/").lastIndexOf("/"));

		File file = new File(absolutePath + "/log/" + filePath);
		//System.out.println(file.getAbsolutePath());

		filePath = file.getAbsolutePath();

		//System.out.println(filePath);

		PrintWriter writer = doStuff(0, filePath);
		if (writer != null)
		{
			String outString = LocalDateTime.now().toString() + " " + clazz.getName() + " - " + msg;
			writer.println(outString);
		} else
		{
			System.out.println("null");
		}

	}

	/**
	 * @param mode     The mode to operate on: 0 = open, 1 = close, 2 = close all
	 * @param filePath The FilePath only needed for mode 0 and 1
	 * @return Return the Opened PrintWriter for Mode 0, else return null
	 */
	public synchronized PrintWriter doStuff(int mode, String filePath)
	{
		switch (mode)
		{
		case 0:
			return openWriter(filePath);
		case 1:
			closeWriter(filePath);
			return null;
		case 2:
			closeAllWriters();
			return null;
		default:
			return null;
		}
	}

	private void closeWriter(String filePath)
	{
		String absolutePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		absolutePath = absolutePath.substring(0, absolutePath.replace("\\", "/").lastIndexOf("/"));

		filePath = absolutePath + "/log/" + filePath;
		if (!filePath.endsWith(".log"))
		{
			filePath += ".log";
		}
		System.out.println("closing writer for " + filePath);

		PrintWriter writer = printWriterHashMap.get(filePath);
		if (writer != null)
		{
			writer.close();
			printWriterHashMap.remove(filePath);
			printWriterLocalDateTimeHashMap.remove(writer);
			pathHashMap.remove(writer);
		}
	}

	private void closeAllWriters()
	{
		printWriterHashMap.forEach((path, writer) -> {
			if (writer != null)
			{
				writer.close();
			}
			printWriterHashMap.remove(path);
			printWriterLocalDateTimeHashMap.remove(writer);
			pathHashMap.remove(writer);
		});
	}

}
