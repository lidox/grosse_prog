package model;


import java.util.Arrays;

public class Kombi {

	private int[] dauer;
	
	public Kombi(int terminAnzahl, int index) {
//		final int kombinationenAnzahl = (int) getPowerOf(3, terminAnzahl);
//
//		if(index < 0 || index >= kombinationenAnzahl) {
//			throw new IllegalArgumentException("Ungueltiger index "+index+" bei insgesamt "+kombinationenAnzahl+" Kombinationen.");
//		}
		
		dauer = new int[terminAnzahl];
//		int tmp = index;
		
		for(int i=0; i<terminAnzahl; i++) {
			dauer[i] = 15;
//			switch(tmp % 3) {
//				case 0: dauer[i] = 15; break;
//				case 1: dauer[i] = 20; break;
//				case 2: dauer[i] = 30; break;
//			}
//			tmp = tmp/3;
		}
	}
	
//	public int getAnzahlPatienten() {
//		return dauer.length;
//	}
	
//	public int getDauer(int patient) {
//		if(patient < 0 || patient >= dauer.length) {
//			throw new IllegalArgumentException("Ungueltige Patienten-Nummer "+patient+" bei insgesamt "+dauer.length+" Patienten.");
//		}
//		
//		return dauer[patient];
//	}
	
//	public String toString() {
//		return Arrays.toString(dauer);
//	}
	
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
	
//	public double getPowerOf(int n, int k){
//		double ret = n;
//		for(int i = 1;i<k;i++){
//			ret *= n;
//		}
//		return ret;
//	}
}

