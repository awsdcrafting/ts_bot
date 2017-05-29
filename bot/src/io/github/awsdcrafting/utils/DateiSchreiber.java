package io.github.awsdcrafting.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DateiSchreiber
{

	public static void schreibeArrayList(ArrayList AL, String dateiName)
	{
		// File anlegen
		try
		{
			// new FileWriter(file ,true) - falls die Datei bereits existiert
			// werden die Bytes an das Ende der Datei geschrieben

			// new FileWriter(file) - falls die Datei bereits existiert
			// wird diese �berschrieben
			File file = new File(dateiName);
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);

			// Text wird in den Stream geschrieben
			for (int i = 0; i < AL.size(); i++)
			{
				fw.write("" + AL.get(i));
				// Platformunabhaengiger Zeilenumbruch wird in den Stream
				// geschrieben
				fw.write(System.getProperty("line.separator"));
			}

			// Schreibt den Stream in die Datei
			// Sollte immer am Ende ausgef�hrt werden, sodass der Stream
			// leer ist und alles in der Datei steht.
			fw.flush();

			// Schlie�t den Stream
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void schreibeArrayList(ArrayList AL, File file)
	{
		// File anlegen
		try
		{
			// new FileWriter(file ,true) - falls die Datei bereits existiert
			// werden die Bytes an das Ende der Datei geschrieben

			// new FileWriter(file) - falls die Datei bereits existiert
			// wird diese �berschrieben
			if (!file.exists())
			{
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);

			// Text wird in den Stream geschrieben
			for (int i = 0; i < AL.size(); i++)
			{
				fw.write("" + AL.get(i));
				// Platformunabh�ngiger Zeilenumbruch wird in den Stream
				// geschrieben
				fw.write(System.getProperty("line.separator"));
			}

			// Schreibt den Stream in die Datei
			// Sollte immer am Ende ausgef�hrt werden, sodass der Stream
			// leer ist und alles in der Datei steht.
			fw.flush();

			// Schlie�t den Stream
			fw.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
