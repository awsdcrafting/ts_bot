package io.github.awsdcrafting;
import java.util.ArrayList;
import java.util.Date;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

public class WarnSystem {
	
	  public static void SchreibeWarnung(String name,int anzahl_Warnungen){
		    // File anlegen
		     try {
		       // new FileWriter(file ,true) - falls die Datei bereits existiert
		       // werden die Bytes an das Ende der Datei geschrieben
		       
		       // new FileWriter(file) - falls die Datei bereits existiert
		       // wird diese überschrieben
		      FileWriter fw = new FileWriter("Warnungen.txt");
		       
		       // Text wird in den Stream geschrieben
		       fw.write(name + " " + anzahl_Warnungen);
		       
		       // Platformunabhängiger Zeilenumbruch wird in den Stream geschrieben
		       fw.write(System.getProperty("line.separator"));
		       
		       // Schreibt den Stream in die Datei
		       // Sollte immer am Ende ausgeführt werden, sodass der Stream 
		       // leer ist und alles in der Datei steht.
		       fw.flush();
		       
		       // Schließt den Stream
		       fw.close();
		    } catch (IOException e) {
		      e.printStackTrace();
		    }
		  }
	  
	  public static String[] leseWarnungen()
	  {
	      ArrayList<String> al = new ArrayList<String>();
			try
			{
				FileReader fr = new FileReader("Warnungen.txt");
				BufferedReader br = new BufferedReader(fr);
				String s = br.readLine();
				while(s != null)
				{
					al.add(s);
					s = br.readLine();
				}
				br.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			return al.toArray(new String[0]);
	    }
	  
	  public static int getAnzahlWarnungen(String name){
		  String[] alleWarnungen = leseWarnungen();
		  for(int i = 0;i<=alleWarnungen.length;i++){
			 String[] Warnung = alleWarnungen[i].split(" ");
			 if(Warnung[0].equals(name)){
				return Integer.parseInt(Warnung[1]);
			 }
			 }
		  
		  return -1;
	  }
	  
}
