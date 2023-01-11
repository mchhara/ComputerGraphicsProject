package SPR_GRAFIKA1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.MouseInputListener;


    public class Bezier extends JPanel implements MouseInputListener,ActionListener {

        private List<Point> list = new ArrayList();
        private List<Point> curvePointsList = new ArrayList();
        private JTextField x, y;
        private JButton add_new_btn, draw,update_cords;
        Graphics2D g2d;
        private final int AREA_MARGIN = 8;
        private final int ACTION_RESIZE = 1;
        private final int CORNER_TOP_LEFT = 1;
        private final int ACTION_DRAW = 0;
        private int CURRENT_ACTION = 0;
        private Object shape_selected = null;
        private Object shape_clicked = null;

        int corner=0;

        public void mouseDragged(MouseEvent e) {
            switch (CURRENT_ACTION) {
                case ACTION_RESIZE:
                    resize((Point) shape_selected, e);
                    updateShapeCords((Point)shape_selected );
                    curvePointsList.clear();
                    CalculateBezier();
                    repaint();
                    break;

            }
            repaint();
        }

        public void updateShapeCords(Point p) {
            int x= (int) p.getX();
            int y = (int) p.getY();


            setCords(x, y);
        }
        public void resize(Point p, MouseEvent e) {
            int x1 = e.getX();
            int y1 = e.getY();

            p.setLocation(x1,y1);
        }

        public void setCords(int cord_x1, int cord_y1) {
            x.setText(String.valueOf(cord_x1));
            y.setText(String.valueOf(cord_y1));

        }
        public int checkCornersHover(Point l, Point p) {
            int x1 = l.x;
            int y1 = l.y;


            Rectangle areaToCheck = new Rectangle((int) p.getX() - AREA_MARGIN, (int) p.getY() - AREA_MARGIN,
                    2 * AREA_MARGIN, 2 * AREA_MARGIN);

            if (areaToCheck.contains(new Point(x1, y1))) {
                return CORNER_TOP_LEFT;
            }
            return 0;
        }


        public Bezier() {

            addMouseListener(this);
            addMouseMotionListener(this);

            setLocation(100, 100);
            setSize(1920, 1080);
            setVisible(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g2d = (Graphics2D) g.create();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, WIDTH, HEIGHT);
            g2d.setColor(Color.black);


            for (int i = 0; i < list.size(); i++) {
                g2d.fillOval(list.get(i).x, list.get(i).y, 5, 5);
            }
            if(!curvePointsList.isEmpty()) {
                Path2D polyline = new Path2D.Double();
                polyline.moveTo(list.get(0).x, list.get(0).y);
                    for (int j = 0; j < curvePointsList.size(); j++) {

                        polyline.lineTo(curvePointsList.get(j).x, curvePointsList.get(j).y);
                    }
                g2d.draw(polyline);
                g2d.dispose();
            }
        }


        @Override
        public void mouseClicked(MouseEvent e) {
            if (shape_selected != null) {
                shape_clicked = shape_selected;

                if (shape_clicked instanceof Point) {
                    updateShapeCords((Point) shape_clicked);
                }
            } else {
                shape_clicked = null;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {
            if (corner == 0) {
                list.add(new Point(e.getX(), e.getY()));
                repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {}

        @Override
        public void mouseExited(MouseEvent e) {}

        @Override
        public void mouseMoved(MouseEvent e) {

             corner = 0;

            for (Object o : list) {
                if (o instanceof Point) {
                    Point l = (Point) o;

                    corner = checkCornersHover(l, e.getPoint());

                    if (corner != 0) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                        shape_selected = l;

                        CURRENT_ACTION = ACTION_RESIZE;

                        return;
                    }
                }
            }

            shape_selected = null;
            CURRENT_ACTION = ACTION_DRAW;
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }

        public void injectCords(JTextField x, JTextField y) {
            this.x = x;
            this.y = y;
        }

        public void injectAddNewButton(JButton btn) {
            add_new_btn = btn;
            add_new_btn.addActionListener(this);
        }
        public void injectDrawButton(JButton btn) {
            draw = btn;
            draw.addActionListener(this);
        }
        public void injectUpdateCordsButton(JButton btn) {
            update_cords = btn;
            update_cords.addActionListener(this);
        }


        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == add_new_btn) {
                int p_x = Integer.valueOf(x.getText());
                int p_y = Integer.valueOf(y.getText());

                list.add(new Point(p_x,p_y));
                repaint();
            }
            else if(e.getSource() == draw){

                if(curvePointsList.isEmpty()) {
                    CalculateBezier();
                    repaint();
                }
                else{
                    curvePointsList.clear();
                    CalculateBezier();
                    repaint();
                }

            }
            else if(e.getSource() == update_cords && shape_clicked != null) {
                Point p1 = new Point(Integer.valueOf(x.getText()), Integer.valueOf(y.getText()));

                if (shape_clicked instanceof Point) {
                    Point p = ((Point) shape_clicked);
                    p.setLocation(p1);
                    curvePointsList.clear();
                    CalculateBezier();
                    repaint();
                }

            }
        }


        private double Newton(double n, double i)
        {
            double result = 1;
            double iterator;

            for (iterator = 1; iterator <= i; iterator++)
            {
                result = result * (n - iterator + 1) / iterator;
            }

            return result;
        }

        private double Bernstein(double n, double i, double t)
        {
            return Newton(n, i) * Math.pow(t, i) * Math.pow(1.0 - t, n - i);
        }

        private void CalculateBezier()
        {
            int n = list.size() - 1;
            for (double t = 0.01; t <= 1; t = t + 0.005)
            {
                int i = 0;
                int x = 0;
                int y = 0;

                for(Point p : list)
                {

                    x = (int) (x + Bernstein(n, i, t) * p.x);
                    y = (int) (y + Bernstein(n, i, t) * p.y);
                    i = i + 1;
                }

                curvePointsList.add(new Point(x, y));
            }
        }
    }