package model;


/**
 * 
 * @author schaefer.artur
 *
 */
public class Kombination {
	private int[][] feld;
	private int verstrichenEnd = 0;
	private int realeEnd = 0;
	
	public Kombination(int[] termine, int[] kombination){
		feld = new int[6][termine.length];
		feld[3] = kombination;
		setVerstricheneZeit(termine);
		setRealeZeit();
		setDifferenz();
		setX();
		setY();
	}
	
	private void setY() {
		for (int i = 0; i < feld[5].length; i++) {
			feld[5][i] = feld[4][i]-feld[0][i];
		}
	}

	private void setX() {
		feld[4][0] = 0;
		int i=1;
		for(;i<feld[0].length;i++){
			int v = feld[0][i-1];
			int x = feld[4][i-1];
			int big = x;
			if(v>x){
				big = v;
			}
			big = big + feld[3][i-1]; 
			feld[4][i]=big;
		}
	}

	private void setVerstricheneZeit(int[] termine){
		int sum = 0;
		int i = 1;
		for(;i<termine.length;i++){
			int a = termine[i-1];
			sum += a;
			feld[0][i] = sum;
		}
		this.verstrichenEnd = sum + termine[i-1];
	}
	
	private void setRealeZeit(){
		int gesamt = 0;
		feld[1][0] = 0;
		int i = 1;
		for(;i<feld[3].length;i++){
			gesamt += feld[3][i-1];
			feld[1][i] = gesamt;
		}
		this.realeEnd  = gesamt + feld[3][i-1];
	}
	
	private void setDifferenz(){
		for (int i = 0; i < feld[0].length; i++) {
			feld[2][i] = feld[1][i]-feld[0][i];
		}
	}
	
	public double getMWZ(){
		double max = 0;
		for (int i = 1; i < feld[5].length; i++) {
			if(max< feld[5][i]){
				max = feld[5][i];
			}
		}
		return (double)max;
	}
	
	public double getLZ(){
		double max = realeEnd - verstrichenEnd;
		for (int i = 1; i < feld[2].length; i++) {
			if(feld[2][i]<max){
				max=feld[2][i];
			}
		}
		if(max<0){
			return max*(-1);
		}
		return 0;
	}
	
	public double getWZ(){
		double sum = 0;
		for (int i = 1; i < feld[5].length; i++) {
			if(feld[5][i]>0){
				sum+=feld[5][i];
			}
		}
		return sum /  (double) feld[5].length;
	}
}
