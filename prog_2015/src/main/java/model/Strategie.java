package model;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Strategie implements IStrategie{
	
	private String name;
	public List<String> lterminZeitpunkte = new ArrayList<String>();
	private List<Integer> lstrategieListe = new ArrayList<Integer>();
	private int[] lterminDauerWerte;
	private BigInteger anzahlKombinationen=null;
	private List<Kombination> lKombinationen = new ArrayList<Kombination>();
	
 
	public Strategie(String strategieName, List<Integer> lstrategieListe) {
		this.name = strategieName;
		this.lstrategieListe = lstrategieListe;
		setTerminZeitPunkte();
		//createAllKombinations();
	}

	private Calendar getTimeByString(String s){
		Calendar cal1 = Calendar.getInstance();
		try {
			SimpleDateFormat dfs = new SimpleDateFormat("HH:mm");
			Date d1 = dfs.parse(s);
			cal1.setTime(d1);
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return cal1;
	}
	
	private String getTimeByCalendar(Calendar cal){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		String newTime = df.format(cal.getTime());
		return newTime;
	}
	
	/**
	 * setzt die termin uhrzeiten
	 */
	private void setTerminZeitPunkte() {
		try {
			lterminZeitpunkte.add("8:00");
			Calendar neunUhr = getTimeByString("9:00");
			Calendar elfUhr = getTimeByString("11:00");
			Calendar zwoelfUhr = getTimeByString("12:00");
			
			int phase1 = lstrategieListe.get(0);
			int phase2 = lstrategieListe.get(1);
			int phase3 = lstrategieListe.get(2);
			
			Calendar aktuelleZeit = getTimeByString("8:00");
			
			//lterminDauerWerte
			List<Integer> terminDauer = new ArrayList<Integer>();
			
			generateTime(neunUhr, phase1, aktuelleZeit, terminDauer);
			
			generateTime(elfUhr, phase2, aktuelleZeit, terminDauer);
			
			generateTime(zwoelfUhr, phase3, aktuelleZeit, terminDauer);
			
			System.out.println();
			String letzerTermin = lterminZeitpunkte.remove(lterminZeitpunkte.size()-1);//TODO: removen
			int letzteDauer = terminDauer.remove(terminDauer.size()-1);
			long x = getTimeByString(letzerTermin).getTimeInMillis();
			long y = zwoelfUhr.getTimeInMillis(); 
			long z = (x-y)*1000*60;
			int g = (int) (letzteDauer - z);
			terminDauer.add(g);
			lterminDauerWerte = new int[terminDauer.size()];
			for(int i = 0;i<terminDauer.size();i++){
				lterminDauerWerte[i]= terminDauer.get(i);
			}
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
	}

	private void generateTime(Calendar neunUhr, int phase1, Calendar start,
			List<Integer> terminDauer) {
		while(start.before(neunUhr)){
			start.add(Calendar.MINUTE, phase1);
			String addTime = getTimeByCalendar(start);
			lterminZeitpunkte.add(addTime);
			terminDauer.add(phase1);
		}
	}

	public void createAllKombinations(){
		long start = System.currentTimeMillis();
	    int patienten = lterminZeitpunkte.size();
		int anzahl_kombinationen = (int) getPowerOf(3,patienten);//Math.pow(3, patienten);
		//TODO: check hier
		anzahlKombinationen = new BigInteger(anzahl_kombinationen+"");
		//
		Kombi k = new Kombi(patienten, 0);
		for(int i=0; i<anzahl_kombinationen; i++) {
			//System.out.println("Kombination "+i+": "+Arrays.toString(k.getKombi()));
			Kombination kom = new Kombination(lterminDauerWerte, k.getKombi());
			lKombinationen.add(kom);
			k.next();
		}
		long delta = System.currentTimeMillis() - start;
		
		System.out.println("time: "+delta/1000.0);
		System.out.println(this);
	}
	
	/**
	 * durchschnittliche mittlere wartezeit
	 */
	public double getWZ() {
		double sum = 0;
		for (Kombination kombination : lKombinationen) {
			sum+= kombination.getMWZ();
		}
		int anzahl = this.anzahlKombinationen.intValue();
		return sum/ (double) anzahl;
	}

	/**
	 * durchschnittliche maximale wartezeit
	 */
	public double getMWZ() {
		double sum = 0;
		for (Kombination kombination : lKombinationen) {
			sum+= kombination.getWZ();
		}
		return sum/ this.anzahlKombinationen.floatValue();
	}

	/**
	 * durchschnittliche Leerlaufzeit
	 */
	public double getLZ() {
		double sum = 0;
		for (Kombination kombination : lKombinationen) {
			sum+= kombination.getLZ();
		}
		int anzahl = this.anzahlKombinationen.intValue();
		return sum/ (double) anzahl;
	}

	/**
	 * gesamtbewertung der strategie
	 */
	public double getBS() {
		return getWZ() + (0.1 * getMWZ() + (5*getLZ()));
	}
	
	public String toString(){
		StringBuilder ret = new StringBuilder();
		String n = "\n";
		ret.append(name+n);
		ret.append("Terminverteilung bei dieser Strategie:"+n);
		for (String t : lterminZeitpunkte) {
			ret.append(t+" ");
		}
		ret.append("Bei "+anzahlKombinationen +" Kombinationen"+n);
		ret.append("WZ="+getWZ()+n);
		ret.append("MWZ="+getMWZ()+n);
		ret.append("LZ="+getLZ()+n);
		ret.append("BS="+getBS());
		
		return ret.toString();
	}
	
	public double getPowerOf(int n, int k){
		double ret = n;
		for(int i = 1;i<k;i++){
			ret *= n;
		}
		return ret;
	}
}
