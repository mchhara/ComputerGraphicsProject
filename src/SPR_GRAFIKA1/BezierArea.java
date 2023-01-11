package SPR_GRAFIKA1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BezierArea extends JFrame {

    public BezierArea() {
        setLayout(new BorderLayout());


        JPanel jp2 = new JPanel();
        jp2.setBackground(Color.LIGHT_GRAY);
        jp2.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));


        JButton paint = new JButton("Paint");
        jp2.add(paint);

        paint.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DrawArea();
                setVisible(false);
            }
        });

        JTextField x = new JTextField("0", 4);
        jp2.add(new JLabel("  x:"));
        jp2.add(x);

        JTextField y = new JTextField("0", 4);
        jp2.add(new JLabel("  y:"));
        jp2.add(y);

        JButton applyBtn = new JButton("Add");
        jp2.add(applyBtn);

        JButton update = new JButton("Update");
        jp2.add(update);

        JButton drawBtn = new JButton("Draw");
        jp2.add(drawBtn);


        add(jp2, BorderLayout.NORTH);

        Bezier w1 = new Bezier();
        add(w1, BorderLayout.CENTER);


        w1.injectCords( x, y);
        w1.injectAddNewButton(applyBtn);
        w1.injectUpdateCordsButton(update);
        w1.injectDrawButton(drawBtn);

        setSize(1920, 1080);
        setVisible(true);
    }
}