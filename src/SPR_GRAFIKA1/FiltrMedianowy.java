package SPR_GRAFIKA1;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;

public class FiltrMedianowy {
	private int width, height;
	
	int mask_size = 7;
	
	int[][] mask = {
			{1,1,1},
			{1,1,1},
			{1,1,1}
		};
	
	File f;
	BufferedImage orig_img;

	private List<Integer> red   = new ArrayList<Integer>();
	private List<Integer> green = new ArrayList<Integer>();
	private List<Integer> blue  = new ArrayList<Integer>();
	
	private Comparator<Integer> comparator;
	
	public FiltrMedianowy( File f ) {
		this.f = f;
		
		comparator = new Comparator<Integer>() {
	        @Override
	        public int compare(Integer v1, Integer v2) {
	            return  v1.compareTo(v2);
	        }
	    };
	}
	
	public BufferedImage filter() throws IOException {
		orig_img = ImageIO.read(f);
		BufferedImage tmp_img = ImageIO.read(f);
		
		width = orig_img.getWidth();
		height = orig_img.getHeight();
		
		for ( int i = 0; i<height; ++i ) { 
			for ( int j = 0; j<width; ++j ) {
				tmp_img.setRGB(j, i, countColorForPixel(j,i));
			}
		}
						
		return tmp_img;
	}
	
	public int countColorForPixel(int x, int y) {
		int sum = 0;
		int usedColors = 0;
		int r=0, g=0, b=0;
		Color tmp;
		
		int tmp_index = (mask_size-1)/2;
		
		red.clear();
		green.clear();
		blue.clear();
		
		
		for (int i=-tmp_index; i<=tmp_index; ++i ) {
			for (int j=-tmp_index; j<=tmp_index; ++j ) {
				if ( pixelExists(x+i,y+j) == true ) {
					tmp = getPixelColor(x+i,y+j);

					red.add( tmp.getRed() );
					green.add( tmp.getGreen() );
					blue.add( tmp.getBlue() );
					
					++usedColors;
				}							
			}
		}		

		red.sort(comparator);
		green.sort(comparator);
		blue.sort(comparator);

		int len = red.size();
		int index;
		
		if ( len % 2 == 0 ) {
			index = len / 2;			
			r = (red.get(index) + red.get(index+1)) / 2;			
		} else {
			index = len / 2;			
			r = red.get(index);
		}
		
		len = green.size();
		if ( len % 2 == 0 ) {
			index = len / 2;			
			g = (green.get(index) + green.get(index+1)) / 2;			
		} else {
			index = len / 2;			
			g = green.get(index);
		}

		len = blue.size();
		if ( len % 2 == 0 ) {
			index = len / 2;			
			b = (blue.get(index) + red.get(index+1)) / 2;			
		} else {
			index = len / 2;			
			b = blue.get(index);
		}
				
		Color t = new Color(r,g,b);
		
		return t.getRGB();
	}

	
	public Color getPixelColor(int x, int y) {
		if ( pixelExists(x,y) == true ) {
			return new Color(orig_img.getRGB(x, y));
		} else {
			return new Color(0);
		}
	}
	
	public boolean pixelExists(int x, int y) {
		if ( x < 0 || x >= width ) {
			return false;
		}
		
		if ( y < 0 || y >= height ) {
			return false;			
		}
		
		return true;
	}
}
