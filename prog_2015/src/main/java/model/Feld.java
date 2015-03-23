package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Feld {
	public String bezeichnung;
	public int breite,laenge;
	public List<Baum> baumListe = new ArrayList<Baum>();
	public List<Baum> ergebnisListe = new ArrayList<Baum>();
	
	public int[] arten = new int[baumListe.size()];
	public ArrayList<Spot> spotListe = new ArrayList<Spot>();
	
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public int getBreite() {
		return breite;
	}
	public void setBreite(int breite) {
		this.breite = breite;
	}
	public int getLaenge() {
		return laenge;
	}
	public void setLaenge(int laenge) {
		this.laenge = laenge;
	}
	public List<Baum> getBaumListe() {
		return baumListe;
	}
	public void setBaumListe(List<Baum> baumListe) {
		this.baumListe = baumListe;
		arten = new int[baumListe.size()];
		Arrays.fill(arten, new Integer(0));
	}
	public List<Baum> getErgebnisList() {
		return ergebnisListe;
	}
	public void setErgebnisList(List<Baum> ergebnisList) {
		this.ergebnisListe = ergebnisList;
	}
	
	/**
	 * Meine zusatzmethoden:
	 */
	public void addBaum(Baum b){
		ergebnisListe.add(b);
		speedMap.put(b.getSpot(), b);
	}
	public HashMap<Integer, Baum> speedMap = new HashMap<Integer, Baum>();
	
	private int getMax(){
		int max = arten[0];
		for (int i =1;i<arten.length;i++) {
			if(arten[i]>max){
				max=arten[i];
			}
		}
		return max;
	}
	
	private int getMin(int[] ar){
		int min = ar[0]; 
		for (int i =1;i<ar.length;i++) {
			if(ar[i]<min){
				min=ar[i];
			}
		}
		return min;
	}
	
	public ArrayList<Integer> getNextTypeIndices(){
		ArrayList<Integer> moeglichIndices = new ArrayList<Integer>();
		for(int i=0;i<arten.length;i++){
			arten[i]+=1;
			if(isDispersity()){
				moeglichIndices.add(i);	
			}
			arten[i]-=1;
		}
		// sortieren: die am wenigsten verwendete baumart zuerst
		//moeglichIndices = getSortedTypeIndices(moeglichIndices);
		return moeglichIndices;
	}
	
	/**
	 * artenvielfachheit ok?
	 * @return
	 */
	public boolean isDispersity(){
		if(getMax()-getMin(arten)<2){
			return true;
		}
		return false;
	}
	
	public Baum getNextBaum(){
		return null;
	}

	public Baum getTree(Integer validIndex) {
		for (Baum baum : baumListe) {
			if(baum.getIndex()==validIndex){
				return baum;
			}
		}
		throw new IllegalArgumentException("Baum mit index="+validIndex +" nicht gefunden! Exception in class:Feld in Method: getTree()");
	}
	
	public ArrayList<Spot> getFreeSpots(){
		return spotListe;
	}
	
	public Baum getBaumBySpot(int spot){
//		for (Baum baum : ergebnisListe) {
//			if(baum.getSpot()==spot){
//				return baum;
//			}
//		}
		Baum ret = speedMap.get(spot);
		if(ret!=null){
			return ret;
		}
		throw new IllegalArgumentException("Exception in class:Feld in Method: getBaumBySpot()");
	}
	
	public void removeSpot(Spot spot){
		for (int i = 0;i<spotListe.size();i++) {
			if(spotListe.get(i).getSpotA()==spot.getSpotA() && spotListe.get(i).getSpotB()==spot.getSpotB()){
				spotListe.remove(i);
			}
		}
	}
	
	
}
