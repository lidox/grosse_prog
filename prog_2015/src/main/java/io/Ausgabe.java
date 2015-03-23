package io;

import java.io.FileWriter;
import java.io.IOException;

import model.Baum;
import model.Feld;

public class Ausgabe {
	public void export(Feld f, String exportDatei) {
		double d = berechneD(f);
		double b =  berechneB(f,d);
		String ausgabe = "";
		ausgabe += "reset\n";
		ausgabe += "set yrange [0:" + f.getLaenge() + "]\n";
		ausgabe += "set xrange [0:" + f.getBreite() + "]\n";
		ausgabe += "set size ratio " + f.getLaenge() / (f.getBreite() * 1.0)
				+ "\n";
		ausgabe += "set title \"" + f.getBezeichnung() + " mit D=" + d
				+ ", B=" + b + "\"\n";
		ausgabe += "plot '-' using 1:2:3:4 with circles lc var\n";
		for (Baum p : f.getErgebnisList()) {
			ausgabe += p.getX() + " ";
			ausgabe += p.getY() + " ";
			ausgabe += p.getRadius() + " ";
			ausgabe += p.getIndex() + "\n";
		}

		FileWriter fw;
		try {

			fw = new FileWriter(exportDatei);//"D:/Users/A543414/Desktop/ausgabe.plt"

			fw.write(ausgabe);

			fw.flush();

			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	private double berechneB(Feld f, double d) {
		double zaehler=0;
		for (int i=0;i<f.arten.length;i++) {
			zaehler += f.arten[i]*Math.PI*f.getTree(i).getRadius()*f.getTree(i).getRadius();
		}
		double b= d * (zaehler / (f.getBreite()*f.getLaenge()));
		return b;
	}

	private double berechneD(Feld f) {
		double n = 0;
		for (Integer i : f.arten) {
			n+=i;
		}
		double d = 1;
		for (Integer i : f.arten) {
			d-= (i/n)*(i/n);
		}
		return d;
	}
	
	public static void exportFehler(String message, String exportDatei) {
		System.out.println(message);
		StringBuilder ret = new StringBuilder(exportDatei);
		ret.delete(exportDatei.length()-3, exportDatei.length());
		ret.append(".err");
		FileWriter fw;
		try {

			fw = new FileWriter(ret.toString());//"D:/Users/A543414/Desktop/ausgabe.plt"

			fw.write(message);

			fw.flush();

			fw.close();

		} catch (IOException e) {

			e.printStackTrace();
		}
		//System.exit(0);
	}
	
}
