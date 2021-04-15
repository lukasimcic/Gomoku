package vodja;

public enum VrstaIgralca {
	R, C; 

	@Override
	public String toString() {
		switch (this) {
		case C: return "èlovek";
		case R: return "raèunalnik";
		default: assert false; return "";
		}
	}

}
