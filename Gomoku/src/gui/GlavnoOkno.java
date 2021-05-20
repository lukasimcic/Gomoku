package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import vodja.Vodja;
import vodja.VrstaIgralca;
import logika.Algoritem;
import logika.Igra;
import logika.Igralec;
import logika.Stanje;


@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	
	private IgralnoPolje polje;

	//Statusna vrstica v spodnjem delu okna
	private JLabel status;
	
	// Izbire v menujih
	private JMenuItem igraClovekRacunalnik;
	private JMenuItem igraRacunalnikClovek;
	private JMenuItem igraClovekClovek;
	private JMenuItem igraRacunalnikRacunalnik;
	private JMenuItem velikost;
	private JMenuItem ime;
	private JMenuItem algoritem;
	private JMenuItem cas;
	private JMenuItem barvaOzadja;

	// velikost polja
	private int N;

	
	public GlavnoOkno(int N) {
		
		
		this.N = N;
		this.setTitle("Gomoku");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
	
		
		// menu
		
		JMenuBar menu_bar = new JMenuBar();
		this.setJMenuBar(menu_bar);
		
		JMenu vrsta_igre = dodajMenu(menu_bar, "vrsta igre");
		JMenu velikost_igre = dodajMenu(menu_bar, "velikost igre");
		JMenu lastnosti_igralcev = dodajMenu(menu_bar, "lastnosti igralcev");
		JMenu lastnosti_graficnega_vmesnika = dodajMenu(menu_bar, "lastnosti grafiènega vmesnika");
		
		igraClovekRacunalnik = dodajMenuItem(vrsta_igre, "èlovek – raèunalnik");
		igraRacunalnikClovek = dodajMenuItem(vrsta_igre, "raèunalnik – èlovek");
		igraClovekClovek = dodajMenuItem(vrsta_igre, "èlovek – èlovek");
		igraRacunalnikRacunalnik = dodajMenuItem(vrsta_igre, "raèunalnik – raèunalnik");
		
		velikost = dodajMenuItem(velikost_igre, "izberi velikost");
		
		ime = dodajMenuItem(lastnosti_igralcev, "imena igralcev");
		algoritem = dodajMenuItem(lastnosti_igralcev, "algoritem");
		cas = dodajMenuItem(lastnosti_igralcev, "èas raèunalnika");
		
		barvaOzadja = dodajMenuItem(lastnosti_graficnega_vmesnika, "barva ozadja");
		
		
		// igralno polje
		
		polje = new IgralnoPolje(N);

		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.anchor = GridBagConstraints.CENTER;
		polje_layout.weightx = 1.0;
		polje_layout.weighty = 1.0;
		polje_layout.fill = GridBagConstraints.BOTH;
		getContentPane().add(polje, polje_layout);
		
		
		// statusna vrstica za sporoèila
		
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
							    status.getFont().getStyle(),
							    20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.anchor = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		status.setText("Izberite vrsto igre!");
		
	}
	
	
	public JMenu dodajMenu(JMenuBar menubar, String naslov) {
		JMenu menu = new JMenu(naslov);
		menubar.add(menu);
		return menu;
	}
	
	public JMenuItem dodajMenuItem(JMenu menu, String naslov) {
		JMenuItem menuitem = new JMenuItem(naslov);
		menu.add(menuitem);
		menuitem.addActionListener(this);
		return menuitem;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == igraClovekRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.C, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.R);
			Vodja.igramoNovoIgro(this.N);
		} 
		else if (e.getSource() == igraRacunalnikClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.C, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.C);
			Vodja.igramoNovoIgro(this.N);
		} 
		else if (e.getSource() == igraClovekClovek) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.C, VrstaIgralca.C); 
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.C);
			Vodja.igramoNovoIgro(this.N);
		} 
		else if (e.getSource() == igraRacunalnikRacunalnik) {
			Vodja.vrstaIgralca = new EnumMap<Igralec,VrstaIgralca>(Igralec.class);
			Vodja.vrstaIgralca.put(Igralec.C, VrstaIgralca.R); 
			Vodja.vrstaIgralca.put(Igralec.B, VrstaIgralca.R);
			Vodja.igramoNovoIgro(this.N);
		} 
		else if (e.getSource() == velikost) {
			String velikost = JOptionPane.showInputDialog(this, "Velikost polja lahko spremenite, èe igra ni v teku.\nVelikost polja: ");
			if (velikost != null && velikost.matches("\\d+")) {
				if (Vodja.igra == null) {
					this.N = Integer.parseInt(velikost);
					polje.N = this.N;
					polje.repaint();
				}
				else {
					this.N = Integer.parseInt(velikost);
					polje.N = this.N;
				}
			}
		}
		else if (e.getSource() == ime) {
			JTextField imeBeli = new JTextField();
			JTextField imeCrni = new JTextField();
			Component[] polja = {
				new JLabel("Vnesi ime belega: "), imeBeli, 
				new JLabel("Vnesi ime èrnega: "), imeCrni
			};
			int izbira = JOptionPane.showConfirmDialog(this, polja, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (izbira == JOptionPane.OK_OPTION) {
				Igralec.imeCrni = imeCrni.getText();
				Igralec.imeBeli = imeBeli.getText();
				return;	
			}
		}
		else if (e.getSource() == algoritem) {
			String[] algoritmi = {"minimax", "alfa beta"};
			int x = JOptionPane.showOptionDialog(this, "Izberite algoritem:",
	                "", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, algoritmi, algoritmi[0]);
			if (x == 0) Igra.setAlgoritem(Algoritem.MINIMAX);
			else if (x == 1) Igra.setAlgoritem(Algoritem.ALFABETA);
			// TODO se ne spremeni prav
		}
		else if (e.getSource() == cas) {
			String casRacunalnika = JOptionPane.showInputDialog(this, "Odzivni èas raèunalnika: ");
			if (casRacunalnika != null && casRacunalnika.matches("\\d+")) {
				Vodja.odzivniCasRacunalnika = Integer.parseInt(casRacunalnika);
			}
		}
		else if (e.getSource() == barvaOzadja) {
			Color barva = JColorChooser.showDialog(this, "Izberi barvo ozadja", polje.barvaOzadja);
			if (barva != null)  {
				polje.barvaOzadja = barva;
				polje.repaint();
			}
		}
	}

	public void osveziGUI() {
		if (Vodja.igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch(Vodja.igra.stanje()) {
			case NEODLOCENO: status.setText("Neodloèeno!"); break;
			case V_TEKU: 
				status.setText("Na potezi je " + Vodja.igra.getIgralecNaPotezi() + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.getIgralecNaPotezi()) + "."); 
				break;
			case ZMAGA_C: 
				status.setText("Zmagal je èrni - " + 
						Vodja.vrstaIgralca.get(Igralec.C) + ".");
				break;
			case ZMAGA_B: 
				status.setText("Zmagal je beli - " + 
						Vodja.vrstaIgralca.get(Igralec.B) + ".");
				break;
			}
		}
		polje.repaint();
	}
	
	
}
