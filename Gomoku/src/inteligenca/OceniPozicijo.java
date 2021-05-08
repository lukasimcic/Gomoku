package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Vrsta;

public class OceniPozicijo {
	
	// Metoda oceniPozicijo za igro TicTacToe
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		for (Vrsta v : igra.getVRSTE()) {     // TODO VRSTE private, static ??
			ocena = ocena + oceniVrsto(v, igra, jaz);
		}
		return ocena;	
	}
	
	// TODO popravi oceniVrsto
	public static int oceniVrsto (Vrsta v, Igra igra, Igralec jaz) {
		Polje[][] plosca = igra.getPlosca();
		int count_B = 0;
		int count_C = 0;
		for (int k = 0; k < Igra.M && (count_B == 0 || count_C == 0); k++) {  // TODO N spet static ??
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


