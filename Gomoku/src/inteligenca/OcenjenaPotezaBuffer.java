package inteligenca;

import java.util.List;
import java.util.LinkedList;

public class OcenjenaPotezaBuffer {
	
	private int velikost;
	
	private LinkedList<OcenjenaPoteza> buffer;
		
	public OcenjenaPotezaBuffer(int velikost) {
		this.velikost = velikost;
		this.buffer = new LinkedList<OcenjenaPoteza> ();
	}
	

	public void add(OcenjenaPoteza ocenjenaPoteza) {
		int i = 0;
		for (OcenjenaPoteza op : getBuffer()) {
			if (ocenjenaPoteza.compareTo(op) != 1) i++;
			else break; // izstopimo iz zanke	
		}
		if (i < velikost) getBuffer().add(i, ocenjenaPoteza);
		if (getBuffer().size() > velikost) getBuffer().removeLast();
	}
	
	
	public List<OcenjenaPoteza> list() {
		return (List<OcenjenaPoteza>) getBuffer();
	}


	public LinkedList<OcenjenaPoteza> getBuffer() {
		return buffer;
	}

}
