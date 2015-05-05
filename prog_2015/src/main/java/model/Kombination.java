package model;



public class Kombination {
	private int[][] feld;
	private int verstrichenEnd = 0;
	private int realeEnd = 0;
	
	public Kombination(int[] termine, int[] kombination){
		feld = new int[4][termine.length];
		feld[3] = kombination;
		setVerstricheneZeit(termine);
		setRealeZeit();
		setDifferenz();
		//System.out.println(Arrays.toString(feld[3]));
		if(kombination[0]==15 && kombination[1]==30 && kombination[2]==20 && kombination[4]==15 && kombination[9]==30){
			System.out.print(""); 
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
	
	public double getWZ(){
		int max = feld[2][0];
		for (int i = 1; i < feld[2].length; i++) {
			if(max< feld[2][i]){
				max = feld[2][i];
			}
		}
		return max;
	}
	
	public double getLZ_oldversion(){//TODO: delete da alt
		double l = feld[2][feld[2].length-1];
		if(l<0){
			return l*(-1);
		}
		return 0;
	}
	
	public double getLZ_old2(){
		double l = realeEnd - verstrichenEnd;
		if(l<0){
			return l*(-1);
		}
		return 0;
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
	
	public double getMWZ(){
		double sum = 0;
		for (int i = 1; i < feld[2].length; i++) {
			if(feld[2][i]>0){
				sum+=feld[2][i];
			}
		}
		return sum /  feld[2].length;
	}
}
