package bigimp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TouchEvent;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class SwtExample {

	private Display display;
	private Shell shell;
	private Group group;
	private Image image;
	private Canvas canvas;
	private Rectangle rect = new Rectangle(0, 0, 200, 200);
	private boolean mouseDown = false;
	private MouseHandler mouseHandler;
	private Button button;
	private Composite buttonBar;
	private Composite hbox;
    private Table trainingComposite;
    double s;

	TrainingImages trainingImages = new TrainingImages();
	String currentImageFileName = "data/images/1277383675Image000009.jpg";

	private class PaintHandler implements PaintListener {
		public void paintControl(PaintEvent e) {
			Rectangle b = image.getBounds();
			Rectangle cb = canvas.getBounds();
			double sx = ((double) cb.width) / b.width;
			double sy = ((double) cb.height) / b.height;
			s = (sx > sy) ? sy : sx;
			e.gc.drawImage(image, 0, 0, b.width, b.height, 0, 0,
					(int) (b.width * s), (int) (b.height * s));
			e.gc.drawRectangle(rect);
		}
	}

	private class MouseHandler implements MouseListener, MouseMoveListener {

		@Override
		public void mouseDoubleClick(MouseEvent arg0) {
		}

		@Override
		public void mouseDown(MouseEvent arg) {
			rect.x = arg.x;
			rect.y = arg.y;
			rect.width = 0;
			rect.height = 0;
			mouseDown = true;
			canvas.redraw();
		}

		@Override
		public void mouseUp(MouseEvent arg) {
			rect.width = arg.x - rect.x;
			rect.height = arg.y - rect.y;
			mouseDown = false;
			canvas.redraw();
		}

		@Override
		public void mouseMove(MouseEvent arg) {
			if (mouseDown) {
				rect.width = arg.x - rect.x;
				rect.height = arg.y - rect.y;
				canvas.redraw();
			}
		}

	}

	private class OnImageSaveAction implements SelectionListener, TouchListener {
		
		void saveTrainingImage() {
			trainingImages.add(new TrainingImages.TrainingImage(currentImageFileName, rect, true));
			TableItem item = new TableItem(trainingComposite, SWT.BORDER);
			Image itemImage = new Image(display, 20, 20);
			GC gc = new GC(itemImage);
			System.out.format("%f %f %f %f\n", rect.x / s, rect.y / s, rect.width / s, rect.height / s);
			gc.drawImage(image, (int)(rect.x / s), (int)(rect.y / s), (int)(rect.width / s), (int)(rect.height / s), 0, 0, 20, 20);
			gc.dispose();
			item.setImage(itemImage);
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {
			saveTrainingImage();
		}

		@Override
		public void widgetSelected(SelectionEvent arg0) {
			saveTrainingImage();
		}

		@Override
		public void touch(TouchEvent arg0) {
			saveTrainingImage();
		}

	}

	public static void main(String[] args) throws FileNotFoundException {

		SwtExample example = new SwtExample();
		example.init();
		example.run();
		example.dispose();
	}

	public void run() {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	private void dispose() {
		image.dispose();
		display.dispose();
	}

	private void init() {
		GridLayout shellLayout = new GridLayout();
		shellLayout.numColumns = 1;

		RowLayout buttonBarLayout = new RowLayout();
		buttonBarLayout.type = SWT.HORIZONTAL;
		buttonBarLayout.fill = true;
		buttonBarLayout.pack = true;

		display = new Display();

		shell = new Shell(display);
		shell.setLayout(shellLayout);

		buttonBar = new Composite(shell, 0);
		buttonBar.setLayout(buttonBarLayout);
		buttonBar.setLayoutData(newHorizGridData());

		button = new Button(buttonBar, SWT.PUSH);
		button.setText("Save Image");
		button.addSelectionListener(new OnImageSaveAction());
		button.addTouchListener(new OnImageSaveAction());

		hbox = new Composite(shell, SWT.BORDER);
		GridLayout hboxLayout = new GridLayout();
		hboxLayout.numColumns = 2;
		hbox.setLayout(hboxLayout);
		hbox.setLayoutData(newGridData(400));
		
		trainingComposite = new Table (hbox, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		trainingComposite.setLayoutData(newGridData(200));
		
		loadImage("data/images/1277383675Image000009.jpg");
		group = new Group(hbox, SWT.NONE);
		group.setLayout(new FillLayout());
		group.setText("a square");
		group.setLayoutData(newGridData(200));

		canvas = new Canvas(group, SWT.NONE);
		canvas.addPaintListener(new PaintHandler());
		mouseHandler = new MouseHandler();
		canvas.addMouseListener(mouseHandler);
		canvas.addMouseMoveListener(mouseHandler);

		shell.pack();
		shell.open();
	}

	private GridData newGridData(int width) {
		GridData groupGridData = new GridData();
		groupGridData.horizontalAlignment = GridData.FILL;
		groupGridData.verticalAlignment = GridData.FILL;
		groupGridData.grabExcessHorizontalSpace = true;
		groupGridData.grabExcessVerticalSpace = true;
		groupGridData.widthHint = width;
		return groupGridData;
	}

	private GridData newHorizGridData() {
		GridData groupGridData = new GridData();
		groupGridData.horizontalAlignment = GridData.FILL;
		groupGridData.verticalAlignment = GridData.FILL;
		groupGridData.grabExcessHorizontalSpace = true;
		groupGridData.grabExcessVerticalSpace = false;
		return groupGridData;
	}

	private void loadImage(String filename) {
		currentImageFileName = filename;
		try {
			InputStream imageStream = new FileInputStream(new File(currentImageFileName));
			image = new Image(display, imageStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
