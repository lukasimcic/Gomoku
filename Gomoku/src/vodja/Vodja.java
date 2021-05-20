package vodja;

import java.util.Map;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import gui.GlavnoOkno;
import inteligenca.Inteligenca;
import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;

public class Vodja {	
	
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static GlavnoOkno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
	public static int odzivniCasRacunalnika = 2;
	
	// ustvari novo igro in jo za�ene
	public static void igramoNovoIgro (int N) {
		igra = new Igra (N);
		igramo ();
	}
	
	// potek igre
	public static void igramo () {
		okno.osveziGUI();
		System.out.println(igra.stanje());
		switch (igra.stanje()) {
		case ZMAGA_C: 
		case ZMAGA_B: 
		case NEODLOCENO: 
			return; // odhajamo iz metode igramo
		case V_TEKU: 
			Igralec igralec = igra.getIgralecNaPotezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				igrajRacunalnikovoPotezo ();
			}
		}
	}
	
	// na�in igranja ra�unalnika
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca(3);
	
	// Odigra ra�unalnikovo potezo in posodobi grafiko-nari�e potezo. Preden odigra po�aka 2s oz. po izbiri.
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
				try {poteza = get();} catch (Exception e) {};
				/*
				 *  Preveri �e uporabnik med izvajanjem ni spremenil igre, t. j. da ni v meniju izbral nove igre,
				 *  saj ne �elimo odigrati poteze na stari igri.
				 */
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo ();
				}
			}
		};
		worker.execute();
	}
	
	// odigra �lovekovo potezo, �e je mo�na in nadaljuje z igro
	public static void igrajClovekovoPotezo(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo ();
	}


}
