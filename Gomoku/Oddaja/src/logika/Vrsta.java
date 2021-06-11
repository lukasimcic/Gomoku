package logika;

import java.util.Arrays;

/**
 * Objekt, ki predstavlja eno vrsto na plošèi.
 */
public class Vrsta {
	
	// Vrsta na plošèi je predstavljena z dvema tabelama doložine M.
	// To sta tabeli x in y koordinat.
	public int[] xList;
	public int[] yList;
	
	public Vrsta(int[] x, int[] y) {
		this.xList = x;
		this.yList = y;
	}

	@Override
	public String toString() {
		return "Vrsta [x=" + Arrays.toString(xList) + ", y=" + Arrays.toString(yList) + "]";
	}
	
	/**
	 * Oceni dano peterico iz perspektive igralca na potezi.
	 * 
	 * @param igra igra v kateri ocenjuje vrsto
	 * @return ocena vrste
	 */
	public int ocenaVrste (Igra igra) {
		Polje[][] plosca = igra.getPlosca();
		int count_B = 0;
		int count_C = 0;
		for (int k = 0; k < Igra.M && (count_B == 0 || count_C == 0); k++) {
			switch (plosca[xList[k]][yList[k]]) {
			case B: count_B += 1; break;
			case C: count_C += 1; break;
			case PRAZNO: break;
			}
		}
		if (count_B > 0 && count_C > 0) return Igra.M + 1;  // ta vrsta se bo izbrisala iz slovarja vrst
		else if (igra.getIgralecNaPotezi() == Igralec.C) { return count_B - count_C; }
		else { return count_C - count_B; }
	}
	
}
