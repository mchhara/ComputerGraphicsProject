package SPR_GRAFIKA1;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import javax.imageio.ImageIO;

public class FiltrWygladzajacy {
	private int width, height;
	
	int mask_size = 7;
	
	int[][] mask = {
			{1,1,1},
			{1,1,1},
			{1,1,1}
		};
	
	File f;
	BufferedImage orig_img;
	
	public FiltrWygladzajacy( File f ) {
		this.f = f;
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
		
		for (int i=-tmp_index; i<=tmp_index; ++i ) {
			for (int j=-tmp_index; j<=tmp_index; ++j ) {
				if ( pixelExists(x+i,y+j) == true ) {
					tmp = getPixelColor(x+i,y+j);

					r += tmp.getRed();
					g += tmp.getGreen();
					b += tmp.getBlue();
					
					++usedColors;
				}				
			
			}
		}		

		Color t = new Color(r/usedColors, g/usedColors, b/usedColors);
		
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
