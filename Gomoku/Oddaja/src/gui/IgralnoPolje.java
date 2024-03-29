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
import splosno.Koordinati;

/**
 * Pravokotno obmo�je, kjer se nahaja igralna povr�ina.  
 *
 */
@SuppressWarnings("serial")
public class IgralnoPolje extends JPanel implements MouseListener {
	
	// velikost igralne plo��e
	public int N;
	
	/**
	 * Novo igralno polje z mre�o velikosti NxN.
	 * 
	 * @param N �tevilo polj v vsakem stolpci/vrstici v mre�i
	 */
	public IgralnoPolje(int N) {
		this.addMouseListener(this);
		this.N = N;
	}
	
	/**
	 * Za�etna velikost igralnega polja.
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(600, 600);
	}
	
	/**
	 * Za�etna to�ka pomembna za centriranje �rt iz mre�e glede na razmerje med �irino in dol�ino glavnega okna.
	 * 
	 * @return za�etna to�ka risanja mre�e - �rt na igralnem polju
	 */
	private double[] startingPoint() {
		double x_0, y_0;
		double begin = (getWidth() - getHeight()) / 2;
		if (begin < 0) {
			x_0 = 0; y_0 = - begin;
			}
		else {
			x_0 = begin; y_0 = 0;
			}
		return new double[] {x_0, y_0};
	}
	
	
	// Relativna �irina �rte
	private final static double LINE_WIDTH = 0.08;
	
	/** �irina enega kvadratka v mre�i - igralnem polju
	 * 
	 * @return �irina
	 */
	private double squareWidth() {
		return Math.min(getWidth(), getHeight()) / N;
	}
	
	// Relativni prostor okoli figur 
	private final static double PADDING = 0.18;
	
	public Color barvaOzadja = new Color(245,222,179);
	
	/*
	 * V grafi�ni kontekst nari�e krog z izbrano barvo, ki predstavlja figuro na polju.
	 */
	private void paintCircle(Graphics2D g2, int i, int j, Igralec barvaIgralca) {
		double x_0 = startingPoint()[0];
		double y_0 = startingPoint()[1];
		double w = squareWidth();
		double d = w * (1.0 - LINE_WIDTH - 2.0 * PADDING); // premer O
		double x = x_0 + w * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = y_0 + w * (j + 0.5 * LINE_WIDTH + PADDING);
		if (barvaIgralca == Igralec.C) {g2.setColor(Color.BLACK);}
		else {g2.setColor(Color.WHITE);}
		g2.fillOval((int)x, (int)y, (int)d , (int)d);
	}
	
	/**
	 * Risanje in barvanje v grafi�nem kontekstu.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;

		double w = squareWidth();
		
		// kvadrat za barvanje igralno plo��o
		g2.setColor(barvaOzadja);
		double x_0 = startingPoint()[0];
		double y_0 = startingPoint()[1];
		g2.fillRect((int) x_0, (int) y_0, N * (int)w, N * (int)w );
		
		
		// barvanje zmagovalne petorke, �e ta obstaja
		Vrsta t = null;
		if (Vodja.igra != null) {t = Vodja.igra.zmagovalnaVrsta();}
		if (t != null) {
			g2.setColor(new Color(255, 255, 196));
			for (int k = 0; k < Igra.M; k++) {
				int i = t.xList[k];
				int j = t.yList[k];
				g2.fillRect((int)(x_0 + w * i), (int)(y_0 + w * j), (int)w, (int)w);
			}
		}
		
		// �rte centrirane glede na okno
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (w * LINE_WIDTH)));
		
		for (int i = 0; i < N + 1; i++) {
			g2.drawLine((int)(x_0 + (i * w)),
					    (int)(y_0),
					    (int)(x_0 + (i * w)),
					    (int)(y_0 + (N * w)));
			g2.drawLine((int)(x_0),
					    (int)(y_0 + (i * w)),
					    (int)(x_0 + (N * w)),
					    (int)(y_0 + (i * w)));
		}
		
		// Na vsakem kvadratku na igralni plo��i se nari�e figura, �e kvadratek ni prazen. 
		Polje[][] plosca;;
		if (Vodja.igra != null) {
			plosca = Vodja.igra.getPlosca();
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					switch(plosca[i][j]) {
					case B: paintCircle(g2, i, j, Igralec.B); break;
					case C: paintCircle(g2, i, j, Igralec.C); break;
					default: break;
					}
				}
			}
		}	
		
	}
	
	/** 
	 * Od�ita mesto �lovekove poteze in jo odigra.
	 *  
	 */
	@Override
	public void mouseClicked(MouseEvent e) {		
		if (Vodja.clovekNaVrsti) {
			int x_0 = (int) startingPoint()[0];
			int y_0 = (int) startingPoint()[1];
			int x = e.getX();
			int y = e.getY();
			int w = (int)(squareWidth());
			int i = (x - x_0) / w ;
			double di = ((x - x_0) % w) / squareWidth() ;
			int j = (y - y_0) / w ;
			double dj = ((y - y_0) % w) / squareWidth() ;
			if (0 <= i && i < N &&
					0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
					0 <= j && j < N && 
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
