package SPR_GRAFIKA1;


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import java.nio.Buffer;
import java.sql.SQLOutput;
import javax.imageio.ImageIO;
import static java.awt.Color.white;

public class FiltrDylatacja {
	private int width, height;
	File f;
	BufferedImage orig_img;
	private final Color white;

	private int tand = 128;
	
	int mask_size = 5;

	int[][] mask = {
			{0,0,1,0,0},
			{0,1,1,1,0},
			{1,1,1,1,1},
			{0,1,1,1,0},
			{0,0,1,0,0}
		};
	
	public FiltrDylatacja( File f ) {
		this.f = f;
		this.white = new Color(255,255,255);
	}
	
	public BufferedImage filter() throws IOException {
		orig_img = ImageIO.read(f);
		width = orig_img.getWidth();
		height = orig_img.getHeight();
		BufferedImage tmp_img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		width = orig_img.getWidth();
		height = orig_img.getHeight();
		Color c;
		int colorSum;

		for ( int i = 0; i<width; i++ ) {
			for ( int j = 0; j<height; j++ ) {
				c = new Color(orig_img.getRGB(i,j));
				colorSum = c.getRed() + c.getGreen() + c.getBlue();
				System.out.println(i+" "+j);
				if ( colorSum / 3 > tand ) {
					tmp_img.setRGB(i, j, white.getRGB());
				}
			}
		}
						
		return dilatate(tmp_img);
	}

	private BufferedImage dilatate(BufferedImage bi) {
		BufferedImage to_return = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		for (int w=0; w<width; ++w) {
			for (int h=0; h<height; ++h) {
				if ( pixelInMaskRange(w,h,bi) == true ) {
					to_return.setRGB(w,h, 0);
				} else {
					to_return.setRGB(w,h, white.getRGB());
				}
			}
		}

		return to_return;
	}

	private boolean pixelInMaskRange(int x, int y, BufferedImage bi) {
		int maskOffset = ((mask_size-1) / 2) * -1;
		Color c;
		for (int i=0; i<mask_size; ++i) {
			for (int j=0; j<mask_size; ++j) {
				if ( mask[i][j] == 1 && pixelExists(x+i+maskOffset, y+j+maskOffset)) {
					c = getPixelColor(x+i+maskOffset, y+j+maskOffset, bi);

					if ( c.getRGB() != white.getRGB() ) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public Color getPixelColor(int x, int y, BufferedImage bi) {
		if ( pixelExists(x,y) == true ) {
			return new Color(bi.getRGB(x, y));
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
