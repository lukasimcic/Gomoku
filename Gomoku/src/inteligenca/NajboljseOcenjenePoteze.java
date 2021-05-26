package inteligenca;

import java.util.List;

/**
 * Seznam najboljše ocenjenih potez. 
*/
public class NajboljseOcenjenePoteze {
	
	private OcenjenaPotezaBuffer buffer;
		
	public NajboljseOcenjenePoteze() {
		this.buffer = new OcenjenaPotezaBuffer (10);
	}
	
	public void addIfBest(OcenjenaPoteza ocenjenaPoteza) {
		if (buffer.getBuffer().isEmpty()) buffer.add(ocenjenaPoteza);
		else {
			OcenjenaPoteza op = buffer.getBuffer().getFirst();
			switch (ocenjenaPoteza.compareTo(op)) {
			case 1: 
				buffer.getBuffer().clear();  // ocenjenaPoteza > op
			case 0: // ali 1
				buffer.add(ocenjenaPoteza); // ocenjenaPoteza >= op
			}			
		}
	}
	
	
	public List<OcenjenaPoteza> list() {
		return buffer.list();
	}

}
