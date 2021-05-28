package logika;

/**
 * Mo�ni igralci.
 */

public enum Igralec {
	C, B;
	
	public static String imeCrni = "crni";
	public static String imeBeli = "beli";

	public Igralec nasprotnik() {
		return (this == C ? B : C);
	}

	public Polje getPolje() {
		return (this == C ? Polje.C : Polje.B);
	}
	
	public String toString() {
		return (this == C ? imeCrni : imeBeli);
	}
}
