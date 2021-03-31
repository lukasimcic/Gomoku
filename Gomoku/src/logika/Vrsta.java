package logika;

import java.util.Arrays;

/**
 * Objekt, ki predstavlja eno vrsto na plošèi.
 */
public class Vrsta {
	// Vrsta na plošèi je predstavljena z dvema tabelama doložine N.
	// To sta tabeli x in y koordinat.
	public int[] xList;
	public int[] yList;
	
	public Vrsta(int[] x, int[] y) {  // prej je blo int y[]
		this.xList = x;
		this.yList = y;
	}

	@Override
	public String toString() {
		return "Vrsta [x=" + Arrays.toString(xList) + ", y=" + Arrays.toString(yList) + "]";
	}
}
