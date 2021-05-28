package inteligenca;

import java.util.List;

/**
 * Buffer najboljše ocenjenih potez. 
*/
public class NajboljseOcenjenePoteze {
	
	private OcenjenaPotezaBuffer buffer;
		
	public NajboljseOcenjenePoteze() {
		this.buffer = new OcenjenaPotezaBuffer (10);
	}
	
	/**
	 * V buffer doda potezo, èe je njena ocena boljša ali enaka ocenam v seznamu.
	 * 
	 * @param ocenjenaPoteza poteza, ki jo zelimo dodati
	 */
	public void addIfBest(OcenjenaPoteza ocenjenaPoteza) {
		if (buffer.getBuffer().isEmpty()) buffer.add(ocenjenaPoteza);
		else {
			OcenjenaPoteza op = buffer.getBuffer().getFirst();
			switch (ocenjenaPoteza.compareTo(op)) {
			case 1: 
				buffer.getBuffer().clear();  // ocenjenaPoteza > op
			case 0:
				buffer.add(ocenjenaPoteza); // ocenjenaPoteza >= op
			}			
		}
	}
	
	
	public List<OcenjenaPoteza> list() {
		return buffer.list();
	}

}
