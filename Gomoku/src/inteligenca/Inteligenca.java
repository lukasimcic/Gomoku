package inteligenca;

import logika.Igra;

import splosno.Koordinati;
import splosno.KdoIgra;

public class Inteligenca extends KdoIgra {
	
	public Inteligenca (String ime) {
		super(ime);
	}
	
	public Koordinati izberiPotezo (Igra igra) {
		return RandomMinimax.izberiPotezo(igra);
	}

}
