import gui.GlavnoOkno;
import vodja.Vodja;

public class Gomoku {
	
	public static void main(String[] args) {
		GlavnoOkno glavno_okno = new GlavnoOkno(15);
		glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno;
	}

}
