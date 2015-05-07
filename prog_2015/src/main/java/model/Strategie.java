package model;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author artur.schaefer
 *
 */
public class Strategie implements IStrategie{
	
	private String name;
	public List<String> lterminZeitpunkte = new ArrayList<String>();
	private List<Integer> lstrategieListe = new ArrayList<Integer>();
	private int[] lterminDauerWerte;
	private BigInteger anzahlKombinationen=null;
	private double wz = 0;
	private double mwz = 0;
	private double lz = 0;
	private int[] dauer;
	
	public Strategie(String strategieName, List<Integer> lstrategieListe) throws Exception {
		this.name = strategieName;
		this.lstrategieListe = lstrategieListe;
		setTerminZeitPunkte();
		
		dauer = new int[lterminDauerWerte.length];
		for(int i=0; i<lterminDauerWerte.length; i++) {
			dauer[i] = 15;
		}
		createAllKombinations();
	}

	private Calendar getTimeByString(String time) throws Exception{
		Calendar zeit = Calendar.getInstance();
		try {
			SimpleDateFormat datumsFormat = new SimpleDateFormat("HH:mm");
			Date date = datumsFormat.parse(time); 
			zeit.setTime(date);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Die folgende angegebene Zeit ist fehlerhaft:"+time);
		}
		return zeit;
	}
	
	private String getTimeByCalendar(Calendar cal){
		SimpleDateFormat df = new SimpleDateFormat("HH:mm");
		String newTime = df.format(cal.getTime());
		return newTime;
	}
	
	/**
	 * setzt die termin uhrzeiten
	 * @throws Exception 
	 */
	private void setTerminZeitPunkte() throws Exception {
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
			
			String letzerTermin = lterminZeitpunkte.remove(lterminZeitpunkte.size()-1);
			int letzteDauer = terminDauer.remove(terminDauer.size()-1);
			long x = getTimeByString(letzerTermin).getTimeInMillis();
			long y = zwoelfUhr.getTimeInMillis(); 
			long z = (x-y)/1000/60;
			int g = (int) (letzteDauer - z);
			terminDauer.add(g);
			lterminDauerWerte = new int[terminDauer.size()];
			for(int i = 0;i<terminDauer.size();i++){
				lterminDauerWerte[i]= terminDauer.get(i);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw new Exception("Fuer die Strategie '"+getName()+"' konnten die Termine nicht berechnet werden.");
		}
		
	}

	private void generateTime(Calendar uhrzeit, int phase, Calendar start,
			List<Integer> terminDauer) {
		while(start.before(uhrzeit)){
			start.add(Calendar.MINUTE, phase);
			String addTime = getTimeByCalendar(start);
			lterminZeitpunkte.add(addTime);
			terminDauer.add(phase);
		}
	}

	public void createAllKombinations(){
	    int patienten = lterminZeitpunkte.size();
		int anzahl_kombinationen = (int) getPowerOf(3,patienten);//Math.pow(3, patienten);
		//TODO: check hier
		anzahlKombinationen = new BigInteger(anzahl_kombinationen+"");
		//
		//Kombi k = new Kombi(patienten, 0);
		for(int i=0; i<anzahl_kombinationen; i++) {
			//System.out.println("Kombination "+i+": "+Arrays.toString(k.getKombi()));
			Kombination kom = new Kombination(lterminDauerWerte,dauer);
			this.wz += kom.getWZ();
			this.mwz += kom.getMWZ();
			this.lz += kom.getLZ();
			//lKombinationen.add(kom);
			next();
		}
	}
	
	/**
	 * durchschnittliche mittlere wartezeit
	 */
	public double getWZ() {
		return this.wz/ this.anzahlKombinationen.intValue();
	}

	/**
	 * durchschnittliche maximale wartezeit
	 */
	public double getMWZ() {
		return this.mwz/ this.anzahlKombinationen.intValue();
	}

	/**
	 * durchschnittliche Leerlaufzeit
	 */
	public double getLZ() {
		return this.lz/ this.anzahlKombinationen.intValue();
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
		String tab = "\t";
		double rounded = 0.0;
		ret.append(name+n);
		ret.append("Terminverteilung bei dieser Strategie:"+n);
		for (String t : lterminZeitpunkte) {
			ret.append(t+" ");
		}
		ret.append(n+n+"Bei "+anzahlKombinationen +" Kombinationen der Behandlungsdauern ergen sich folgende Zeiten:"+n);
		
		rounded = Math.round(10000.0*getWZ())/10000.0;
		ret.append(" durchschnittliche mittlere Wartezeit"+tab+" WZ = "+String.format("%.4f\n",rounded));
		
		
		rounded = Math.round(10000.0*getMWZ())/10000.0;
		ret.append(" durchschnittliche maximale Wartezeit"+tab+"MWZ = "+String.format("%.4f\n",rounded));
		
		rounded = Math.round(10000.0*getLZ())/10000.0;
		ret.append(" durchschnittliche maximale Wartezeit"+tab+" LZ = "+String.format("%.4f\n",rounded));
		
		rounded = Math.round(10000.0*getBS())/10000.0;
		ret.append(" Gesamtbewertung der Strategie"+tab+tab+tab+" BS = "+String.format("%.4f\n",rounded)+n);
		
		return ret.toString();
	}
	
	public double getPowerOf(int n, int k){
		double ret = n;
		for(int i = 1;i<k;i++){
			ret *= n;
		}
		return ret;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void next() {
		// "belege zahl um eins von (20,30,15) weiter"
		for(int j=0; j<dauer.length; j++) {
			switch(dauer[j]) {
				case 15: dauer[j] = 20; return;
				case 20: dauer[j] = 30; return;
				case 30: dauer[j] = 15; break;
			}
		}
		
		// bei ueberlauf -> fange wieder mit erster kombination an
		for(int j=0; j<dauer.length; j++) {
			dauer[j] = 15;
		}
	}
}
