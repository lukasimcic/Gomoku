package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;

import splosno.Koordinati;

public class RandomMinimax {
	
	private static final Random RANDOM = new Random();
	
	private static final int ZMAGA = 10000; // vrednost zmage, veè kot vsaka druga ocena pozicije
	private static final int NEODLOC = 0;  // vrednost neodloèene igre	
	
	public static Koordinati izberiPotezo (Igra igra, int globina) {
		System.out.println("izbiram potezo");
		List<OcenjenaPoteza> ocenjenePoteze = najboljsePoteze(igra, globina);
		System.out.println(ocenjenePoteze.size() + " potez z vrednostjo " + ocenjenePoteze.get(0).ocena);
		int i = RANDOM.nextInt(ocenjenePoteze.size());	
		return ocenjenePoteze.get(i).poteza;		
	}
	
	// vrne seznam vseh potez, ki imajo najveèjo vrednost z vidike trenutnega igralca na potezi
	public static List<OcenjenaPoteza> najboljsePoteze(Igra igra, int globina) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.seznamMoznihPotez;
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra); 
			kopijaIgre.odigraj(p); //poskusimo vsako potezo v novi kopiji igre
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_C:
			case ZMAGA_B: ocena = ZMAGA; break; // p je zmagovalna poteza
			case NEODLOCENO: ocena = NEODLOC; break;
			default: //nekdo je na potezi
				if (globina==1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre,igra.getIgralecNaPotezi());
				else ocena = //negacija ocene z vidike dgrugega igralca
						-najboljsePoteze(kopijaIgre,globina-1).get(0).ocena;  
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(p, ocena));			
		}
		return najboljsePoteze.list();
	}

	
}
