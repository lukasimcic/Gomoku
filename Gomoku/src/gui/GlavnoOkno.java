package gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EnumMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import vodja.Vodja;
import vodja.VrstaIgralca;
import logika.Igralec;


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


	private int N;

	/**
	 * Ustvari novo glavno okno in prièni igrati igro.
	 */
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
		polje = new IgralnoPolje();

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
			// TODO: moramo povsod spremeniti iz 15 v N
			return;
		}
		else if (e.getSource() == ime) {
			JTextField imeBeli = new JTextField();
			JTextField imeCrni = new JTextField();
			Component[] polja = {
				new JLabel("Vnesi ime belega: "), imeBeli, 
				new JLabel("Vnesi ime èrnega: "), imeCrni
			};
			int izbira = JOptionPane.showConfirmDialog(this, polja, "Input", JOptionPane.OK_CANCEL_OPTION);
			if (izbira == JOptionPane.OK_OPTION && imeBeli.getText().matches("\\w+") && imeCrni.getText().matches("\\w+")) {
				return;
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
				status.setText("Na potezi je " + Vodja.igra.naPotezi + 
						" - " + Vodja.vrstaIgralca.get(Vodja.igra.naPotezi) + "."); 
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
