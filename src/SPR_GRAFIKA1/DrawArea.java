package SPR_GRAFIKA1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawArea extends JFrame {

	public static Window2 w2 = new Window2();

	public DrawArea() {
		setLayout(new BorderLayout());


		JPanel jp2 = new JPanel();
		jp2.setBackground(Color.LIGHT_GRAY);
		jp2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

		JButton loadBtn = new JButton("Load");
		jp2.add(loadBtn);

		JButton saveBtn = new JButton("Save");
		jp2.add(saveBtn);


		String[] petStrings = { "Line", "Rectangle", "Oval" };
		JComboBox shapesList = new JComboBox(petStrings);

		jp2.add(shapesList);

		JTextField x1 = new JTextField("0", 4);
		jp2.add(new JLabel("  x1:"));
		jp2.add(x1);

		JTextField y1 = new JTextField("0", 4);
		jp2.add(new JLabel("  y1:"));
		jp2.add(y1);

		JTextField x2 = new JTextField("0", 4);
		jp2.add(new JLabel("  x2:"));
		jp2.add(x2);

		JTextField y2 = new JTextField("0", 4);
		jp2.add(new JLabel("  y2:"));
		jp2.add(y2);

		JButton applyBtn = new JButton("Add");
		jp2.add(applyBtn);

		JButton updateBtn = new JButton("Update");
		jp2.add(updateBtn);

		JButton filtrButton = new JButton("Open filtrs");
		jp2.add(filtrButton);


		JButton bezierButton = new JButton("Open bezier");
		jp2.add(bezierButton);


		filtrButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WindowFiltr wf = new WindowFiltr();
				wf.setVisible(true);
				setVisible(false);
			}
		});


		bezierButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BezierArea ba = new BezierArea();
				ba.setVisible(true);
				setVisible(false);
			}
		});



		add(jp2, BorderLayout.NORTH);


		JPanel jp = new JPanel();
		jp.setBackground(Color.LIGHT_GRAY);
		jp.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.GRAY));

		add(jp, BorderLayout.SOUTH);

		Window w1 = new Window();
		jp.add(w2);


		w1.injectShapeList( shapesList );
		w1.injectCords( x1, y1, x2, y2 );
		w1.injectAddNewButton( applyBtn );
		w1.injectUpdateCordsButton( updateBtn );
		w1.injectSaveButton(saveBtn);
		w1.injectLoadButton(loadBtn);

		add(w1, BorderLayout.CENTER);

		setSize(1920, 1080);
		setVisible(true);
	}

	public static Window2 getWindow2(){
		return w2;
	}

}
