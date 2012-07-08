package bigimp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


public class GenFeatures {

	
	public static void main(String[] args) throws IOException {
		File imageFile = new File("/home/richcole/ImageProcessing/StreetSigns/1277383675Image000009.jpg");
		BufferedImage image = ImageIO.read(imageFile);
		int width = image.getWidth();
		int height = image.getHeight();
		int depth = 3;
		int[] pixelData = new int[width*height*depth];
		image.getRGB(0, 0, width, height, pixelData, 0, width);
		
		System.out.println("Done " + pixelData[pixelData.length-1] + " " + (pixelData[width * height - 1] & 0xff));
	}
}
