package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;
import logika.Algoritem;
import splosno.KdoIgra;

public class Inteligenca extends KdoIgra {
	
	private int globina;
	
	private static final Random RANDOM = new Random();
	
	private static final int ZMAGA = 1000000; // vrednost zmage
	private static final int PORAZ = -ZMAGA;  // vrednost izgube
	private static final int NEODLOC = 0;  // vrednost neodlocene igre	
	
	public Inteligenca (int globina) {
		super("racunalnik igra z globino " + globina);
		this.globina = globina;
	}	
	
	public static OcenjenaPoteza alfabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz, int nivo) {
		int ocena;
		// ce sem racunalnik, maksimiramo oceno z zacetno oceno PORAZ
		// ce sem pa clovek, minimiziramo oceno z zacetno oceno ZMAGA
		if (igra.getIgralecNaPotezi() == jaz) {ocena = PORAZ;} else {ocena = ZMAGA;}
		List<Koordinati> moznePoteze = igra.seznamMoznihPotez;
		Koordinati kandidat = moznePoteze.get(0); // Možno je, da se ne spremeni vrednost kanditata. Zato ne more biti null.
		List<OcenjenaPoteza> nekajNajboljsihPotez = nekajNajboljseOcenjenihPotez(moznePoteze, igra, nivo); // drevo naredimo le na najboljsi tretini moznih potez
		for (OcenjenaPoteza op: nekajNajboljsihPotez) {
			int ocenap;
			if (globina == 1 || moznePoteze.size() == 1 || op.ocena == ZMAGA) ocenap = op.ocena;
			else {
				Igra kopijaIgre = new Igra(igra); 
				kopijaIgre.odigraj(op.poteza); //poskusimo vsako potezo v novi kopiji igre
				ocenap = //negacija ocene z vidike drugega igralca
						-alfabetaPoteze(kopijaIgre, globina-1, alpha, beta, jaz, nivo+1).ocena;
			}
			if (igra.getIgralecNaPotezi() == jaz) { // Maksimiramo oceno
				if (ocenap > ocena) { // mora biti > namesto >=
					ocena = ocenap;
					kandidat = op.poteza;
					alpha = Math.max(alpha,ocena);
				}
			} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
				if (ocenap < ocena) { // mora biti < namesto <=
					ocena = ocenap;
					kandidat = op.poteza;
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
				return new OcenjenaPoteza (kandidat, ocena);
		}
		return new OcenjenaPoteza (kandidat, ocena);
	}
	
	// vrne seznam vseh potez, ki imajo najvecjo vrednost z vidike trenutnega igralca na potezi
	public static List<OcenjenaPoteza> minimaxPoteze(Igra igra, int globina, int nivo) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.seznamMoznihPotez;
		List<OcenjenaPoteza> nekajNajboljsihPotez = nekajNajboljseOcenjenihPotez(moznePoteze, igra, nivo); // drevo naredimo le na najboljsi tretini moznih potez
		for (OcenjenaPoteza op: nekajNajboljsihPotez) {
			int ocena;
			if (globina == 1 || moznePoteze.size() == 1 || op.ocena == ZMAGA) ocena = op.ocena;
			else {
				Igra kopijaIgre = new Igra(igra); 
				kopijaIgre.odigraj(op.poteza); //poskusimo vsako potezo v novi kopiji igre
				ocena = //negacija ocene z vidike drugega igralca
						-minimaxPoteze(kopijaIgre, globina-1, nivo+1).get(0).ocena;
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(op.poteza, ocena));	
		}
		return najboljsePoteze.list();
	}
	
	public static List<OcenjenaPoteza> nekajNajboljseOcenjenihPotez(List<Koordinati> moznePoteze, Igra igra, int nivo) {
		int steviloMoznihPotez = moznePoteze.size();
		int velikost = (int) steviloMoznihPotez / (5 + nivo) + 1;
		OcenjenaPotezaBuffer buffer;
		if (steviloMoznihPotez < 3) buffer = new OcenjenaPotezaBuffer(steviloMoznihPotez);
		else buffer = new OcenjenaPotezaBuffer(velikost);
		for (Koordinati p : moznePoteze) {
			Igra kopijaIgre = new Igra(igra); 
			kopijaIgre.odigraj(p); //poskusimo vsako potezo v novi kopiji igre
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_C:
			case ZMAGA_B: ocena = ZMAGA; break; // p je zmagovalna poteza
			case NEODLOCENO: ocena = NEODLOC; break;
			default: //nekdo je na potezi
			ocena = oceniPozicijo(kopijaIgre);
			}
			OcenjenaPoteza op = new OcenjenaPoteza(p, ocena);
			buffer.add(op);
		}
		return buffer.list(); 
	}
	
	public static int oceniPozicijo(Igra igra) {
		int ocena = 0;
		for (int ocenaVrste : igra.getVrste().values()) {
			ocena = ocena + ocenaVrste;
		}
		return ocena;	
	}
	
	public Koordinati izberiPotezo (Igra igra) {
		if (igra.getAlgoritem() == Algoritem.MINIMAX) {
			List<OcenjenaPoteza> ocenjenePoteze = minimaxPoteze(igra, globina, 0);
			int i = RANDOM.nextInt(ocenjenePoteze.size());	
			return ocenjenePoteze.get(i).poteza;
		}
		else return alfabetaPoteze(igra, globina, PORAZ, ZMAGA, igra.getIgralecNaPotezi(), 0).poteza;
	}
}