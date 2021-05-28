package inteligenca;

import java.util.List;
import java.util.Random;

import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;
import logika.Algoritem;
import splosno.KdoIgra;

/**
 * Skrbi za pametno oz. smiselno igranje raèunalniškega igralca.
 *
 */
public class Inteligenca extends KdoIgra {
	
	private int globina;
	
	private static final Random RANDOM = new Random();
	
	private static final int ZMAGA = 1000000; // vrednost zmage
	private static final int PORAZ = -ZMAGA;  // vrednost izgube
	private static final int NEODLOC = 0;  // vrednost neodlocene igre	
	
	/**
	 * 
	 * Nov objekt inteligence.
	 * 
	 * @param globina globina rekurzije v algoritmih 
	 */
	public Inteligenca (int globina) {
		super("racunalnik igra z globino " + globina);
		this.globina = globina;
	}	
	
	// pri alfa beti je problem, da racunalnik ne naredi poteze, kjer bi blokiral nasprotnikovo zmago.
	// problema sta dva:
	// 1. vcasih ta poteza ni vkljucena v nekajNajboljsihPotez
	// 2. v tistem krogu se druge poteze ne evalvirajo pravilno
	
	/**
	 * Uporaba Alpha-Beta algoritma za doloèitev potez raèunalniškega igralca.
	 * 
	 * @param igra v kateri igri igramo
	 * @param globina globina rekurzije
	 * @param alpha alfa
	 * @param beta beta
	 * @param jaz iz katere perspektive ocenjujemo
	 * @param nivo 
	 * @return raèunalnikova poteza
	 */
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
			if (op.ocena == ZMAGA && (nivo % 2 == 1)) return op;
			if (globina == 1 || moznePoteze.size() == 1 || op.ocena == ZMAGA || op.ocena == PORAZ) ocenap = op.ocena;
			else {
				Igra kopijaIgre = new Igra(igra); 
				kopijaIgre.odigraj(op.poteza); //poskusimo vsako potezo v novi kopiji igre
				ocenap = //negacija ocene z vidike drugega igralca
						-alfabetaPoteze(kopijaIgre, globina-1, alpha, beta, jaz, nivo+1).ocena;
				// if (nivo == 0) System.out.println(op + " koncna ocena je " + ocenap);
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
	
	// TODO: zakaj rabimo nivo v alfa-beta? kje se ga nastavi?
	
	/**
	 * Seznam vseh potez, ki imajo najvecjo vrednost z vidike trenutnega igralca na potezi.
	 * Raèunalnik bo za svojo potezo izbral eno izmed teh in ne vedno iste, zato je igra manj monotona.
	 * 
	 * @param igra igra v kateri išèemo ta seznam
	 * @param globina globina rekurzije v algoritmu
	 * @param nivo
	 * @return seznam najboljše ocenjenih potez
	 */
	public static List<OcenjenaPoteza> minimaxPoteze(Igra igra, int globina, int nivo) {
		NajboljseOcenjenePoteze najboljsePoteze = new NajboljseOcenjenePoteze();
		List<Koordinati> moznePoteze = igra.seznamMoznihPotez;
		List<OcenjenaPoteza> nekajNajboljsihPotez = nekajNajboljseOcenjenihPotez(moznePoteze, igra, nivo); // drevo naredimo le na najboljsi tretini moznih potez
		for (OcenjenaPoteza op: nekajNajboljsihPotez) {
			int ocena;
			if (globina == 1 || moznePoteze.size() == 1 || op.ocena == ZMAGA || op.ocena == PORAZ) ocena = op.ocena;
			else {
				Igra kopijaIgre = new Igra(igra); 
				kopijaIgre.odigraj(op.poteza); //poskusimo vsako potezo v novi kopiji igre
				ocena = //negacija ocene z vidike drugega igralca
						-minimaxPoteze(kopijaIgre, globina-1, nivo+1).get(0).ocena;
				if (nivo == 0) System.out.println(op + " koncna ocena je " + ocena);
			}
			najboljsePoteze.addIfBest(new OcenjenaPoteza(op.poteza, ocena));	
		}
		return najboljsePoteze.list();
	}
	
	/**
	 * Seznam statièno ocenjenih najboljših potez, na katerih se nato uporablja minimax ali alfa-beta algoritem.
	 * Pripomore k boljši èasovni uèinkovitosti algoritmov.
	 * 
	 * @param moznePoteze veljavne poteze v igri - prazna polja
	 * @param igra igra katero naj metoda uporablja
	 * @param nivo kako globoko smo v algoritmu, potrebuje se za doloèanje števila zaèetnih najboljših potez
	 * @return seznam nekaj najboljše ocenjenih potez
	 */
	public static List<OcenjenaPoteza> nekajNajboljseOcenjenihPotez(List<Koordinati> moznePoteze, Igra igra, int nivo) {
		int steviloMoznihPotez = moznePoteze.size();
		int velikost = (int) 3 * steviloMoznihPotez / (5 + 2 * nivo);
		OcenjenaPotezaBuffer buffer;
		if (steviloMoznihPotez < 3) buffer = new OcenjenaPotezaBuffer(steviloMoznihPotez);
		else buffer = new OcenjenaPotezaBuffer(velikost);
		// buffer = new OcenjenaPotezaBuffer(steviloMoznihPotez);
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
	
	// TODO: s katere perspektive ocenjuje
	
	/**
	 * Ocena pozicije v igri.
	 * 
	 * @param igra igra v kateri želimo oceniti pozicijo
	 * @return ocena trenutne pozicije
	 */
	public static int oceniPozicijo(Igra igra) {
		int ocena = 0;
		for (int ocenaVrste : igra.getVrste().values()) {
			ocena = ocena + ocenaVrste;
		}
		return ocena;	
	}
	
	/**
	 * Izbere raèunalnikovo potezo glede na izbran algoritem.
	 * 
	 * @param igra igra v kateri naj se metoda izvede
	 * @return raèunalnikova poteza
	 */
	public Koordinati izberiPotezo (Igra igra) {
		if (igra.getAlgoritem() == Algoritem.MINIMAX) {
			List<OcenjenaPoteza> ocenjenePoteze = minimaxPoteze(igra, globina, 0);
			int i = RANDOM.nextInt(ocenjenePoteze.size());	
			return ocenjenePoteze.get(i).poteza;
		}
		else {
			System.out.println("\n");
			return alfabetaPoteze(igra, globina, PORAZ, ZMAGA, igra.getIgralecNaPotezi(), 0).poteza;
		}
	}
}