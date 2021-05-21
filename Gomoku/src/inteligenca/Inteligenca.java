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
	private static final int NEODLOC = 0;  // vrednost neodloèene igre	
	
	public Inteligenca (int globina) {
		super("raèunalnik igra z globino " + globina);
		this.globina = globina;
	}	
	
	public static OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		// Èe sem raèunalnik, maksimiramo oceno z zaèetno oceno PORAZ
		// Èe sem pa èlovek, minimiziramo oceno z zaèetno oceno ZMAGA
		if (igra.getIgralecNaPotezi() == jaz) {ocena = PORAZ;} else {ocena = ZMAGA;}
		List<Koordinati> moznePoteze = igra.seznamMoznihPotez;
		Koordinati kandidat = moznePoteze.get(0); // Možno je, da se ne spremeni vrednost kanditata. Zato ne more biti null.
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj (p);
			int ocenap;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_B: ocenap = (jaz == Igralec.B ? ZMAGA : PORAZ); break;
			case ZMAGA_C: ocenap = (jaz == Igralec.C ? ZMAGA : PORAZ); break;
			case NEODLOCENO: ocenap = NEODLOC; break;
			default:
				// Nekdo je na potezi
				if (globina == 1) ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				else ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz).ocena;
			}
			if (igra.getIgralecNaPotezi() == jaz) { // Maksimiramo oceno
				if (ocenap > ocena) { // mora biti > namesto >=
					ocena = ocenap;
					kandidat = p;
					alpha = Math.max(alpha,ocena);
				}
			} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
				if (ocenap < ocena) { // mora biti < namesto <=
					ocena = ocenap;
					kandidat = p;
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
				return new OcenjenaPoteza (kandidat, ocena);
		}
		return new OcenjenaPoteza (kandidat, ocena);
	}
	
	// vrne seznam vseh potez, ki imajo najveèjo vrednost z vidike trenutnega igralca na potezi
	public static List<OcenjenaPoteza> minimaxPoteze(Igra igra, int globina) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.seznamMoznihPotez;
		List<OcenjenaPoteza> nekajNajboljsihPotez = nekajNajboljseOcenjenihPotez(moznePoteze, igra);
		for (OcenjenaPoteza op: nekajNajboljsihPotez) {
			int ocena;
			if (globina==1) ocena = op.ocena;
			else {
				Igra kopijaIgre = new Igra(igra); 
				kopijaIgre.odigraj(op.poteza); //poskusimo vsako potezo v novi kopiji igre
				List<OcenjenaPoteza> test = minimaxPoteze(kopijaIgre, globina-1);
				System.out.println(test.size());
				ocena = //negacija ocene z vidike drugega igralca
						-test.get(0).ocena;  // TODO ko so samo se 3 mozne poteze ga nekej zmoti
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(op.poteza, ocena));			
		}
		return najboljsePoteze.list();
	}
	
	public static List<OcenjenaPoteza> nekajNajboljseOcenjenihPotez(List<Koordinati> moznePoteze, Igra igra) {
		int velikost;
		if (moznePoteze.size() < 4) velikost = moznePoteze.size();
		else velikost = (int) moznePoteze.size()/3;
		OcenjenaPotezaBuffer buffer = new OcenjenaPotezaBuffer(velikost);
		System.out.print(Math.max((int) moznePoteze.size()/3, 3));
		for (Koordinati p : moznePoteze) {
			Igra kopijaIgre = new Igra(igra); 
			kopijaIgre.odigraj(p); //poskusimo vsako potezo v novi kopiji igre
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_C:
			case ZMAGA_B: ocena = ZMAGA; break; // p je zmagovalna poteza
			case NEODLOCENO: ocena = NEODLOC; break;
			default: //nekdo je na potezi
			ocena = OceniPozicijo.oceniPozicijo(kopijaIgre, igra.getIgralecNaPotezi());
			OcenjenaPoteza op = new OcenjenaPoteza(p, ocena);
			buffer.add(op);
			}
		}
		return buffer.list(); 
	}
	
	public Koordinati izberiPotezo (Igra igra) {
		if (igra.getAlgoritem() == Algoritem.MINIMAX) {
			List<OcenjenaPoteza> ocenjenePoteze = minimaxPoteze(igra, globina);
			int i = RANDOM.nextInt(ocenjenePoteze.size());	
			return ocenjenePoteze.get(i).poteza;
		}
		else return alphabetaPoteze(igra, globina, PORAZ, ZMAGA, igra.getIgralecNaPotezi()).poteza;
	}
}
