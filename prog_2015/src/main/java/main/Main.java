package main;

import io.Ausgabe;
import io.Eingabe;
import controller.Controller;

public class Main {

	public static void main(String[] args) {
		
		Controller c;
		try {
			if (args.length == 0) {
				System.out.println("Es wird ein Parameter benoetigt!");
			} else {
				c = new Controller(new Eingabe(args[0]), new Ausgabe());
				
				c.starteBerechnung();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

}
