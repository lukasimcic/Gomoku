package logika;

/**
 * Možni igralci.
 */

public enum Igralec {
	C, B;
	
	public String ime = "ime";

	public Igralec nasprotnik() {
		return (this == C ? B : C);
	}

	public Polje getPolje() {
		return (this == C ? Polje.C : Polje.B);
	}
	
	public String toString() {
		return ime;
	}
}
