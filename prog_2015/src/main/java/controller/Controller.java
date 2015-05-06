package controller;

import io.InputOutput;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import model.Strategie;

/**
 * 
 * @author artur.schaefer
 *
 */
public class Controller {
	
	private InputOutput io;
	
	public Controller(InputOutput argIO) {
		this.io = argIO;
	}
	
	/**
	 * liest die dateien ein und starte jeweils die berechung
	 * @throws Exception 
	 */
	public void starteBerechnung() throws Exception{
		FilenameFilter dateiFilter = new FilenameFilter() {
			// Ein Dateinamenfilter: Es duerfen nur Dateien mit der Endung
			// .in ausgewaehlt werden.
			public boolean accept(File arg0, String arg1) {
				String dateiEnde = "-out.txt";
				if(io.getAusgabeDateiName()!=null){
					dateiEnde = io.getAusgabeDateiName();
				}
				return !arg1.endsWith(dateiEnde);
			}
		};
		
		File ordner = null;
		String ordnerpfad = io.getDateiPfad();
		ordner = new File(ordnerpfad);

		String[] inputDateien = ordner.list(dateiFilter);

		// Durchlaufe alle gefunden Dateien und berechne die Ergebnisse
		for (int i = 0; i < inputDateien.length; i++) {
			System.out.println("Folgende Datei wird nun bearbeitet: "+ inputDateien[i]);
			
			File[] eingabeDateien = io.read(ordnerpfad + "/"+ inputDateien[i], dateiFilter);
			for (File eingabeDatei : eingabeDateien) {
				// Durchlaufe alle gefunden .in-Dateien und berechne die
				// Ergebnisse.
				List<Strategie> strategieListe = null;
				try {
					strategieListe  = io.readInput(eingabeDatei.getAbsolutePath());
					if(strategieListe ==null){
						continue;
					}
				} catch (Exception e) {
					// TODO: es ist ein fehler aufgetretten, also export fehler
					eingabeDatei.getName();
					e.printStackTrace();
				}

				io.export(eingabeDatei, strategieListe);
			}
		}
	}
	
}
