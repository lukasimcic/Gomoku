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
	
	// ustvari novo igro - objekt razreda Igra in jo požene z metodo igramo()
	public static void igramoNovoIgro (int N) {
		igra = new Igra (N);
		igramo ();
	}
	
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

	// v igri raè-raè na vsaki potezi èaka 2s in popravi grafiko - plošèo s figurami šele ko se igra konèa.
//	public static void igrajRacunalnikovoPotezo() {
//		List<Koordinati> moznePoteze = igra.poteze();
//		try {TimeUnit.SECONDS.sleep(2);} catch (Exception e) {};
//		int randomIndex = random.nextInt(moznePoteze.size());
//		Koordinati poteza = moznePoteze.get(randomIndex);
//		igra.odigraj(poteza);
//		igramo ();
//	}
	
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca(2);
	
	// Odigra raèunalnikovo potezo. Preden odigra poèaka 2s, nato odigra in posodobi grafiko-nariše potezo.
	public static void igrajRacunalnikovoPotezo() {
		Igra zacetnaIgra = igra;
		SwingWorker<Koordinati, Void> worker = new SwingWorker<Koordinati, Void> () {
			@Override
			protected Koordinati doInBackground() {
				Koordinati poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				return poteza;
			}
			@Override
			protected void done () {
				Koordinati poteza = null;
				try {poteza = get();} catch (Exception e) {};
				// preveri èe uporabnik med izvajanjem ni spremenil igre, t. j. da ni v meniju izbral nove igre, saj potem ne želimo odigrati poteze na stari igri
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo ();
				}
			}
		};
		worker.execute();
	}
		
	public static void igrajClovekovoPotezo(Koordinati poteza) {
		if (igra.odigraj(poteza)) clovekNaVrsti = false;
		igramo ();
	}


}
