package test;

import model.Kombination;

import org.junit.Test;

public class KombinationTest {
	private Kombination kombi;
	
	@Test
	public void test(){
		int[] termine = { 15,15,15,15,30,30,30,30,20,20,20};
		int[] kom = {20,30,15,30,30,15,20,15,15,15,20};
		kombi = new Kombination(termine, kom);
		System.out.println("Leerlauf="+kombi.getLZ());
		System.out.println("Wartezeit="+kombi.getWZ());
		System.out.println("Mittlere="+kombi.getMWZ());
	}
	
	@Test
	public void test2(){
		int[] termine = {15,15,15,15,30,30,30,30,20,20,20};
		int[] kom = {15,15,15,15,15,15,15,15,15,15,15};
		kombi = new Kombination(termine, kom);
		System.out.println("Leerlauf="+kombi.getLZ());
		System.out.println("Wartezeit="+kombi.getWZ());
		System.out.println("Mittlere="+kombi.getMWZ());
	}
	
	@Test
	public void testNachYannik(){
		int[] termine = {15,15,15,15,30,30,30,30,20,20,20};
		int[] kom = {15, 30, 20, 15, 15, 15, 15, 15, 15, 30, 15};
		kombi = new Kombination(termine, kom);
		System.out.println("Leerlauf="+kombi.getLZ());
		System.out.println("Wartezeit="+kombi.getWZ());
		System.out.println("Mittlere="+kombi.getMWZ());
	}
	
}
