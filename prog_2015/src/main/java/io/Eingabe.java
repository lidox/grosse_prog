package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Baum;
import model.Feld;

public class Eingabe {
	
	private String dateiPfad;
	
	public Eingabe(String dateiPfad){
		this.dateiPfad = dateiPfad;
	}
	
	public String getDateiPfad(){
		return this.dateiPfad;
	}
	
	public boolean userInput = false;
	public HashMap<String, Integer> treeIndexMap = new HashMap<String, Integer>();
	private int index = -1;
	
	public int getIndex(String bezeichung){
		String b = bezeichung.toLowerCase();
		if(treeIndexMap.containsKey(b)){
			throw new IllegalArgumentException();
		}
		index++;
		treeIndexMap.put(b, this.index);
		return treeIndexMap.get(b);
	}
	
	private String isCorrectDesignation(String s){
		String line = s.trim();
		if(line.contains("%")){
			int index = line.indexOf("%");
			line=line.substring(0,index);
			line=line.trim();
		}
		if(line.equals("")){
			throw new IllegalArgumentException("Fehlerhafte Eingabedatei! Die erste Zeile darf nicht leer sein.");
		}
		return line;
	}
	
	private double[] isCorrectField(String s){
		try {
		String line = s.trim();
		if(line.contains("%")){
			int index = line.indexOf("%");
			line=line.substring(0,index);
			line=line.trim();
		}
		
		if(!line.contains(" ")){
			throw new IllegalArgumentException("Fehlerhafte Eingabedatei! Angabe der Feldgroese muss eine Leerstelle beinhalten.");
		}
		else{
			String[] f = line.split(" ");
			if(f.length!=2){
				throw new IllegalArgumentException("Fehlerhafte Eingabedatei! Angabe der Feldgroese muss genau eine Laenge und Breite haben.");
			}
			double a = Double.parseDouble(f[0]);
			double b = Double.parseDouble(f[1]);
			if(a<1 || b<1 ){
				throw new IllegalArgumentException("Fehlerhafte Eingabedatei! X und Y Wert fuer die Feldgroesse muss groesser 1 sein");
			}
			if(a>10000 || b>10000 ){
				throw new IllegalArgumentException("Fehlerhafte Eingabedatei! X und Y Wert fuer die Feldgroesse sollte nicht groesser als 10.000 sein.");
			}
			double[] feldgroesse ={a, b};
			return feldgroesse;
		}

		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Fehlerhafte Eingabedatei! Angabe der Feldgroese muss in natürlichen Zahlenwerten erfolegen.");
		}
	}
	
	private Baum isCorrectTreeSet(String s,int index, Feld feld){
		String line = s.trim();
		double radius =-1;
		String bezeichnung="empty";
		if(!line.contains("%")){
			//line= line.substring(line.indexOf("%"));
			throw new IllegalArgumentException("Fehlerhafte Eingabedatei! Der Baum mit r="+line + " hat keine Bezeichnung!");
		}
		if(!line.contains("\t")){
			throw new IllegalArgumentException("Fehlerhafte Eingabedatei! Der Baum in Zeile:"+line + " hat keinen TAB!");
		}
		try {
			String[] f = line.split("\t");
			radius = Double.parseDouble(f[0]);
			if(feld.getBreite()/2<radius || feld.getLaenge()/2 < radius){
				throw new IllegalArgumentException("Die Zeile mit dem Baum r="+line + " hat einen zu grossen Radius für das Feld!");
			}
			bezeichnung = f[1];
			if(!bezeichnung.contains(" ")){
				throw new IllegalArgumentException("Die Zeile mit dem Baum r="+line + " muss ein Leerzeichen nach dem % haben!");
			}
			bezeichnung=bezeichnung.substring(2);
		} catch (Exception e) {
			throw new IllegalArgumentException("Fehlerhafte Eingabedatei! Der Baum in Zeile:"+line + " hat ein Fehler! "+e.getMessage());
		}
		
		return new Baum(bezeichnung,index,radius);
	}
	
//	public boolean isCorrectUserInput(){
//		this.userInput = isCorrectDesignation() && isCorrectField() && isCorrectTreeSet();
//		return this.userInput;
//	}
	
//	public Feld readInput2(){
//		Feld f = new Feld();
//		f.setBreite(30);f.setLaenge(30);
//		f.setBezeichnung("Christinenwaeldchen");
//		List<Baum> baumListe = new ArrayList<Baum>();
//		baumListe.add(new Baum("Esche",0,6));//6 4
//		baumListe.add(new Baum("Erle",1,4));//4 3
//		baumListe.add(new Baum("Buche",2,9));//9 6
//		f.setBaumListe(baumListe );
//		return f; //TODO: hier
//	}
	
//	public Feld readInput3(){
//		Feld f = new Feld();
//		f.setBreite(1000);f.setLaenge(500);
//		f.setBezeichnung("Snowdenforst");
//		List<Baum> baumListe = new ArrayList<Baum>();
//		baumListe.add(new Baum("Eiche",0,12));
//		baumListe.add(new Baum("Esche",1,6));
//		baumListe.add(new Baum("Pappel",2,4));
//		baumListe.add(new Baum("Buche",3,9));
//		baumListe.add(new Baum("Erle",4,3));
//		f.setBaumListe(baumListe);
//		return f; //TODO: hier
//	}
	
	@SuppressWarnings("resource")
	public Feld readInput(String datei) {
		Feld f = new Feld();
		List<Baum> baumListe = new ArrayList<Baum>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(datei));
			String line;
			double[] feldgroessen = new double[2];
			int i = 1;
			int index = 0;

			
			int counter=0;
			while ((line = br.readLine()) != null) {
				counter++;
				if (i == 1) {
					f.setBezeichnung(isCorrectDesignation(line));
				} else if (i == 2) {
					feldgroessen = isCorrectField(line);
					f.setBreite((int) feldgroessen[0]);
					f.setLaenge((int) feldgroessen[0]);
				} 
				else {
					baumListe.add(isCorrectTreeSet(line, index,f));
					index++;
				}
				System.out.println(line);
				i++;
			}
			if(counter<=2){
				throw new IllegalArgumentException("Eingabedatei ist nicht vollstaendig!"); 
			}
			f.setBaumListe(baumListe);
			br.close();
			return f;
		} catch (Exception e) {
			Ausgabe.exportFehler(e.getMessage(), datei);
		}
		return null;
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
	
//	/**
//	 * FileFilter um nur .in Dateien im Eingabeordner zu untersuchen.
//	 */
//	private FileFilter dateiFilter = new FileFilter() {
//		// Ein Dateiendungsfilter: Es dürfen nur Dateien mit der Endung
//		// .in ausgewählt werden.
//		public boolean accept(File arg0) {
//			return arg0.getName().endsWith(".in");
//		}
//	};
	
}
