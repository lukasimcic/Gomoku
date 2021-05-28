package logika;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import splosno.Koordinati;

/**
 * Logika potrebna za potek igre.
 */
public class Igra {
	public int N; // velikost igralne plošèe je NxN
	public static final int M = 5; // število zaporednih polj iste barve, ki zadostuje za zmago
	
	private Polje[][] plosca; // Igralna plošèa
	
	private Hashtable<Vrsta, Integer> vrste; // kombinacije zaporednih 5 polj, ki lahko dajo zmago
	
	private Koordinati zadnjaPoteza;
	private Koordinati predzadnjaPoteza;
	
	public Igralec naPotezi; // Igralec, ki je trenutno na potezi.
	
	public List<Koordinati> seznamMoznihPotez = new ArrayList<>(); // seznam polj na igralni plošèi, ki so še prazna
	
	public static Algoritem algoritem = Algoritem.ALFABETA; // raèunalnik priène igro z alfa beta algoritmom
	
	/** 
	 * Vse potrebno za prièetek nove igre
	 * 
	 * @param N dolžina/širina igralne plošèe
	 */
	public Igra(int N) {
		this.N = N;
		this.vrste = vrste(N);
		setAlgoritem(algoritem);
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		seznamMoznihPotez = trenutneMoznePoteze();
		naPotezi = Igralec.C;
	} 
	
	/** 
	 * Ustvari kopijo igre
	 * 
	 * @param igra kopijo ki jo igre želimo
	 */
	public Igra(Igra igra) {
		this.N = igra.N;
		this.vrste = vrste(N);
		this.plosca = new Polje[N][N];
		setAlgoritem(igra.getAlgoritem());
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.plosca[i][j] = igra.plosca[i][j];
			}
		}
		this.seznamMoznihPotez = trenutneMoznePoteze();
		this.naPotezi = igra.naPotezi;
	}
	
	//getter in setter za veè atributov
	
	public Igralec getIgralecNaPotezi() {
		return naPotezi;
	}
	
	public Polje[][] getPlosca () {
		return plosca;
	}
	
	public int getN () {
		return N;
	}
	
	public Hashtable<Vrsta, Integer> getVrste () {
		return vrste;
	}
	
	public static void setAlgoritem (Algoritem alg) {
		Igra.algoritem = alg;
	}
	
	public Algoritem getAlgoritem() {
		return algoritem;
	}
	
	/** 
	 * Ustvari seznam vseh možnih peteric - vrstic dolžine 5
	 * 
	 * @param N dolžina/širina igralne plošèe
	 * @return seznam peteric
	 */
	private Hashtable<Vrsta, Integer> vrste(int N) {
		Hashtable<Vrsta, Integer> vrste = new Hashtable<Vrsta, Integer>();
		int[][] smer = {{1,0}, {0,1}, {1,1}, {1,-1}};
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < N; y++) {
				for (int[] s : smer) {
					int dx = s[0];
					int dy = s[1];
					// èe je skrajno polje peterice še na plošèi, jo dodamo med peterice
					if ((0 <= x + (M-1) * dx ) && (x + (M-1) * dx < N) && 
						(0 <= y + (M-1) * dy) && (y + (M-1) * dy < N)) {
						int[] vrsta_x = new int[M];
						int[] vrsta_y = new int[M];
						for (int k = 0; k < M; k++) {
							vrsta_x[k] = x + dx * k;
							vrsta_y[k] = y + dy * k;
						}
						vrste.put(new Vrsta(vrsta_x, vrsta_y), 0);
					}
				}
			}
		}
		return vrste;
	}
	
	/**
	 * Vsa prazna polja na igralni plošèi, to so vse možne poteze.
	 * 
	 * @return možne poteze
	 */
	public List<Koordinati> trenutneMoznePoteze() {
		LinkedList<Koordinati> trenutnoMozne = new LinkedList<Koordinati>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					trenutnoMozne.add(new Koordinati(i, j));
				}
			}
		}
		return trenutnoMozne;
	}
	
	public boolean veljavnaPoteza(Koordinati p) {
		return seznamMoznihPotez.contains(p);
	}
	
	/**
	 * Doloèi zmagovalno vrsto, èe ta obstaja.
	 * 
	 * @return zmagovalna vrsta/null
	 */
	public Vrsta zmagovalnaVrsta() {
		for (Map.Entry<Vrsta, Integer> entry : vrste.entrySet()) {
			if (entry.getValue() == Igra.M) {
				return entry.getKey();
			}
		}
		return null;
	}
	
	/** 
	 * Trenutno stanje v igri.
	 * 
	 * @return trenutno stanje
	 */
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		Vrsta t = zmagovalnaVrsta();
		if (t != null) {
			switch (plosca[t.xList[0]][t.yList[0]]) {
			case C: return Stanje.ZMAGA_C; 
			case B: return Stanje.ZMAGA_B;
			case PRAZNO: assert false;
			}
		}
		// Ali imamo kakšno prazno polje?
		// Èe ga imamo, igre ni konec in je nekdo na potezi.
		if (seznamMoznihPotez.size() != 0) {
			return Stanje.V_TEKU;
		}
		// Igralna plošèa je polna, rezultat je neodloèen.
		return Stanje.NEODLOCENO;
	}
	
	public boolean konec() {
		return stanje() != Stanje.V_TEKU;
	}
	
	/**
	 * Odigra potezo, èe je izbrano polje prazno in jo izbriše iz seznama možnih potez.
	 * 
	 * @param p koordinati poteze
	 * @return vrednost izjave, da je bilo polje prazno
	 */
	public boolean odigraj(Koordinati p) {
		if (plosca[p.getX()][p.getY()] == Polje.PRAZNO) {
			plosca[p.getX()][p.getY()] = naPotezi.getPolje();
			seznamMoznihPotez.remove(p);
			naPotezi = naPotezi.nasprotnik();
			
			// spremeni vrednosti vrst, neuporabne vrste izbrise iz slovarja
			List<Vrsta> neuporabneVrste = new ArrayList<Vrsta>();
			for (Vrsta v : vrste.keySet()) {
				int ocena = v.ocenaVrste(this);
				if (ocena == Igra.M + 1) {
					neuporabneVrste.add(v);
				}
				vrste.replace(v, ocena);
			}
			vrste.keySet().removeAll(neuporabneVrste);
			
			predzadnjaPoteza = zadnjaPoteza;
			zadnjaPoteza = p;
			
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Razveljavi zadnjo odigrano potezo.
	 */
	public void razveljaviZadnjoPotezo() {
		if (zadnjaPoteza != null) {
			plosca[zadnjaPoteza.getX()][zadnjaPoteza.getY()] = Polje.PRAZNO;
		
			seznamMoznihPotez.add(zadnjaPoteza);
			naPotezi = naPotezi.nasprotnik();
			
			List<Vrsta> neuporabneVrste = new ArrayList<Vrsta>();
			for (Vrsta v : vrste(N).keySet()) {
				int ocena = v.ocenaVrste(this);
				if (ocena == Igra.M + 1) {
					neuporabneVrste.add(v);
				}
				vrste.replace(v, ocena);
			}
			vrste.keySet().removeAll(neuporabneVrste);
			
			zadnjaPoteza = predzadnjaPoteza;
			predzadnjaPoteza = null;
		}
			
	}

	
}