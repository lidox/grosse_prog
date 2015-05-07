package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.Strategie;

/**
 * liest dateien ein und speichert die ergebnisse in neuen dateien ab
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
	
	/**
	 * Liest die eingabe datei aus und prueft diese auf fehler
	 * @param datei
	 * 		die einzulesende datei
	 * @return
	 * 		eine liste aller strategien, die in der eingabedatei vorhanden ist
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public List<Strategie> readInput(String datei) throws Exception {
		List<Strategie> lStrategien = new ArrayList<Strategie>();
		
			BufferedReader br = new BufferedReader(new FileReader(datei));
			String line;
						
			boolean firstIteration = true;
			while ((line = br.readLine()) != null ) {
				// speichere den Inhalt der Eingabedatei
				this.eingabeInhalt= this.eingabeInhalt + line + "\n";
				String secondLine = br.readLine();
				
				validateInput(line,secondLine);
				
				line = line.trim();
				List<Integer> l = new ArrayList<Integer>();
				String strategieName = line;
				this.eingabeInhalt= this.eingabeInhalt + secondLine + "\n";
				secondLine = secondLine.trim();
				String[] s = secondLine.split("\t");
			    for(String item: s){
					l.add(Integer.parseInt(item));
				}
			    firstIteration = false;
				lStrategien.add(new Strategie(strategieName,l));
			}
			if(firstIteration && line==null){
				throw new IllegalArgumentException("Fehlerhafte Eingabe! Die Datei darf nicht leer sein");
			}
			br.close();
			return lStrategien;

	}
	
	/**
	 * prueft jeweils zwei untereinander liegende zeilen der eingabedatei auf eingabefehler
	 * @param line
	 * 		jeweils erste zeile
	 * @param secondLine
	 * 		jeweils zweite zeile
	 * @return
	 * 		true wenn die eingabe fehlerfrei ist, sonst exception
	 * @throws Exception
	 */
	private boolean validateInput(String firstLine, String secondLine) throws Exception {
		if(firstLine == null || firstLine.equals("")){
			throw new Exception("Fehlerhafte Eingabe! Die  Zeile '"+ firstLine+"' darf nicht leer sein.");
		}
		if(!firstLine.startsWith("%")){
			throw new Exception("Fehlerhafte Eingabe! Die  Zeile '"+ firstLine+"' muss mit einem '%' anfangen.");
		}
		if(secondLine == null || secondLine.equals("")){
			throw new Exception("Es fehlt eine Zeile nach '"+ firstLine+ "'.");
		}
		secondLine = secondLine.trim();
		if(secondLine.startsWith("%")){
			throw new Exception("Fehlerhafte Eingabe! Die  Zeile '"+ secondLine+"' darf nicht mit einem '%' anfangen.");
		}
		if(!secondLine.contains("\t")){
			throw new Exception("Fehlerhafte Eingabe! In der  Zeile '"+ secondLine+"' müssen die Werte mit einem 'TAB' getrennt sein.");
		}
		String[] werte = null;
		if((werte = secondLine.split("\t")).length!=3){
			throw new Exception("Fehlerhafte Eingabe! In der  Zeile '"+ secondLine+"' müssen genau drei Werte jeweils mit einem 'TAB' getrennt sein. "
					+"Es konnte nur "+Arrays.toString(werte)+" eingelesen werden.");
		}
		for (String strategieWert : werte) {
			int wert;
			try {
				wert =Integer.parseInt(strategieWert);
			} catch (Exception e) {
				throw new Exception("Fehlerhafte Eingabe! In der  Zeile '"+ secondLine+"' müssen alle Werte ganze Zahlen sein.");
			}
			if(wert<15 || wert>30 ){
				throw new Exception("Fehlerhafte Eingabe! In der  Zeile '"+ secondLine+"' müssen alle Werte größer als 14 und kleiner als 31 sein.");
			}
		}
		return true;
	}

	/**
	 * Erstellt eine neue ausgabedatei mit ausgabetext 
	 * @param eingabeDatei
	 * 			datei, die eingelesen wurde
	 * @param strategieListe
	 * 			liste mit allen strategien, die in der eingabedatei vorhanden waren
	 * @throws Exception
	 */
	public void exportData(File eingabeDatei, String ausgabe){
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
		
        // schreibe in die datei rein
		FileWriter fw;
		try {
			//z.B. "D:/Users/Artur/Desktop/ausgabe.plt"
			fw = new FileWriter(ausgabeDateiName.toString());

			fw.write(ausgabe);

			fw.flush();

			fw.close();

		} catch (IOException e) {
			System.out.println(e.getMessage());
			throw new IllegalArgumentException("Die Datei " +ausgabeDateiName.toString() + " konnte nicht beschrieben werden.");
		}
	}

	public StringBuilder getErgebnisse(List<Strategie> strategieListe) {
		//ausgabe
		StringBuilder ausgabe = new StringBuilder();
		ausgabe.append(eingabeInhalt+"\n");
		int minIndex = 0;
		
		ausgabe.append(strategieListe.get(minIndex));
		for (int i=1;i< strategieListe.size();i++) {
			if(strategieListe.get(minIndex).getBS()>strategieListe.get(i).getBS()){
				minIndex = i;
			}
			ausgabe.append(strategieListe.get(i));
		}
		
		ausgabe.append("");
        ausgabe.append("Die Strategie \"" + strategieListe.get(minIndex).getName() + ("\" ist mit einer Bewertung von " 
		+ Math.round(strategieListe.get(minIndex).getBS()*10000.0)/10000.0).replace(".",",")
		+ " die beste der eingelesenen Strategien und sollte deshalb bei der Terminvergabe gewählt werden.");
		return ausgabe;
	}
}
