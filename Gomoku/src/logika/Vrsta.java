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
	
	public boolean vsebuje(int x0, int y0) {
		boolean vsebuje = false;
		for (int x : xList) {
			if (x == x0) {
				vsebuje = true;
				break;
			}
		}
		if (! vsebuje) return false;
		for (int y : yList) {
			if (y == y0) {
				return true;
			}
		}
		return false;
	}
	
	// oceni dano peterico iz perspektive podanega igralca
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
		if (count_B > 0 && count_C > 0) { return 0; }
		else if (igra.getIgralecNaPotezi() == Igralec.C) { return count_B - count_C; }
		else { return count_C - count_B; }
	}
	
}
