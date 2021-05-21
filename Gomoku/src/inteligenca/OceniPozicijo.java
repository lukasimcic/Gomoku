package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Vrsta;

public class OceniPozicijo {
	
	// statièna ocena trenutne pozicije v igri	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		for (Vrsta v : igra.getVRSTE()) {
			ocena = ocena + oceniVrsto(v, igra, jaz);
		}
		return ocena;	
	}
	
	// oceni dano peterico iz perspektive podanega igralca
	public static int oceniVrsto (Vrsta v, Igra igra, Igralec jaz) {
		Polje[][] plosca = igra.getPlosca();
		int count_B = 0;
		int count_C = 0;
		for (int k = 0; k < Igra.M && (count_B == 0 || count_C == 0); k++) {
			switch (plosca[v.xList[k]][v.yList[k]]) {
			case B: count_B += 1; break;
			case C: count_C += 1; break;
			case PRAZNO: break;
			}
		}
		if (count_B > 0 && count_C > 0) { return 0; }
		else if (jaz == Igralec.B) { return count_B - count_C; }
		else { return count_C - count_B; }
	}
	

}


