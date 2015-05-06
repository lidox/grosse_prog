package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Strategie;

/**
 * 
 * @author artur.schaefer
 *
 */
public class InputOutput {
	private String dateiPfad;
	private String ausgabeDateiName;
	private String eingabeInhalt="";
	
	public InputOutput(String dateiPfad, String ausgabeDateiName){
		this.dateiPfad = dateiPfad;
		this.ausgabeDateiName = ausgabeDateiName; 
	}
	
	public String getAusgabeDateiName(){
		return this.ausgabeDateiName;
	}
	
	public String getDateiPfad(){
		return this.dateiPfad;
	}
	
	/**
	 * Methode die das Einlesen der Eingabedateien durchfuehrt.
	 * 
	 * @param dateiPfad
	 *            Datei oder Ordner Pfad fuer die Eingabe
	 * @return Array von eingelesenen Dateien
	 * @throws EingabeValidierungsFehler
	 *             : Fehlermeldungen werden hier als EingabeValidierungsfehler
	 *             ausgegeben.
	 */
	public File[] read(String dateiPfad,FilenameFilter dateiFilter) throws IllegalArgumentException {
		if ((dateiPfad.isEmpty())) {
			throw new IllegalArgumentException("Der Dateipfad '" + dateiPfad
					+ "' konnte nicht geoeffnet werden.");
		}
		File datei = new File(dateiPfad);
		// Es wurde ein Dateipfad übergeben
		if (!(datei.exists())) {
			throw new IllegalArgumentException("Der Dateipfad '" + dateiPfad
					+ "' konnte nicht geoeffnet werden.");
		}
		if (datei.isDirectory()) {
			File[] eingabeDateien = datei.listFiles(dateiFilter);
			return eingabeDateien;
		} else {
			return new File[] { datei };
		}
	}
	
	public List<Strategie> readInput(String datei) throws Exception {
		List<Strategie> lStrategien = new ArrayList<Strategie>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(datei));
			String line;
						
			
			while ((line = br.readLine()) != null) {
				// speichere den Inhalt der Eingabedatei
				this.eingabeInhalt= this.eingabeInhalt + line + "\n";
				if(line.startsWith("%")){
					List<Integer> l = new ArrayList<Integer>();
					String strategieName = line;
					line = br.readLine().trim();
					this.eingabeInhalt= this.eingabeInhalt + line + "\n";
					String[] s = line.split("\t");
					for(String item: s){
						l.add(Integer.parseInt(item));
					}
					lStrategien.add(new Strategie(strategieName,l));
				}
			}
			
			br.close();
			return lStrategien;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Die Datei " +datei + " konnte nicht gelesen werden.");
		}
	}
	
	public void export(File eingabeDatei, List<Strategie> strategieListe) throws Exception{
		// bestimme ausgabe datei name 
		StringBuilder ausgabeDateiName = new StringBuilder(eingabeDatei.getAbsolutePath());
		int pointIndex = eingabeDatei.getAbsolutePath().lastIndexOf(".");
		//String end = null;
		if(pointIndex!=-1){
		    //end  = ausgabeDateiName.substring(pointIndex);
			ausgabeDateiName.delete(pointIndex, eingabeDatei.getAbsolutePath().length());
			
			if(this.ausgabeDateiName!=null){
				ausgabeDateiName.append(this.ausgabeDateiName);
			}
			else{
				ausgabeDateiName.append("-out.txt");
			}
		}
	
		//ausgabe
		StringBuilder a = new StringBuilder();
		a.append(eingabeInhalt+"\n");
		int minIndex = 0;
		
		a.append(strategieListe.get(minIndex));
		for (int i=1;i< strategieListe.size();i++) {
			if(strategieListe.get(minIndex).getBS()>strategieListe.get(i).getBS()){
				minIndex = i;
			}
			a.append(strategieListe.get(i));
		}
		
		a.append("");
        a.append("Die Strategie \"" + strategieListe.get(minIndex).getName() + ("\" ist mit einer Bewertung von " 
		+ Math.round(strategieListe.get(minIndex).getBS()*10000.0)/10000.0).replace(".",",")
		+ " die beste der eingelesenen Strategien und sollte deshalb bei der Terminvergabe gewählt werden.");
		
		FileWriter fw;
		try {
			//z.B. "D:/Users/Artur/Desktop/ausgabe.plt"
			fw = new FileWriter(ausgabeDateiName.toString());

			fw.write(a.toString());

			fw.flush();

			fw.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new Exception("Die Datei " +ausgabeDateiName.toString() + " konnte nicht beschrieben werden.");
		}
	}
}
