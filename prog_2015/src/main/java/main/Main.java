package main;

import io.InputOutput;
import controller.Controller;

/**
 * 
 * @author artur.schaefer
 *
 */
public class Main {
	// src/main/resources/ihk
	public static void main(String[] args) {

		long start = System.currentTimeMillis();
		String output = null;
		Controller c;

		// es muss angegeben werden, in welchem ordner die eingabe-dateien liegen
		if (args.length == 0) {
			System.out.println("Es wird ein Parameter benoetigt!");
		} else {
			// ist der optionale parameter gesetzt, soll er verwendet werden
			if (args.length == 2) {
				if (args[1] != null && args[1].length() > 1) {
					output = args[1];
				} else {
					System.out.println("Zweiter Parameter ist ungueltig");
				}
			}
			c = new Controller(new InputOutput(args[0], output));
			// starte das einlesen der dateien
			c.starteBerechnung();
		}

		long delta = System.currentTimeMillis() - start;
		System.out.println("Programmlaufzeit: " + delta / 1000.0 + "s");
	}

}
