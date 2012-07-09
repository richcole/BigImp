package bigimp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

public class SwtExample {

	public static void main(String[] args) throws FileNotFoundException {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout (new FillLayout ());
		shell.setBounds(new Rectangle(0, 0, 400, 400));
		
		InputStream imageStream = new FileInputStream(new File("data/images/1277383675Image000009.jpg"));
		final Image image = new Image(display, imageStream);
		Group group = new Group (shell, SWT.NONE);
		group.setLayout (new FillLayout ());
		group.setText ("a square");
		Canvas canvas = new Canvas (group, SWT.NONE);
		canvas.addPaintListener (new PaintListener () {
			public void paintControl (PaintEvent e) {
				Rectangle b = image.getBounds();
				double s = 1.0;
				if ( b.width > b.height ) {
					s = 200.0 / b.width; 
				}
				else {
					s = 200.0 / b.height;
				}
				e.gc.drawImage(image, 0, 0, b.width, b.height, 0, 0, (int) (b.width * s), (int) (b.height * s));
			}
		});
		shell.pack();
		shell.open();
		
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		image.dispose();
		display.dispose();
	}

}
