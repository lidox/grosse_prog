package model;

public class Spot {
	
	int spotA,spotB;
	
	public Spot(int a,int b){
		this.spotA=a;
		this.spotB=b;
	}
	
	public int getSpotA() {
		return spotA;
	}

	public void setSpotA(int spotA) {
		this.spotA = spotA;
	}

	public int getSpotB() {
		return spotB;
	}

	public void setSpotB(int spotB) {
		this.spotB = spotB;
	}

	public String toString(){
		StringBuilder ret = new StringBuilder();
		ret.append("<");
		ret.append(spotA);
		ret.append(",");
		ret.append(spotB);
		ret.append(">");
		return ret.toString();
	}
}
