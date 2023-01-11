package SPR_GRAFIKA1;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Window2 extends JPanel {


    private int STATE = -1;
    private final int STATE_BUSY = 1;
    private final int STATE_FREE = 2;

    ChangeListener RGBSliderListener, CMYKSliderListener;
    DocumentListener RGBTextListener, CMYKTextListener;

    public Color color = Color.black;
    BufferedImage img;
    JSlider red_slider, green_slider, blue_slider, cyan_slider, magenta_slider, yellow_slider, black_slider;
    JTextField red_text, green_text, blue_text, cyan_text, magenta_text, yellow_text, black_text;

    public Window2() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("r: "), c);

        c.gridx = 1;
        c.gridy = 0;
        red_slider = new JSlider(0, 255, 0);
        add(red_slider, c);

        c.gridx = 2;
        c.gridy = 0;
        red_text = new JTextField("0", 4);
        add(red_text, c);

        c.gridx = 3;
        c.gridy = 0;
        add(new JLabel("   c: "), c);

        c.gridx = 4;
        c.gridy = 0;
        cyan_slider = new JSlider(0, 100, 0);
        add(cyan_slider, c);

        c.gridx = 5;
        c.gridy = 0;
        cyan_text = new JTextField("0", 4);
        add(cyan_text, c);

        // ---
        //

        c.gridx = 0;
        c.gridy = 1;
        add(new JLabel("g: "), c);

        c.gridx = 1;
        c.gridy = 1;
        green_slider = new JSlider(0, 255, 0);
        add(green_slider, c);

        c.gridx = 2;
        c.gridy = 1;
        green_text = new JTextField("0", 4);
        add(green_text, c);

        c.gridx = 3;
        c.gridy = 1;
        add(new JLabel("   m: "), c);

        c.gridx = 4;
        c.gridy = 1;
        magenta_slider = new JSlider(0, 100, 0);
        add(magenta_slider, c);

        c.gridx = 5;
        c.gridy = 1;
        magenta_text = new JTextField("0", 4);
        add(magenta_text, c);

        // ---
        //

        c.gridx = 0;
        c.gridy = 2;
        add(new JLabel("b: "), c);

        c.gridx = 1;
        c.gridy = 2;
        blue_slider = new JSlider(0, 255, 0);
        add(blue_slider, c);

        c.gridx = 2;
        c.gridy = 2;
        blue_text = new JTextField("0", 4);
        add(blue_text, c);

        c.gridx = 3;
        c.gridy = 2;
        add(new JLabel("   y: "), c);

        c.gridx = 4;
        c.gridy = 2;
        yellow_slider = new JSlider(0, 100, 0);
        add(yellow_slider, c);

        c.gridx = 5;
        c.gridy = 2;
        yellow_text = new JTextField("0", 4);
        add(yellow_text, c);

        // ---
        //

        c.gridx = 3;
        c.gridy = 3;
        add(new JLabel("   k: "), c);

        c.gridx = 4;
        c.gridy = 3;
        black_slider = new JSlider(0, 100, 0);
        add(black_slider, c);

        c.gridx = 5;
        c.gridy = 3;
        black_text = new JTextField("100", 4);
        add(black_text, c);


        img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        JLabel picLabel = new JLabel(new ImageIcon(img));
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 6;
        c.ipady = 60;
        add(picLabel, c);

        initListeners();


    }



    public void initListeners() {

        RGBSliderListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();

                int[] v = RGBtoCMYK(red_slider.getValue(), green_slider.getValue(), blue_slider.getValue());

                if (STATE == STATE_BUSY) {
                    return;
                }

                STATE = STATE_BUSY;

                red_text.setText(String.valueOf(red_slider.getValue()));
                green_text.setText(String.valueOf(green_slider.getValue()));
                blue_text.setText(String.valueOf(blue_slider.getValue()));

                cyan_slider.setValue(v[0]);
                cyan_text.setText(String.valueOf(v[0]));
                magenta_slider.setValue(v[1]);
                magenta_text.setText(String.valueOf(v[1]));
                yellow_slider.setValue(v[2]);
                yellow_text.setText(String.valueOf(v[2]));
                black_slider.setValue(v[3]);
                black_text.setText(String.valueOf(v[3]));


                Color c = new Color(
                        red_slider.getValue(),
                        green_slider.getValue(),
                        blue_slider.getValue()
                );


                    setColor(c);

                int rgb = c.getRGB();
                int width = img.getWidth();
                int height = img.getHeight();

                for (int i = 0; i < width; ++i) {
                    for (int j = 0; j < height; ++j) {
                        img.setRGB(i, j, rgb);
                    }
                }

                STATE = STATE_FREE;

                repaint();
            }
        };

        CMYKSliderListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
//                JSlider source = (JSlider) e.getSource();


                int[] v = CMYKtoRGB(cyan_slider.getValue(), magenta_slider.getValue(), yellow_slider.getValue(), black_slider.getValue());

                if (STATE == STATE_BUSY) {
                    return;
                }

                STATE = STATE_BUSY;

                cyan_text.setText(String.valueOf(cyan_slider.getValue()));
                magenta_text.setText(String.valueOf(magenta_slider.getValue()));
                yellow_text.setText(String.valueOf(yellow_slider.getValue()));
                black_text.setText(String.valueOf(black_slider.getValue()));

                red_slider.setValue(v[0]);
                red_text.setText(String.valueOf(v[0]));
                green_slider.setValue(v[1]);
                green_text.setText(String.valueOf(v[1]));
                blue_slider.setValue(v[2]);
                blue_text.setText(String.valueOf(v[2]));


                Color c = new Color(
                        red_slider.getValue(),
                        green_slider.getValue(),
                        blue_slider.getValue()
                );

                setColor(c);

                int rgb = c.getRGB();
                int width = img.getWidth();
                int height = img.getHeight();

                for (int i = 0; i < width; ++i) {
                    for (int j = 0; j < height; ++j) {
                        img.setRGB(i, j, rgb);
                    }
                }

                STATE = STATE_FREE;

                repaint();
            }
        };

        RGBTextListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    updateFieldState(e);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    updateFieldState(e);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    updateFieldState(e);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            protected void updateFieldState(DocumentEvent e) throws BadLocationException {
                if (STATE == STATE_BUSY) {
                    return;
                }

                STATE = STATE_BUSY;

                Document doc = e.getDocument();
                int len = doc.getLength();
                String text = doc.getText(0, len);
                int value_rgb, red, green, blue;


                try {
                    value_rgb = Integer.valueOf(text);
                } catch (Exception ex) {
                    value_rgb = 0;
                }

                if (value_rgb < 0) {
                    value_rgb = 0;

                } else if (value_rgb > 255) {
                    value_rgb = 255;
                }

                String rP = red_text.getText();
                if(rP.equals("")){red = 0;} else {red = Integer.parseInt(rP);}
                String gP = green_text.getText();
                if(gP.equals("")){green = 0;} else {green = Integer.parseInt(gP);}
                String bP = blue_text.getText();
                if(bP.equals("")){blue = 0;} else {blue = Integer.parseInt(bP);}



                if (red_text == doc.getProperty("source")) {
                    red = value_rgb;
                    red_slider.setValue(value_rgb);

                } else if (green_text == doc.getProperty("source")) {
                    green = value_rgb;
                    green_slider.setValue(value_rgb);

                } else if (blue_text == doc.getProperty("source")) {
                    blue = value_rgb;
                    blue_slider.setValue(value_rgb);
                }

                int[] v = RGBtoCMYK(red, green, blue);


                cyan_slider.setValue(v[0]);
                cyan_text.setText(String.valueOf(v[0]));
                magenta_slider.setValue(v[1]);
                magenta_text.setText(String.valueOf(v[1]));
                yellow_slider.setValue(v[2]);
                yellow_text.setText(String.valueOf(v[2]));
                black_slider.setValue(v[3]);
                black_text.setText(String.valueOf(v[3]));


                Color c = new Color(
                        red_slider.getValue(),
                        green_slider.getValue(),
                        blue_slider.getValue()
                );

                setColor(c);



                int rgb = c.getRGB();
                int width = img.getWidth();
                int height = img.getHeight();

                for (int i = 0; i < width; ++i) {
                    for (int j = 0; j < height; ++j) {
                        img.setRGB(i, j, rgb);
                    }
                }

                STATE = STATE_FREE;

                repaint();
            }
        };

        CMYKTextListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    updateFieldState(e);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    updateFieldState(e);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                try {
                    updateFieldState(e);
                } catch (BadLocationException e1) {
                    e1.printStackTrace();
                }
            }

            protected void updateFieldState(DocumentEvent e) throws BadLocationException {
                if (STATE == STATE_BUSY) {
                    return;
                }

                STATE = STATE_BUSY;

                Document doc = e.getDocument();
                int len = doc.getLength();
                String text = doc.getText(0, len);
                int value_cmyk, cyan, magenta, yellow, black;


                try {
                    value_cmyk = Integer.valueOf(text);
                } catch (Exception ex) {
                    value_cmyk = 0;
                }

                if (value_cmyk < 0) {
                    value_cmyk = 0;
                } else if (value_cmyk > 100) {
                    value_cmyk = 100;
                }


                String cP = cyan_text.getText();
                if(cP.equals("")){cyan = 0;} else {cyan = Integer.parseInt(cP);}
                String mP = magenta_text.getText();
                if(mP.equals("")){magenta = 0;} else {magenta = Integer.parseInt(mP);}
                String yP = yellow_text.getText();
                if(yP.equals("")){yellow = 0;} else {yellow = Integer.parseInt(yP);}
                String bP = black_text.getText();
                if(bP.equals("")){black = 0;} else {black = Integer.parseInt(bP);}


                if (cyan_text == doc.getProperty("source")) {
                    cyan = value_cmyk;
                    cyan_slider.setValue(value_cmyk);

                } else if (magenta_text == doc.getProperty("source")) {
                    magenta = value_cmyk;
                    magenta_slider.setValue(value_cmyk);

                } else if (yellow_text == doc.getProperty("source")) {
                    yellow = value_cmyk;
                    yellow_slider.setValue(value_cmyk);

                } else if (black_text == doc.getProperty("source")) {
                    black = value_cmyk;
                    black_slider.setValue(value_cmyk);

                }


                int[] v = CMYKtoRGB(cyan, magenta, yellow, black);


                red_slider.setValue(v[0]);
                red_text.setText(String.valueOf(v[0]));
                green_slider.setValue(v[1]);
                green_text.setText(String.valueOf(v[1]));
                blue_slider.setValue(v[2]);
                blue_text.setText(String.valueOf(v[2]));

                Color c = new Color(
                        red_slider.getValue(),
                        green_slider.getValue(),
                        blue_slider.getValue()
                );

                setColor(c);

                int rgb = c.getRGB();
                int width = img.getWidth();
                int height = img.getHeight();

                for (int i = 0; i < width; ++i) {
                    for (int j = 0; j < height; ++j) {
                        img.setRGB(i, j, rgb);
                    }
                }

                STATE = STATE_FREE;

                repaint();
            }
        };


        red_slider.addChangeListener(RGBSliderListener);
        green_slider.addChangeListener(RGBSliderListener);
        blue_slider.addChangeListener(RGBSliderListener);
        cyan_slider.addChangeListener(CMYKSliderListener);
        magenta_slider.addChangeListener(CMYKSliderListener);
        yellow_slider.addChangeListener(CMYKSliderListener);
        black_slider.addChangeListener(CMYKSliderListener);

        red_text.getDocument().putProperty("source", red_text);
        green_text.getDocument().putProperty("source", green_text);
        blue_text.getDocument().putProperty("source", blue_text);

        red_text.getDocument().addDocumentListener(RGBTextListener);
        green_text.getDocument().addDocumentListener(RGBTextListener);
        blue_text.getDocument().addDocumentListener(RGBTextListener);

        cyan_text.getDocument().putProperty("source", cyan_text);
        magenta_text.getDocument().putProperty("source", magenta_text);
        yellow_text.getDocument().putProperty("source", yellow_text);
        black_text.getDocument().putProperty("source", black_text);

        cyan_text.getDocument().addDocumentListener(CMYKTextListener);
        magenta_text.getDocument().addDocumentListener(CMYKTextListener);
        yellow_text.getDocument().addDocumentListener(CMYKTextListener);
        black_text.getDocument().addDocumentListener(CMYKTextListener);
    }

    public int[] RGBtoCMYK(int r, int g, int b) {

        double pR = r / 255.0;
        double pG = g / 255.0;
        double pB = b / 255.0;
        double pK = Math.min(1-pR,Math.min(1-pG,1-pB));


        int c = (int) Math.round(((1-pR-pK)/(1-pK)*100));
        int m = (int) Math.round(((1-pG-pK)/(1-pK)*100));
        int y = (int) Math.round(((1-pB-pK)/(1-pK)*100));
        int k = (int) Math.round(pK*100);

        return new int[]{c, m, y,k};
    }



    public int[] CMYKtoRGB(int c, int m, int y, int k) {
        int R,G,B;
        double Pc,Pm,Py,Pk;


        Pc =c/100.0;
        Pm =m/100.0;
        Py =y/100.0;
        Pk =k/100.0;

        R = (int)Math.round((1- Math.min(1.0,(Pc*(1.0-Pk)+Pk)))*255);
        G = (int)Math.round((1- Math.min(1.0,(Pm*(1.0-Pk)+Pk)))*255);
        B = (int)Math.round((1- Math.min(1.0,(Py*(1.0-Pk)+Pk)))*255);

        return new int[]{R, G, B};
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public Color getColor(){
        return color;
    }
}
