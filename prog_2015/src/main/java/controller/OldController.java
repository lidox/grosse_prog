package controller;

import io.Ausgabe;
import io.Eingabe;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Vector;

import model.Baum;
import model.Feld;
import model.Spot;

public class OldController {
	
	private Eingabe eingabe;
	private Ausgabe ausgabe;
	public Feld feld;
	private int spot = 1;
	
	public OldController(Eingabe eingabe, Ausgabe ausgabe) throws Exception{
		this.eingabe = eingabe;
		this.ausgabe = ausgabe;
	}
	
	public void starteBerechnung() throws Exception{
		FilenameFilter dateiFilter = new FilenameFilter() {
			// Ein Dateinamenfilter: Es duerfen nur Dateien mit der Endung
			// .in ausgewaehlt werden.
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".in");
			}
		};
		
		File ordner = null;
		String ordnerpfad = eingabe.getDateiPfad();
		ordner = new File(ordnerpfad);

		String[] inputDateien = ordner.list(dateiFilter);

		for (int i = 0; i < inputDateien.length; i++) {
			// Durchlaufe alle gefunden .in-Dateien und berechne die
			// Ergebnisse.
			System.out.println();
			System.out.println("Folgende Datei wird nun bearbeitet: "
					+ inputDateien[i]);
			
			File[] eingabeDateien = eingabe.read(ordnerpfad + "/"+ inputDateien[i], dateiFilter);
			for (File eingabeDatei : eingabeDateien) {
				// Durchlaufe alle gefunden .in-Dateien und berechne die
				// Ergebnisse.
				if(readInput(eingabeDatei)==null){
					continue;
				}
				
				long start = System.currentTimeMillis();
				try {
					addInitiallyTwoTrees();
				} catch (Exception e) {
					Ausgabe.exportFehler(e.getMessage(), eingabeDatei.getAbsolutePath());
					System.exit(0);
				}
				addTreeByTree();
				long end = System.currentTimeMillis()-start;
				System.out.println("Laufzeit: "+end/1000 + "s");
				export(eingabeDatei);
			}
		}
	}

	private void addTreeByTree() {
		//prüfe diversität und gebe geeigneten baum
		main: while(feld.getNextTypeIndices().size()!=0){
			ArrayList<Integer> freeTypeIndices = feld.getNextTypeIndices(); // gib mögliche nächste Bäume, die man pflanzen kann
			int i=0;
			for (Integer validIndex : freeTypeIndices) {
				Baum b = feld.getTree(validIndex);
				//welche spots sind frei
				ArrayList<Spot> spots = new ArrayList<Spot>(feld.getFreeSpots());
				boolean isAddedSecond=false;
				boolean isAdded=false;
				int j= 0;
				spotLabel: for (Spot spot : spots) {
					ArrayList<Double> mittelpunkte = getMoeglicheMittelpunkte(b, spot);
					double m2ax = mittelpunkte.get(0);
					double m2ay = mittelpunkte.get(1);
				
					isAdded = addBaumIfPossible(b, spot, m2ax, m2ay);
					if(!isAdded){
						isAddedSecond = addBaumIfPossible(b, spot, mittelpunkte.get(2), mittelpunkte.get(3));
					}
					else{
						break spotLabel;
					}
					if(isAddedSecond){
						break spotLabel;
					}
					j++;
				}
				i++;
				if(i==freeTypeIndices.size() && j==spots.size()){break main;}
			}
		}
		System.out.println("Berechnung fertig");
	}

	private boolean addBaumIfPossible(Baum b, Spot spot, double m2x, double m2y) {
		boolean checkXAchse = (m2x +b.getRadius() <= feld.getBreite()) && (m2x-b.getRadius() >= 0);
		boolean checkYAchse = (m2y +b.getRadius() <= feld.getLaenge()) && (m2y-b.getRadius() >= 0);

		if(checkXAchse && checkYAchse){
			//prüfe ob sich der kreis mit allen anderen kreisen schneidet
			for (Baum baum : feld.ergebnisListe) {
				double m1x = baum.getX();
				double m1y = baum.getY();
				double laengeM1M2 = Math.sqrt( (m2x-m1x)*(m2x-m1x) + (m2y-m1y)*(m2y-m1y) );
				double laengeRadien = baum.getRadius() + b.getRadius();
				
				boolean schneidenSichNicht = laengeM1M2 > laengeRadien;
				boolean sindGleich = Math.abs(laengeM1M2-laengeRadien)<=0.005;
				if(sindGleich || schneidenSichNicht){
					// kreis schneidet nicht einen der kreise aus der ergebnisliste
					continue;
				}
				else{
					return false;
				}
			}
		}
		else{
			return false;
		}
		// kreis schneidet keinen aus der ergebnisliste, mittelpunkt gut ==> hinzufügen
		this.spot++;
		Baum addBaum = new Baum(b.getBezeichnung(), b.getIndex(), b.getRadius(), m2x, m2y, this.spot);
		feld.arten[b.getIndex()]+=1;
		feld.ergebnisListe.add(addBaum);feld.speedMap.put(addBaum.getSpot(),addBaum);
		// remove und add die richtigen spots für neue Kreise
		feld.removeSpot(spot);
		feld.spotListe.add(new Spot(spot.getSpotA(), addBaum.getSpot()));
		feld.spotListe.add(new Spot(spot.getSpotB(), addBaum.getSpot()));
		return true;
	}

	private ArrayList<Double> getMoeglicheMittelpunkte(Baum b, Spot spot) {
		//berechne jeweils ein mittelpunkt 
		double r1 = feld.getBaumBySpot(spot.getSpotA()).getRadius();
		double r2 = feld.getBaumBySpot(spot.getSpotB()).getRadius();
		double r3 = b.getRadius();
		
		//A
		double l1 = r2+r3;
		double l2 = r1+r3;
		double l3 = r1+r2;
		
		//B
		double fi = Math.acos( ((l2*l2)+(l3*l3)-(l1*l1)) / (2*l2*l3) );
		double SM1 = Math.cos(fi)*l2;
		double factorA = SM1 / l3;
		
		//C
		Vector<Double> vM1S = new Vector<Double>();
		double M1M2x =feld.getBaumBySpot(spot.getSpotB()).getX() - feld.getBaumBySpot(spot.getSpotA()).getX();
		double M1M2y =feld.getBaumBySpot(spot.getSpotB()).getY() - feld.getBaumBySpot(spot.getSpotA()).getY();	
		vM1S.add(M1M2x*factorA);
		vM1S.add(M1M2y*factorA);
		
		double Sx = feld.getBaumBySpot(spot.getSpotA()).getX() + vM1S.get(0);
		double Sy = feld.getBaumBySpot(spot.getSpotA()).getY() + vM1S.get(1);
		
		double SM3 = Math.sin(fi)*l2;
		double factorB = SM3 / l3;
		
		double M1M2xStrich =feld.getBaumBySpot(spot.getSpotB()).getY() - feld.getBaumBySpot(spot.getSpotA()).getY();		
		double M1M2yStrich =(feld.getBaumBySpot(spot.getSpotB()).getX() - feld.getBaumBySpot(spot.getSpotA()).getX())*-1;
		
		Vector<Double> vSM3 = new Vector<Double>();
		vSM3.add(M1M2xStrich*factorB);
		vSM3.add(M1M2yStrich*factorB);
		
		double M3x = Sx + vSM3.get(0);
		double M3y = Sy + vSM3.get(1);
		
		ArrayList<Double> ret = new ArrayList<Double>();
		ret.add(M3x);
		ret.add(M3y);
		ret.add(Sx - vSM3.get(0));
		ret.add(Sy - vSM3.get(1));
		if(ret.size()!=4){throw new IllegalArgumentException("Die berechnung muss immer 2 Mittelpunkte liefern");}
		return ret;
	}

	private void addInitiallyTwoTrees() {
		//init Baum 1:
		Baum b = new Baum(feld.getBaumListe().get(0));
		b.setX(b.getRadius());b.setY(b.getRadius());
		b.setSpot(0);
		feld.arten[0]+=1;
		feld.ergebnisListe.add(b);feld.speedMap.put(b.getSpot(),b);
		
		//init Baum 2:
		Baum b2 = new Baum(feld.getBaumListe().get(0));
		if(feld.getBaumListe().size()>1){//fals es nur eine baumart gibt
			b2 = new Baum(feld.getBaumListe().get(1));
		}
		b2.setX(b2.getRadius());
		double r1Plusr2 = (b.getRadius()+b2.getRadius())*(b.getRadius()+b2.getRadius());
		double r1Minusr2 = (b.getRadius()-b2.getRadius())*(b.getRadius()-b2.getRadius());
		double y = b.getRadius() + Math.sqrt(r1Plusr2-r1Minusr2);
		b2.setY(y);
		b2.setSpot(1);
		
		//TODO: testen ob er passt
		double rgesamt = b2.getRadius()+b.getRadius();
		if(rgesamt>feld.getBreite()/2 || rgesamt>feld.getLaenge()/2){
			throw new IllegalArgumentException("Die ersten zwei Kreise sind zu gross fuer das Feld");
		}
		feld.arten[1]+=1;
		feld.ergebnisListe.add(b2);feld.speedMap.put(b2.getSpot(),b2);
		feld.spotListe.add(new Spot(0, 1));
	}
	
	public Feld readInput(File eingabeDatei) throws Exception{
		feld = eingabe.readInput(eingabeDatei.getAbsolutePath());
		return feld;
	}
	
	public void export(File eingabeDatei){
		StringBuilder ret = new StringBuilder(eingabeDatei.getAbsolutePath());
		ret.delete(eingabeDatei.getAbsolutePath().length()-3, eingabeDatei.getAbsolutePath().length());
		ret.append(".plt");
		ausgabe.export(feld, ret.toString());
	}
}
