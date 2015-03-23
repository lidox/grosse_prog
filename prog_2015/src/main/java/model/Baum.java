package model;

public class Baum {
	public String bezeichnung;
	public int index,spot;
	public double radius,x,y;
	
	public Baum(String bezeichnung,int index,double r){
		this.bezeichnung = bezeichnung;
		this.index = index;
		this.radius = r;
	}
	
	public Baum(Baum b){
		this.bezeichnung = b.bezeichnung;
		this.index = b.index;
		this.radius = b.radius;
		this.x = b.x;
		this.y = b.y;
		this.spot = b.spot;
	}
	
	public Baum(String bezeichnung,int index,double r, double x, double y, int spot){
		this.bezeichnung = bezeichnung;
		this.index = index;
		this.radius = r;
		this.x = x;
		this.y = y;
		this.spot = spot;
	}
	
	public String getBezeichnung() {
		return bezeichnung;
	}
	public void setBezeichnung(String bezeichnung) {
		this.bezeichnung = bezeichnung;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public int getSpot() {
		return spot;
	}

	public void setSpot(int spot) {
		this.spot = spot;
	}
	
	public String toString(){
		StringBuilder ret = new StringBuilder();
		ret.append(getX());
		ret.append(" ");
		ret.append(getY());
		ret.append(" ");
		ret.append(getRadius());
		ret.append(" ");
		ret.append(getIndex());
		return ret.toString();
	}
	
	
}
