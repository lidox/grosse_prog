package controller;

import io.Ausgabe;
import io.Eingabe;
import io.InputOutput;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import model.Strategie;

public class Controller {
	
	private InputOutput io;
	
	public Controller(InputOutput argIO) {
		this.io = argIO;
	}
	
	public void starteBerechnung(){
		FilenameFilter dateiFilter = new FilenameFilter() {
			// Ein Dateinamenfilter: Es duerfen nur Dateien mit der Endung
			// .in ausgewaehlt werden.
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith("");
			}
		};
		
		File ordner = null;
		String ordnerpfad = io.getDateiPfad();
		ordner = new File(ordnerpfad);

		String[] inputDateien = ordner.list(dateiFilter);

		for (int i = 0; i < inputDateien.length; i++) {
			// Durchlaufe alle gefunden .in-Dateien und berechne die
			// Ergebnisse.
			System.out.println();
			System.out.println("Folgende Datei wird nun bearbeitet: "
					+ inputDateien[i]);
			
			File[] eingabeDateien = io.read(ordnerpfad + "/"+ inputDateien[i], dateiFilter);
			for (File eingabeDatei : eingabeDateien) {
				// Durchlaufe alle gefunden .in-Dateien und berechne die
				// Ergebnisse.
				List<Strategie> a=null;
				try {
					a = readInput(eingabeDatei);
					if(a==null){
						continue;
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				long start = System.currentTimeMillis();
				for(Strategie item: a){
					item.createAllKombinations();
				}
				//TODO: hier den algo starten
				//export(eingabeDatei);
				
			}
		}
	}
	
	public List<Strategie> readInput(File eingabeDatei) throws Exception{
		List<Strategie> feld = io.readInput(eingabeDatei.getAbsolutePath());
		return feld;
	}
	
	public void export(File eingabeDatei){
		StringBuilder ret = new StringBuilder(eingabeDatei.getAbsolutePath());
		ret.delete(eingabeDatei.getAbsolutePath().length()-3, eingabeDatei.getAbsolutePath().length());
		ret.append(".plt");
		//ausgabe.export(feld, ret.toString());
	}
}
