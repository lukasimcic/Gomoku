// O = C, X = B

package logika;

import java.util.LinkedList;
import java.util.List;

public class Igra {
	
	public int N; // velikost plosce je NxN
	public static final int M = 5; // gledamo 5 zaporednih polj
	
	private Polje[][] plosca; // Igralno polje
	
	private final List<Vrsta> VRSTE; // mozne kombinacije 5 zapordnih polj
	
	public Igralec naPotezi; // Igralec, ki je trenutno na potezi. Vrednost je poljubna, èe je igre konec (se pravi, lahko je napaèna).
	
	public Igra(int N) {
		this.N = N;
		this.VRSTE = vrste(N);
		
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		
		naPotezi = Igralec.C;
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
					if ((0 <= x + (M-1) * dx ) && (x + (M-1) * dx < M) && 
						(0 <= y + (M-1) * dy) && (y + (M-1) * dy < M)) {
						int[] vrsta_x = new int[M];
						int[] vrsta_y = new int[M];
						for (int k = 0; k < N; k++) {
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

	public List<Koordinati> moznePoteze() {
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
	
	
	
	
}
