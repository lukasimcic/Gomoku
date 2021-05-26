package vodja;

import java.util.Map;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import gui.GlavnoOkno;
import inteligenca.Inteligenca;
import logika.Algoritem;
import logika.Igra;
import logika.Igralec;
import logika.Vrsta;
import splosno.Koordinati;

public class Vodja {	
	
	private static long previousTime; // meri cas potez
	
	private static double longestTime; // meri najdaljsi cas poteze v igri
	
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static GlavnoOkno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
	public static int odzivniCasRacunalnika = 0;
	
	private static int globina = 3;
	
	/** Ustvari novo igro in jo zažene.
	 * 
	 * @param N velikost igralne plošèe - N x N
	 */
	public static void igramoNovoIgro (int N) {
		igra = new Igra (N);
		if (Igra.algoritem == Algoritem.MINIMAX) System.out.println("igramo z minimaxom");
		else System.out.println("igramo z alfa beto");
		previousTime = System.currentTimeMillis();
		igramo ();
	}
	
	/**
	 * Potek igre
	 */
	public static void igramo () {
		
		// print-a koliko casa rabi za eno potezo
		long currentTime = System.currentTimeMillis();
		double elapsedTime = (currentTime - previousTime) / 1000.0;
		longestTime = Math.max(longestTime, elapsedTime);
		// System.out.println("Time in seconds : " + elapsedTime);
		previousTime = currentTime;
		
		okno.osveziGUI();
		switch (igra.stanje()) {
		case ZMAGA_C: System.out.println("zmaga z " + longestTime);
		case ZMAGA_B: System.out.println("zmaga z " + longestTime);
		case NEODLOCENO: System.out.println("neodloceno z " + longestTime);
			return; // odhajamo iz metode igramo
		case V_TEKU: 
			Igralec igralec = igra.getIgralecNaPotezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
//				if (Igra.algoritem == Algoritem.MINIMAX) Igra.algoritem = Algoritem.ALFABETA;
//				else Igra.algoritem = Algoritem.MINIMAX;
//				
//				System.out.println(" igra " + igra.getIgralecNaPotezi() + " z algoritmom " + Igra.algoritem);
				//TODO: kaj je s temi vrsticami zgoraj
				
				
				igrajRacunalnikovoPotezo ();
			}
			
		}
	}
	
	// naèin igranja raèunalnika
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca(globina);
	
	/**
	 *  Odigra raèunalnikovo potezo in posodobi grafiko - nariše potezo.
	 *  Preden odigra poèaka 2s oziroma po izbiri.
	 */
	public static void igrajRacunalnikovoPotezo() {
		Igra zacetnaIgra = igra;
		SwingWorker<Koordinati, Void> worker = new SwingWorker<Koordinati, Void> () {
			@Override
			protected Koordinati doInBackground() {
				Koordinati poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(odzivniCasRacunalnika);} catch (Exception e) {};
				return poteza;
			}
			@Override
			protected void done () {
				Koordinati poteza = null;
				try {poteza = get();} catch (Exception e) {e.printStackTrace();};
				/*
				 *  Preveri èe uporabnik med izvajanjem ni spremenil igre, t. j. da ni v meniju izbral nove igre,
				 *  saj ne želimo odigrati poteze na stari igri.
				 */
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo ();
				}
			}
		};
		worker.execute();
	}
	
	/**
	 * Odigra èlovekovo potezo, èe je možna in nadaljuje z igro.
	 * 
	 * @param poteza izbrana poteza 
	 */
	public static void igrajClovekovoPotezo(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo ();
	}


}
