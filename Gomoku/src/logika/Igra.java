// O = C, X = B

package logika;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import splosno.Koordinati;

public class Igra {
	public int N; // velikost igralne plošèe je NxN
	public static final int M = 5; // število zaporednih polj iste barve, ki zadostuje za zmago
	
	private Polje[][] plosca; // Igralna plošèa
	
	private final List<Vrsta> VRSTE; // vse možne kombinacije 5 zaporednih polj
	
	public Igralec naPotezi; // Igralec, ki je trenutno na potezi.
	
	public List<Koordinati> seznamMoznihPotez = new ArrayList<>(); // seznam polj na igralni plošèi, ki so še prazna
	
	public static Algoritem algoritem = Algoritem.MINIMAX; // raèunalnik priène igro z minimax algoritmom
	
	// vse potrebno za prièetek nove igre
	public Igra(int N) {
		this.N = N;
		this.VRSTE = vrste(N);
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
	
	// identièna kopija igre
	public Igra(Igra igra) {
		this.N = igra.N;
		this.VRSTE = vrste(N);
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
	
	public List<Vrsta> getVRSTE () {
		return VRSTE;
	}
	
	public static void setAlgoritem (Algoritem alg) {
		Igra.algoritem = alg;
	}
	
	public Algoritem getAlgoritem() {
		return algoritem;
	}
	
	// ustvari seznam vseh možnih peteric - vrstic dolžine 5
	private List<Vrsta> vrste(int N) {
		List<Vrsta> vrste = new LinkedList<Vrsta>();
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
						vrste.add(new Vrsta(vrsta_x, vrsta_y));
					}
				}
			}
		}
		return vrste;
	}
	
	// vsa prazna polja na igralni plošèi
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
	
	/* lastnik zmagovalne vrste, èe je le ta zmagovalna
	 * Vrsta je v tem primeru peterica polj iz treh možnih smeri: vodoravno, navpièno ali diagonalno.
	 */
	private Igralec cigavaVrsta(Vrsta t) {  
		int count_B = 0;
		int count_C = 0;
		for (int k = 0; k < M && (count_B == 0 || count_C == 0); k++) {
			switch (plosca[t.xList[k]][t.yList[k]]) {
			case C: count_C += 1; break;
			case B: count_B += 1; break;
			case PRAZNO: break;
			}
		}
		if (count_C == M) { return Igralec.C; }
		else if (count_B == M) { return Igralec.B; }
		else { return null; }
	}
	
	// doloèi zmagovalno vrsto
	public Vrsta zmagovalnaVrsta() {
		for (Vrsta t : VRSTE) {
			Igralec lastnik = cigavaVrsta(t);
			if (lastnik != null) return t;
		}
		return null;
	}
	
	// trenutno stanje v igri
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
	
	// odigra potezo, èe je izbrano polje prazno in jo izbriše iz seznama možnih potez
	public boolean odigraj(Koordinati p) {
		if (plosca[p.getX()][p.getY()] == Polje.PRAZNO) {
			plosca[p.getX()][p.getY()] = naPotezi.getPolje();
			seznamMoznihPotez.remove(p);
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		else {
			return false;
		}
	}

	
}
