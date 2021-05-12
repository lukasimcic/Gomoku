package inteligenca;

import logika.Algoritem;
import logika.Igra;

import splosno.Koordinati;
import splosno.KdoIgra;

public class Inteligenca extends KdoIgra {
	
	private int globina;
	
	public Inteligenca (int globina) {
		super("računalnik igra z globino " + globina);
		this.globina = globina;
	}
	
	public Koordinati izberiPotezo (Igra igra) {
		if (igra.getAlgoritem() == Algoritem.MINIMAX) return RandomMinimax.izberiPotezo(igra, this.globina);
		else return RandomMinimax.izberiPotezo(igra, this.globina); // TODO spremenit v alfabeta
	}

}
