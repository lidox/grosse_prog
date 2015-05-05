package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import model.Strategie;

public class InputOutput {
	private String dateiPfad;
	
	public InputOutput(String dateiPfad){
		this.dateiPfad = dateiPfad;
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
	
	public List<Strategie> readInput(String datei) {
		List<Strategie> lStrategien = new ArrayList<Strategie>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(datei));
			String line;
						
			
			while ((line = br.readLine()) != null) {
				if(line.startsWith("%")){
					List<Integer> l = new ArrayList<Integer>();
					String strategieName = line;
					line = br.readLine().trim();
					String[] s = line.split("\t");
					System.out.println(Arrays.toString(s));
					for(String item: s){
						l.add(Integer.parseInt(item));
					}
					lStrategien.add(new Strategie(strategieName,l));
				}
				System.out.println("line:"+line);
			}
			
			br.close();
			return lStrategien;
		} catch (Exception e) {
			Ausgabe.exportFehler(e.getMessage(), datei);
		}
		return null;
	}
}
