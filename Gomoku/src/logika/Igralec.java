package logika;

/**
 * Možni igralci.
 */

public enum Igralec {
	C, B;
	
	public String imeC = "ime_èrni";
	public String imeB = "ime_beli";

	public Igralec nasprotnik() {
		return (this == C ? B : C);
	}

	public Polje getPolje() {
		return (this == C ? Polje.C : Polje.B);
	}
	
	public String toString() {
		return (this == C ? imeC : imeB);
	}
}
