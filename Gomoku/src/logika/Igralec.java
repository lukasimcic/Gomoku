package logika;

/**
 * Mo�ni igralci.
 */

public enum Igralec {
	C, B;

	public Igralec nasprotnik() {
		return (this == C ? B : C);
	}

	public Polje getPolje() {
		return (this == C ? Polje.C : Polje.B);
	}
	
	public String toString() {
		return (this == C ? "�rni" : "beli");
	}
}
