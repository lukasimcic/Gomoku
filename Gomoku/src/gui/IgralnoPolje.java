package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import vodja.Vodja;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Vrsta;
import splosno.Koordinati;;


@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener {
	
	public IgralnoPolje() {
		setBackground(Color.YELLOW);
		this.addMouseListener(this);
		
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1000, 1000);
	}

	
	// Relativna širina èrte
	private final static double LINE_WIDTH = 0.08;
	
	// Širina enega kvadratka
	private double squareWidth() {
		return Math.min(getWidth(), getHeight()) / 15;
		/* TODO: stevilo 15 popravi v parameter N iz razreda Igra*/
	}
	
	// Relativni prostor okoli figur 
	private final static double PADDING = 0.18;
	
	/*
	 * V grafièni kontekst nariše figuro - krog z izbrano barvo
	 */
	private void paintCircle(Graphics2D g2, int i, int j, Igralec barvaIgralca) {
		double w = squareWidth();
		double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = w * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = w * (j + 0.5 * LINE_WIDTH + PADDING);
		if (barvaIgralca == Igralec.C) {g2.setColor(Color.BLACK);}
		else {g2.setColor(Color.WHITE);}
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		double w = squareWidth();
		
		/*
		// èe imamo zmagovalno vrstico, njeno ozadje pobarvamo
		Vrsta t = null;
		if (Vodja.igra != null) {t = Vodja.igra.zmagovalnaVrsta();}
		if (t != null) {
			g2.setColor(new Color(255, 255, 196));
			TODO: popravi k < 15 v parameter Igra.N
			for (int k = 0; k < 15; k++) {
				int i = t.xList[k];
				int j = t.yList[k];
				g2.fillRect((int)(w * i), (int)(w * j), (int)w, (int)w);
			}
		}
		*/
		
		// èrte
		// TODO: popravi 15 v Igra.N
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		for (int i = 1; i < 15; i++) {
			g2.drawLine((int)(i * w),
					    (int)(0),
					    (int)(i * w),
					    (int)(15 * w));
			g2.drawLine((int)(0),
					    (int)(i * w),
					    (int)(15 * w),
					    (int)(i * w));
		}
		
		// Na vsakem kvadratku v polju nariše figure, èe kvadratek ni prazen 
		// TODO 15 -> Igra.N
		Polje[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < 15; i++) {
				for (int j = 0; j < 15; j++) {
					switch(plosca[i][j]) {
					case B: paintCircle(g2, i, j, Igralec.B); break;
					case C: paintCircle(g2, i, j, Igralec.C); break;
					default: break;
					}
				}
			}
		}	
		
	}
	
	// spet 15 -> Igra.N
	@Override
	public void mouseClicked(MouseEvent e) {
		if (Vodja.clovekNaVrsti) {
			int x = e.getX();
			int y = e.getY();
			int w = (int)(squareWidth());
			int i = x / w ;
			double di = (x % w) / squareWidth() ;
			int j = y / w ;
			double dj = (y % w) / squareWidth() ;
			if (0 <= i && i < 15 &&
					0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
					0 <= j && j < 15 && 
					0.5 * LINE_WIDTH < dj && dj < 1.0 - 0.5 * LINE_WIDTH) {
				Vodja.igrajClovekovoPotezo (new Koordinati(i, j));
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {		
	}

	@Override
	public void mouseReleased(MouseEvent e) {		
	}

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {		
	}
	
}
