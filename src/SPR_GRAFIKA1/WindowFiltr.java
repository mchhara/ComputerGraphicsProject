package SPR_GRAFIKA1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WindowFiltr extends JFrame {
	
	JFileChooser jfc;
	JButton chooseFileBtn;

	String[] filterTypesList = { "brak", "Filtr wygładzający", "Filtr medianowy", "Filtr wykrywania krawędzi",
			"Dylatacja", "Erozja"};
	
	public WindowFiltr() {
		super("Project 2");
		setLayout(new BorderLayout());

		
		JPanel jp2 = new JPanel();
		jp2.setBackground(Color.LIGHT_GRAY);
		jp2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

		
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "JPG,GIF,PNG Images", "jpg", "gif","png");
		jfc = new JFileChooser();	    
	    jfc.setFileFilter(filter);

		JButton paint = new JButton("Paint");
		jp2.add(paint);

		paint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DrawArea();
				setVisible(false);
			}
		});

	    chooseFileBtn = new JButton("Open file");
	    jp2.add(chooseFileBtn);

	    
	    String[] filterTypes = filterTypesList;
		JComboBox filterList = new JComboBox(filterTypes);
		
		jp2.add(filterList);
	    		
	    add(jp2, BorderLayout.NORTH);
	    


	    LoadedImagePanel imgPane = new LoadedImagePanel();
	    imgPane.injectBtn( chooseFileBtn, jfc );
	    imgPane.injectSelect( filterList );
	    
		add(imgPane, BorderLayout.CENTER);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
		setVisible(true);
	}

}
