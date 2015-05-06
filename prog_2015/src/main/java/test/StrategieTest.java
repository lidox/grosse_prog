package test;

import java.util.ArrayList;
import java.util.List;

import model.Strategie;

import org.junit.Test;

public class StrategieTest {
	
	@Test
	public void test() throws Exception{
		List<Integer> l = new ArrayList<Integer>();
		l.add(15);
		l.add(30);
		l.add(20);
		
		Strategie s = new Strategie("% Nr. 1: Strategie aus der Aufgabenstellung", l);
		System.out.println(s.lterminZeitpunkte);
		System.out.println("durchschnittliche mittlere wartezeit = " +s.getWZ());
		System.out.println("durchschnittliche maximale wartezeit = " +s.getMWZ());
		System.out.println("durchschnittliche Leerlaufzeit = " +s.getLZ());
		System.out.println("gesamtbewertung der strategie = " +s.getBS());
	}
	
	@Test
	public void test2() throws Exception{
		List<Integer> l = new ArrayList<Integer>();
		l.add(15);
		l.add(15);
		l.add(15);
		
		Strategie s = new Strategie("% Nr. 2: keine leerlaufzeiten", l);
		
		System.out.println(s.lterminZeitpunkte);
		System.out.println("durchschnittliche mittlere wartezeit = " +s.getWZ());
		System.out.println("durchschnittliche maximale wartezeit = " +s.getMWZ());
		System.out.println("durchschnittliche Leerlaufzeit = " +s.getLZ());
		System.out.println("gesamtbewertung der strategie = " +s.getBS());
	}
	
	@Test
	public void testPowerOf1() throws Exception{
		List<Integer> l = new ArrayList<Integer>();
		l.add(15);
		l.add(30);
		l.add(20);
		Strategie s = new Strategie("% Nr. 2: keine leerlaufzeiten", l);
		double d = s.getPowerOf(2,3);
		System.out.println(d);
	}
	
	@Test
	public void test30er() throws Exception{
		List<Integer> l = new ArrayList<Integer>();
		l.add(21);
		l.add(21);
		l.add(21);
		
		Strategie s = new Strategie("% Nr. 3: 30err", l);
		
		System.out.println(s.lterminZeitpunkte);
		System.out.println("durchschnittliche mittlere wartezeit = " +s.getWZ());
		System.out.println("durchschnittliche maximale wartezeit = " +s.getMWZ());
		System.out.println("durchschnittliche Leerlaufzeit = " +s.getLZ());
		System.out.println("gesamtbewertung der strategie = " +s.getBS());
	}
	
	
	
}
