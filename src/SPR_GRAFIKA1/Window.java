package SPR_GRAFIKA1;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.function.UnaryOperator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Window extends JPanel implements MouseListener, ActionListener, MouseMotionListener, Serializable {

	private final int AREA_MARGIN = 8;

	private final int ACTION_DRAW = 0;
	private final int ACTION_RESIZE = 1;
	private final int ACTION_MOVE = 2;

	private final int CORNER_TOP_LEFT = 1;
	private final int CORNER_TOP_RIGHT = 2;
	private final int CORNER_BOTTOM_RIGHT = 3;
	private final int CORNER_BOTTOM_LEFT = 4;
	private int CORNER_SELECTED = 0;
	private int CURRENT_ACTION = 0;

	private Object shape_selected = null;

	Graphics2D g2d;
	Color c = Color.black;
	private int oldX, oldY;
	private JComboBox shapesList;
	private JTextField x1, x2, y1, y2;
	private JButton add_new_btn, update_cords, save_button, load_button;

	private Object shape_shadow = null;
	private Object shape_hover = null;
	private Object shape_clicked = null;

	private List<Object> drawedItems = new ArrayList<Object>();
	private List<ColorLinePair> colorsList = new ArrayList<>();

	JFileChooser openFileChooser;
	JFileChooser saveFileChooser = new JFileChooser();
	private ObjectOutputStream outputStream;


	public Window() {
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	Window2 win2 = DrawArea.getWindow2();


	//Obsługa akcji
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (CURRENT_ACTION) {
			case ACTION_DRAW:
				draw(e);
				if (shape_shadow instanceof Line2D) {
					updateShapeCords((Line2D) shape_shadow);
				} else if (shape_shadow instanceof Rectangle) {      // RYSOWANIA
					updateShapeCords((Rectangle) shape_shadow);
				} else if (shape_shadow instanceof Ellipse2D) {
					updateShapeCords((Ellipse2D) shape_shadow);
					repaint();
				}
				break;

			case ACTION_RESIZE:
				if (shape_selected instanceof Line2D) {
					resize((Line2D) shape_selected, e);
					updateShapeCords((Line2D) shape_selected);
				} else if (shape_selected instanceof Rectangle) {            // ROZCIĄGANIE
					resize((Rectangle) shape_selected, e);
					updateShapeCords((Rectangle) shape_selected);
				} else if (shape_selected instanceof Ellipse2D) {
					resize((Ellipse2D) shape_selected, e);
					updateShapeCords((Ellipse2D) shape_selected);
				}
				break;

			case ACTION_MOVE:
				if (shape_selected instanceof Line2D) {
					move((Line2D) shape_selected, e);
					updateShapeCords((Line2D) shape_selected);
				} else if (shape_selected instanceof Ellipse2D) {            // PRZESUWANIA
					move((Ellipse2D) shape_selected, e);
					updateShapeCords((Ellipse2D) shape_selected);
				} else if (shape_selected instanceof Rectangle) {
					move((Rectangle) shape_selected, e);
					updateShapeCords((Rectangle) shape_selected);
				}
				break;
		}

		repaint();
	}

	// metody updatująca wartosci pozycji w polach tekstowych
	public void setCords(int cord_x1, int cord_y1, int cord_x2, int cord_y2) {
		x1.setText(String.valueOf(cord_x1));
		y1.setText(String.valueOf(cord_y1));
		x2.setText(String.valueOf(cord_x2));
		y2.setText(String.valueOf(cord_y2));
	}

	public void updateShapeCords(Line2D l) {
		int x1 = (int) l.getP1().getX();
		int y1 = (int) l.getP1().getY();
		int x2 = (int) l.getP2().getX();
		int y2 = (int) l.getP2().getY();

		setCords(x1, y1, x2, y2);
	}

	public void updateShapeCords(Rectangle r) {
		int x1 = (int) r.getMinX();
		int y1 = (int) r.getMinY();
		int x2 = (int) r.getMaxX();
		int y2 = (int) r.getMaxY();

		setCords(x1, y1, x2, y2);
	}

	public void updateShapeCords(Ellipse2D e) {
		int x1 = (int) e.getMinX();
		int y1 = (int) e.getMinY();
		int x2 = (int) e.getMaxX();
		int y2 = (int) e.getMaxY();

		setCords(x1, y1, x2, y2);
	}


	// metody przesuwająca figury
	public void move(Line2D l, MouseEvent e) {
		Point2D p1 = l.getP1();
		Point2D p2 = l.getP2();

		p1.setLocation((int) p1.getX() - (oldX - e.getX()), (int) p1.getY() - (oldY - e.getY()));

		p2.setLocation((int) p2.getX() - (oldX - e.getX()), (int) p2.getY() - (oldY - e.getY()));

		oldX = e.getX();
		oldY = e.getY();

		l.setLine(p1, p2);
	}

	public void move(Rectangle l, MouseEvent e) {
		int x = (int) l.getMinX() - (oldX - e.getX());
		int y = (int) l.getMinY() - (oldY - e.getY());

		oldX = e.getX();
		oldY = e.getY();

		l.setLocation(x, y);
	}

	public void move(Ellipse2D el, MouseEvent e) {
		int x = (int) el.getMinX() - (oldX - e.getX());
		int y = (int) el.getMinY() - (oldY - e.getY());

		oldX = e.getX();
		oldY = e.getY();

		el.setFrame(new Rectangle(x, y, (int) el.getWidth(), (int) el.getHeight()));
	}


	// metody zmieniające rozmiar figur --rozciaganie
	public void resize(Line2D l, MouseEvent e) {
		int x1 = (int) l.getP1().getX();
		int y1 = (int) l.getP1().getY();
		int x2 = (int) l.getP2().getX();
		int y2 = (int) l.getP2().getY();

		switch (CORNER_SELECTED) {
			case CORNER_TOP_LEFT:
				x1 = e.getX();
				y1 = e.getY();
				break;

			case CORNER_TOP_RIGHT:
				x2 = e.getX();
				y2 = e.getY();
				break;
		}

		l.setLine(new Line2D.Double(x1, y1, x2, y2));
	}

	public void resize(Rectangle r, MouseEvent e) {
		int x1 = 0;
		int y1 = 0;

		switch (CORNER_SELECTED) {
			case CORNER_TOP_LEFT:
				x1 = (int) r.getMaxX();
				y1 = (int) r.getMaxY();

				if (r.getMaxX() <= e.getX()) {
					CORNER_SELECTED = CORNER_TOP_RIGHT;
				} else if (r.getMaxY() <= e.getY()) {
					CORNER_SELECTED = CORNER_BOTTOM_LEFT;
				}
				break;

			case CORNER_TOP_RIGHT:
				x1 = (int) r.getMinX();
				y1 = (int) r.getMaxY();

				if (r.getMinX() > e.getX()) {
					CORNER_SELECTED = CORNER_TOP_LEFT;
				} else if (r.getMaxY() <= e.getY()) {
					CORNER_SELECTED = CORNER_BOTTOM_RIGHT;
				}
				break;

			case CORNER_BOTTOM_RIGHT:
				x1 = (int) r.getMinX();
				y1 = (int) r.getMinY();

				if (r.getMinY() > e.getY()) {
					CORNER_SELECTED = CORNER_TOP_RIGHT;
				} else if (r.getMinX() > e.getX()) {
					CORNER_SELECTED = CORNER_BOTTOM_LEFT;
				}
				break;

			case CORNER_BOTTOM_LEFT:
				x1 = (int) r.getMaxX();
				y1 = (int) r.getMinY();

				if (r.getMinY() > e.getY()) {
					CORNER_SELECTED = CORNER_TOP_LEFT;
				} else if (r.getMaxX() <= e.getX()) {
					CORNER_SELECTED = CORNER_BOTTOM_RIGHT;
				}
				break;
		}

		r.setRect(new Rectangle((int) Math.min(x1, e.getX()), (int) Math.min(y1, e.getY()),
				(int) Math.abs(x1 - e.getX()), (int) Math.abs(y1 - e.getY())));
	}

	public void resize(Ellipse2D el, MouseEvent e) {
		resize((Rectangle) shape_hover, e);
		el.setFrame((Rectangle) shape_hover);
	}


	public void draw(MouseEvent e) {
		int currentX = e.getX();
		int currentY = e.getY();

		int x1 = Math.min(oldX, currentX);
		int y1 = Math.min(oldY, currentY);
		int x2 = Math.abs(oldX - currentX);
		int y2 = Math.abs(oldY - currentY);

		switch (shapesList.getSelectedItem().toString()) {
			case "Line":
				shape_shadow = new Line2D.Double(oldX, oldY, currentX, currentY);
				break;

			case "Rectangle":
				shape_shadow = new Rectangle(x1, y1, x2, y2);
				break;

			case "Oval":
				shape_shadow = new Ellipse2D.Double(x1, y1, x2, y2);
				break;
		}
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		int corner = 0;

		Rectangle checkArea = new Rectangle(e.getX() - AREA_MARGIN, e.getY() - AREA_MARGIN, 2 * AREA_MARGIN,
				2 * AREA_MARGIN);

		for (Object o : drawedItems) {
			if (o instanceof Line2D) {
				Line2D l = (Line2D) o;

				corner = checkCornersHover(l, e.getPoint());

				if (corner != 0) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					shape_selected = l;

					CORNER_SELECTED = corner;
					CURRENT_ACTION = ACTION_RESIZE;

					return;
				}

				if (l.ptLineDist(e.getPoint()) < AREA_MARGIN) {
					shape_selected = l;

					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));

					CURRENT_ACTION = ACTION_MOVE;

					return;
				}
			} else if (o instanceof Rectangle) {
				corner = checkCornersHover((Rectangle) o, e.getPoint());

				if (corner != 0) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					shape_selected = (Rectangle) o;

					CORNER_SELECTED = corner;
					CURRENT_ACTION = ACTION_RESIZE;

					return;
				}

				if (((Rectangle) o).contains(checkArea)) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					shape_selected = (Rectangle) o;

					CURRENT_ACTION = ACTION_MOVE;

					return;
				}
			} else if (o instanceof Ellipse2D) {
				Ellipse2D el = ((Ellipse2D) o);

				Rectangle r = new Rectangle((int) el.getMinX(), (int) el.getMinY(), (int) el.getWidth(),
						(int) el.getHeight());

				if (el.contains(e.getPoint())) {
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					shape_selected = el;
					shape_hover = null;
					CURRENT_ACTION = ACTION_MOVE;

					return;
				}

				if (r.contains(e.getPoint())) {
					corner = checkCornersHover(r, e.getPoint());

					shape_hover = r;

					if (corner != 0) {
						setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
						shape_selected = el;

						CORNER_SELECTED = corner;
						CURRENT_ACTION = ACTION_RESIZE;
						return;
					} else {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}

					CURRENT_ACTION = ACTION_MOVE;

					return;
				} else {
					shape_hover = null;
				}
			}
		}

		shape_selected = null;
		CURRENT_ACTION = ACTION_DRAW;

		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}


	public int checkCornersHover(Rectangle r, Point p) {
		double x1 = r.getMinX();
		double y1 = r.getMinY();
		double x2 = r.getMaxX();
		double y2 = r.getMaxY();

		Rectangle areaToCheck = new Rectangle((int) p.getX() - AREA_MARGIN, (int) p.getY() - AREA_MARGIN,
				2 * AREA_MARGIN, 2 * AREA_MARGIN);

		// corner top left
		if (areaToCheck.contains(x1, y1)) {
			return CORNER_TOP_LEFT;
		}

		// corner top right
		if (areaToCheck.contains(x2, y1)) {
			return CORNER_TOP_RIGHT;
		}

		// corner bottom right
		if (areaToCheck.contains(x2, y2)) {
			return CORNER_BOTTOM_RIGHT;
		}

		// corner bottom left
		if (areaToCheck.contains(x1, y2)) {
			return CORNER_BOTTOM_LEFT;
		}

		return 0;
	}

	public int checkCornersHover(Line2D l, Point p) {
		int x1 = (int) l.getX1();
		int y1 = (int) l.getY1();
		int x2 = (int) l.getX2();
		int y2 = (int) l.getY2();

		Rectangle areaToCheck = new Rectangle((int) p.getX() - AREA_MARGIN, (int) p.getY() - AREA_MARGIN,
				2 * AREA_MARGIN, 2 * AREA_MARGIN);

		if (areaToCheck.contains(new Point(x1, y1))) {
			return CORNER_TOP_LEFT;
		}

		if (areaToCheck.contains(new Point(x2, y2))) {
			return CORNER_TOP_RIGHT;
		}

		return 0;
	}


	// obsługa przycisków i wyboru z listy rozwijanej kształtu
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();

		if (source == add_new_btn) {
			double p_x1 = Double.valueOf(x1.getText());
			double p_y1 = Double.valueOf(y1.getText());
			double p_x2 = Double.valueOf(x2.getText());
			double p_y2 = Double.valueOf(y2.getText());

			switch (shapesList.getSelectedItem().toString()) {
				case "Line":
					drawedItems.add(new Line2D.Double(p_x1, p_y1, p_x2, p_y2));
					if (colorsList.size() + 1 == drawedItems.size()) {

						ColorLinePair colorLinePair = new ColorLinePair(c, drawedItems.get(drawedItems.size() - 1));
						colorsList.add((drawedItems.size() - 1), colorLinePair);

					}
					break;

				case "Rectangle":
					drawedItems.add(new Rectangle((int) p_x1, (int) p_y1, (int) Math.abs(p_x2 - p_x1),
							(int) Math.abs(p_y2 - p_y1)));
					if (colorsList.size() + 1 == drawedItems.size()) {

						ColorLinePair colorLinePair = new ColorLinePair(c, drawedItems.get(drawedItems.size() - 1));
						colorsList.add((drawedItems.size() - 1), colorLinePair);

					}
					break;

				case "Oval":
					drawedItems.add(
							new Ellipse2D.Double(p_x1, p_y1, (int) Math.abs(p_x2 - p_x1), (int) Math.abs(p_y2 - p_y1)));
					if (colorsList.size() + 1 == drawedItems.size()) {

						ColorLinePair colorLinePair = new ColorLinePair(c, drawedItems.get(drawedItems.size() - 1));
						colorsList.add((drawedItems.size() - 1), colorLinePair);

					}
					break;
			}

			repaint();

		} else if (source == update_cords && shape_clicked != null) {
			Point p1 = new Point(Integer.valueOf(x1.getText()), Integer.valueOf(y1.getText()));
			Point p2 = new Point(Integer.valueOf(x2.getText()), Integer.valueOf(y2.getText()));

			int position = 0;

			if (shape_clicked instanceof Line2D) {
				Line2D l = ((Line2D) shape_clicked);
				l.setLine(p1, p2);
				position = drawedItems.indexOf(l);

			} else if (shape_clicked instanceof Rectangle) {
				Rectangle r = (Rectangle) shape_clicked;
				r.setFrameFromDiagonal(p1, p2);
				position = drawedItems.indexOf(r);

			} else if (shape_clicked instanceof Ellipse2D) {
				Ellipse2D l = (Ellipse2D) shape_clicked;

				l.setFrameFromDiagonal(p1, p2);
				position = drawedItems.indexOf(l);

			}
			System.out.println(position);

			repaint();
		} else if (source == load_button) {

			int returnValue = openFileExplorer().showOpenDialog(this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				colorsList = null;
				

				try {

					ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(openFileChooser.getSelectedFile()));
					Map<String, List> mapout= (Map<String, List>) inputStream.readObject();
					drawedItems = mapout.get("a");
					colorsList = mapout.get("b");

					repaint();

				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}

			}

		} else if (source == save_button) {

			saveFileChooser.setCurrentDirectory(new File("C:\\Users\\micha\\Desktop\\SPR_GRAFIKA1_final"));
			saveFileChooser.setFileFilter(new FileNameExtensionFilter("BIN file", "bin"));
			saveFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

			int returnValue = saveFileChooser.showSaveDialog(this);

			if (returnValue == JFileChooser.APPROVE_OPTION) {

				try {

					File f=saveFileChooser.getSelectedFile() ;
					if(!saveFileChooser.getSelectedFile().getName().contains(".")) f= new File(saveFileChooser.getSelectedFile().toString() + ".bin");

					ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(f));

					Map<String, List> map = new HashMap<String, List>();
					map.put("a", drawedItems);
					map.put("b", colorsList);
					outputStream.writeObject(map);




				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (IOException ex) {
					ex.printStackTrace();
				}


			}
		}

	}



	@Override
	public void mouseClicked(MouseEvent e) {

		if (shape_selected != null) {
			shape_clicked = shape_selected;

			if (shape_clicked instanceof Line2D) {
				updateShapeCords((Line2D) shape_clicked);
			} else if (shape_clicked instanceof Rectangle) {
				updateShapeCords((Rectangle) shape_clicked);
			} else if (shape_clicked instanceof Ellipse2D) {
				updateShapeCords((Ellipse2D) shape_clicked);
			}
		} else {
			shape_clicked = null;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		drawedItems.add(shape_shadow);
		oldX = e.getX();
		oldY = e.getY();


		c = win2.getColor();
		if (colorsList.size() + 1 == drawedItems.size()) {
			ColorLinePair colorLinePair = new ColorLinePair(c, drawedItems.get(drawedItems.size() - 1));
			colorsList.add((drawedItems.size() - 1), colorLinePair);
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawedItems.add(shape_shadow);
		oldX = e.getX();
		oldY = e.getY();


		c = win2.getColor();
		if (colorsList.size() + 1 == drawedItems.size()) {
			ColorLinePair colorLinePair = new ColorLinePair(c, drawedItems.get(drawedItems.size() - 1));
			colorsList.add((drawedItems.size() - 1), colorLinePair);
		}

	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g2d = (Graphics2D) g;
		g2d.fillRect(0, 0, WIDTH, HEIGHT);


		g2d.setColor(c);
		reDrawLine(g2d);


		drawShadow(g2d);
		drawHover(g2d);
	}

	private void reDrawLine(Graphics2D graphic) {

		if(!colorsList.isEmpty()){
		for (int i = 0; i < colorsList.size(); i++) {
			drawShape(graphic, colorsList.get(i).getObject());
			graphic.setColor(colorsList.get(i).getColor());
		}
		}

	}


	private void drawShadow(Graphics2D g2d) {
		drawShape(g2d, shape_shadow);
	}

	private void drawHover(Graphics2D g2d) {
		drawShape(g2d, shape_hover);
	}

	private void drawShape(Graphics2D g2d, Object o) {
		if (o instanceof Line2D) {
			g2d.draw((Line2D) o);
		} else if (o instanceof Rectangle) {
			g2d.draw((Rectangle) o);
		} else if (o instanceof Ellipse2D) {
			g2d.draw((Ellipse2D) o);
		}
	}


	// wstrzykniecie logiki do zadeklarowanych przyciksów  w DrawArea
	public void injectShapeList(JComboBox list) {
		shapesList = list;
	}

	public void injectCords(JTextField x1, JTextField y1, JTextField x2, JTextField y2) {
		this.x1 = x1;
		this.y1 = y1;

		this.x2 = x2;
		this.y2 = y2;
	}

	public void injectAddNewButton(JButton btn) {
		add_new_btn = btn;
		add_new_btn.addActionListener(this);
	}

	public void injectUpdateCordsButton(JButton btn) {
		update_cords = btn;
		update_cords.addActionListener(this);
	}

	public void injectSaveButton(JButton btn) {
		save_button = btn;
		save_button.addActionListener(this);
	}

	public void injectLoadButton(JButton btn) {
		load_button = btn;
		load_button.addActionListener(this);
	}

	public JFileChooser openFileExplorer() {
		openFileChooser = new JFileChooser();
		openFileChooser.setCurrentDirectory(new File("C:\\Users\\micha\\Desktop\\SPR_GRAFIKA1_final"));
		openFileChooser.setFileFilter(new FileNameExtensionFilter("BIN file","bin"));
		return openFileChooser;
	}
}
