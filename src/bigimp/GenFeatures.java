package bigimp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.common.collect.Lists;


public class GenFeatures {

	public static void main(String[] args) throws IOException {
		
		File posImageDir = new File("data/images/positive");
		File negImageDir = new File("data/images/negative");
		GenFeatures genFeatures = new GenFeatures();
		int instanceIndex = 0;
		

		// first positive images
		if ( ! posImageDir.exists() ) {
			throw new RuntimeException("Missing " + posImageDir.getPath());
		}
		for(File imageFile: posImageDir.listFiles()) {
			System.out.println("" + ++instanceIndex);
			genFeatures(genFeatures, imageFile);
			System.out.println(",1");
		}
		
		// next negative images
		if ( ! negImageDir.exists() ) {
			throw new RuntimeException("Missing " + negImageDir.getPath());
		}
		for(File imageFile: negImageDir.listFiles()) {
			System.out.println("" + ++instanceIndex);
			genFeatures(genFeatures, imageFile);
			System.out.println(",0");
		}
	}

	private static void genFeatures(GenFeatures genFeatures, File imageFile) {
		Random random = new Random(1);
		List<Double> featureVector = genFeatures.getFeatures(imageFile, random, 100);
		for(Double feature: featureVector) {
			System.out.print(feature + ",");
		}
	}

	private ArrayList<Double> getFeatures(File imageFile, Random random, int numFeatures) {
		try {
			BufferedImage image;
			image = ImageIO.read(imageFile);
			int width = image.getWidth();
			int height = image.getHeight();
			int[] pixelData = new int[width*height];
			image.getRGB(0, 0, width, height, pixelData, 0, width);
			
			ArrayList<Double> featureVector = Lists.newArrayList();
			for(int i=0; i<numFeatures; ++i) {
				featureVector.add(newFeature(pixelData, random, i));
			}
			return featureVector;
		} catch (IOException e) {
			throw new RuntimeException("Unable to read " + imageFile, e);
		}
	}

	private Double newFeature(int[] pixelData, Random random, int i) {
		int x = random.nextInt(pixelData.length);
		int y = random.nextInt(pixelData.length);
		int rx = (pixelData[x] >> 24) & 0xff;
		int ry = (pixelData[y] >> 24) & 0xff;
		int gx = (pixelData[x] >> 16) & 0xff;
		int gy = (pixelData[y] >> 16) & 0xff;
		int bx = (pixelData[x] >> 8) & 0xff;
		int by = (pixelData[y] >> 8) & 0xff;
		return Math.sqrt(square(rx-ry) + square(gx-gy) + square(bx-by));
	}

	private double square(double i) {
		return i*i;
	}
}
