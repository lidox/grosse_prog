package model;


import java.util.Arrays;

public class Kombi {

	private int[] dauer;
	
	public Kombi(int patienten, int kombinations_index) {
		final int anzahl_kombinationen = (int) getPowerOf(3, patienten);

		if(kombinations_index < 0 || kombinations_index >= anzahl_kombinationen) {
			throw new IllegalArgumentException("Ungueltiger Kombinations-Index "+kombinations_index+" bei insgesamt "+anzahl_kombinationen+" Kombinationen.");
		}
		
		dauer = new int[patienten];
		
		int a = kombinations_index;
		
		for(int i=0; i<patienten; i++) {
			switch(a % 3) {
			case 0: dauer[i] = 15; break;
			case 1: dauer[i] = 20; break;
			case 2: dauer[i] = 30; break;
			}
			
			a /= 3;
		}
	}
	
	public int getAnzahlPatienten() {
		return dauer.length;
	}
	
	public int getDauer(int patient) {
		if(patient < 0 || patient >= dauer.length) {
			throw new IllegalArgumentException("Ungueltige Patienten-Nummer "+patient+" bei insgesamt "+dauer.length+" Patienten.");
		}
		
		return dauer[patient];
	}
	
	public String toString() {
		return Arrays.toString(dauer);
	}
	
	public int [] getKombi(){
		return dauer;
	}
	
	public void next() {
		// "Addiere ternaere Zahl um eins"
		for(int i=0; i<dauer.length; i++) {
			switch(dauer[i]) {
			case 15: dauer[i] = 20; return;
			case 20: dauer[i] = 30; return;
			case 30: dauer[i] = 15; break;
			}
		}
		
		// Ueberlauf -> Fange wieder mit erster Kombination an
		for(int i=0; i<dauer.length; i++) {
			dauer[i] = 15;
		}
	}
	
	public double getPowerOf(int n, int k){
		double ret = n;
		for(int i = 1;i<k;i++){
			ret *= n;
		}
		return ret;
	}
}

