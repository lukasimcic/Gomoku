import gui.GlavnoOkno;
import vodja.Vodja;

/**
 * Za�ene uporabni�ki vmesnik, t. j. okno v katerem lahko uporabnik igra Gomoku.
 * 
 * @author TS in LS
 * 
 */
public class Gomoku {
	
	public static void main(String[] args) {
		GlavnoOkno glavno_okno = new GlavnoOkno(10);
		glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno;
	}

}
