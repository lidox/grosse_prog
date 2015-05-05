package main;

import io.Ausgabe;
import io.Eingabe;
import io.InputOutput;
import controller.Controller;

public class Main {
    // src/main/resources/ihk
	public static void main(String[] args) {
		Controller c;
		try {
			if (args.length == 0) {
				System.out.println("Es wird ein Parameter benoetigt!");
			} else {
				c = new Controller(new InputOutput(args[0]));
				
				c.starteBerechnung();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
