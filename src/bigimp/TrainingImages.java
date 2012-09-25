package bigimp;

import java.io.File;
import java.util.List;

import org.eclipse.swt.graphics.Rectangle;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class TrainingImages {

	static class TrainingImage {
		String sourceFileName;
		Rectangle rect;
		boolean positive;
		
		public TrainingImage() {};
		
		public TrainingImage(String sourceFileName, Rectangle rect, boolean positive) {
			this.sourceFileName = sourceFileName;
			this.rect = new Rectangle(rect.x, rect.y, rect.width, rect.height);
			this.positive = positive;
		}
	}

	List<TrainingImage> trainingImages = Lists.newArrayList();
	List<String> images = Lists.newArrayList();

	public void add(TrainingImage trainingImage) {
		trainingImages.add(trainingImage);		
	}

	public static TrainingImages load(File file) {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			return gson.fromJson(Files.newReader(file, Charsets.UTF_8), TrainingImages.class);
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void save(File file) {
		try {
			Gson gson = new GsonBuilder().disableHtmlEscaping().create();
			gson.toJson(this, Files.newWriter(file, Charsets.UTF_8));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
