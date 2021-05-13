// O = C, X = B

package logika;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import splosno.Koordinati;

public class Igra {
	public int N; // velikost plosce je NxN
	public static final int M = 5; // gledamo 5 zaporednih polj
	
	private Polje[][] plosca; // Igralno polje
	
	private final List<Vrsta> VRSTE; // mozne kombinacije 5 zapordnih polj
	
	public Igralec naPotezi; // Igralec, ki je trenutno na potezi. Vrednost je poljubna, èe je igre konec (se pravi, lahko je napaèna).
	
	public List<Koordinati> seznamMoznihPotez = new ArrayList<>();
	
	public static Algoritem algoritem = Algoritem.MINIMAX;
	
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
		seznamMoznihPotez = zacetneMoznePoteze();
		naPotezi = Igralec.C;
	} 
	
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
		this.seznamMoznihPotez = igra.seznamMoznihPotez;
		this.naPotezi = igra.naPotezi;
	}

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
	
	private List<Vrsta> vrste(int N) {
		List<Vrsta> vrste = new LinkedList<Vrsta>();
		int[][] smer = {{1,0}, {0,1}, {1,1}, {1,-1}};
		for (int x = 0; x < N; x++) {
			for (int y = 0; y < N; y++) {
				for (int[] s : smer) {
					int dx = s[0];
					int dy = s[1];
					// èe je skrajno polje terice še na plošèi, jo dodamo med terice
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
		for (Vrsta i : vrste) System.out.println(i);
		System.out.println(vrste.size());
		return vrste;
	}

	public List<Koordinati> zacetneMoznePoteze() {
		LinkedList<Koordinati> ps = new LinkedList<Koordinati>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					ps.add(new Koordinati(i, j));
				}
			}
		}
		return ps;
	}
	
	public boolean veljavnaPoteza(Koordinati p) {
		return seznamMoznihPotez.contains(p);
	}
	
	private Igralec cigavaVrsta(Vrsta t) {  // vrsta je 5 zaporednih polj v stolpcu, vrsti alpa diagonali
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
	
	public Vrsta zmagovalnaVrsta() {
		for (Vrsta t : VRSTE) {
			Igralec lastnik = cigavaVrsta(t);
			if (lastnik != null) return t;
		}
		return null;
	}
	
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
		// Èe ga imamo, igre ni konec in je nekdo na potezi
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) return Stanje.V_TEKU;
			}
		}
		// Polje je polno, rezultat je neodloèen
		return Stanje.NEODLOCENO;
	}
	
	public boolean konec() {
		return stanje() != Stanje.V_TEKU;
	}
	
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
	
//	public void razveljaviZadnjoPotezo() {
//		Koordinati p = seznam.remove(poteze.size() - 1);
//		plosca[p.getX()][p.getY()] = Polje.PRAZNO;
//	}

	
}
