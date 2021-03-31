package logika;

import java.util.Arrays;

/**
 * Objekt, ki predstavlja eno vrsto na plo��i.
 */
public class Vrsta {
	// Vrsta na plo��i je predstavljena z dvema tabelama dolo�ine N.
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
