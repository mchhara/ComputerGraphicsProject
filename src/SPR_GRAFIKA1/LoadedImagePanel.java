package SPR_GRAFIKA1;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class LoadedImagePanel extends JPanel implements ActionListener {

	JButton btn;
	JFileChooser jfc;
	File image_original = null;
	BufferedImage image_toShow;
	JComboBox combobox;

	public LoadedImagePanel() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);
		g2d.setColor(Color.BLACK);

		g2d.drawImage(image_toShow, 0, 0, null);
		g2d.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (btn == source) {
			int returnVal = jfc.showOpenDialog(this);

			if (returnVal == JFileChooser.APPROVE_OPTION) {
				image_original = jfc.getSelectedFile();

				try {
					image_toShow = ImageIO.read(image_original);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else if (combobox == source) {
			String filter = combobox.getSelectedItem().toString();
			try {
				switch (filter) {
				
				case "brak":
					image_toShow = ImageIO.read(image_original);
					break;
				
				case "Filtr wygładzający":
					FiltrWygladzajacy fw = new FiltrWygladzajacy(image_original);
					image_toShow = fw.filter();
					break;
					
				case "Filtr medianowy":
					FiltrMedianowy fm = new FiltrMedianowy(image_original);
					image_toShow = fm.filter();
					break;
				
				case "Filtr wykrywania krawędzi":
					FiltrKrawedziowy fk = new FiltrKrawedziowy(image_original);
					image_toShow = fk.filter();
					break;

				case "Dylatacja":
					FiltrDylatacja fd = new FiltrDylatacja(image_original);
					image_toShow = fd.filter();
					break;

				case "Erozja":
					FiltrErozja fe = new FiltrErozja(image_original);
					image_toShow = fe.filter();
					break;

				}
			} catch (IOException e1) {

			}
		}

		repaint();
	}

	public void injectBtn(JButton btn, JFileChooser jfc) {
		btn.addActionListener(this);
		this.btn = btn;
		this.jfc = jfc;
	}

	public void injectSelect(JComboBox jcb) {
		combobox = jcb;
		combobox.addActionListener(this);
	}
}
