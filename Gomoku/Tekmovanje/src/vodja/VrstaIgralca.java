package vodja;

public enum VrstaIgralca {
	R, C; 

	@Override
	public String toString() {
		switch (this) {
		case C: return "�lovek";
		case R: return "ra�unalnik";
		default: assert false; return "";
		}
	}

}
