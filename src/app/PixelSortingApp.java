package app;

import java.nio.IntBuffer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import sorting.BubbleSort;
import sorting.InsertionSort;
import sorting.MergeSort;
import sorting.QuickSort;
import sorting.SelectionSort;
import utils.Coroutine;

public class PixelSortingApp extends Application {
	private final int scale = 1;
	private Image image;
	private int height;
	private int width;
	
	private WritableImage data;
	private PixelReader reader;
	private PixelWriter writer;
	private Canvas canvas;
	private GraphicsContext graphics;
	private Coroutine<IntBuffer> coroutine;
	
	@Override
	public void start(Stage mainStage) throws Exception {
		image = new Image("file:resources/midflower.jpg");
		width = (int) image.getWidth();
		height = (int) image.getHeight();
		data = new WritableImage(
				image.getPixelReader()
				, width
				, height
				);
		
		reader = data.getPixelReader();
		writer = data.getPixelWriter();
		
		canvas = new Canvas(width * scale, height * scale);
		graphics = canvas.getGraphicsContext2D();
		graphics.drawImage(data, 0, 0, width * scale, height * scale);
		
		// Each button checks if a coroutine exist and is running
		// if it exist and is running then it is killed.
		// They then will create and run a new one and pass it
		// To a thread running the main loop.
		Button runQuickSort = 
				new Button("Quick Sort");
		runQuickSort.setOnAction(e -> {
			if (coroutine != null && coroutine.isRunning())
				coroutine.kill();
			reset();
			coroutine = new QuickSort(getBuffer(reader));
			coroutine.start();
			(new Thread(() -> mainLoop(coroutine))).start();
			
		});
		
		Button runBubbleSort = 
				new Button("Bubble Sort");
		runBubbleSort.setOnAction( e -> {
			if (coroutine != null && coroutine.isRunning())
				coroutine.kill();
			reset();
			coroutine = new BubbleSort(getBuffer(reader));
			coroutine.start();
			(new Thread(() -> mainLoop(coroutine))).start();
		});
		
		Button runInsertionSort = 
				new Button("Insertion Sort");
		runInsertionSort.setOnAction(e -> {
			if (coroutine != null && coroutine.isRunning())
				coroutine.kill();
			reset();
			coroutine = new InsertionSort(getBuffer(reader));
			coroutine.start();
			(new Thread(() -> mainLoop(coroutine))).start();
		});
		
		Button runSelectionSort = 
				new Button("Selection Sort");
		runSelectionSort.setOnAction(e -> {
			if (coroutine != null && coroutine.isRunning())
				coroutine.kill();
			reset();
			coroutine = new SelectionSort(getBuffer(reader));
			coroutine.start();
			(new Thread(() -> mainLoop(coroutine))).start();
		});
		
		Button runMergeSort = 
				new Button("Merge Sort");
		runMergeSort.setOnAction(e -> {
			if (coroutine != null && coroutine.isRunning())
				coroutine.kill();
			reset();
			coroutine = new MergeSort(getBuffer(reader));
			coroutine.start();
			(new Thread(() -> mainLoop(coroutine))).start();
		});
		
		
		
		VBox selector = 
				new VBox(5, runQuickSort, runBubbleSort, runInsertionSort, runSelectionSort, runMergeSort);
		selector.setSpacing(5);
		selector.setPadding(new Insets(5));
		selector.setAlignment(Pos.CENTER);
		selector.getChildren().forEach(n -> {
			if (n instanceof Button){ 
				((Button) n).setPrefWidth(100);
			}
		});
		HBox content = 
				new HBox(5, selector, canvas);
		content.setPadding(new Insets(5));
		
		Scene scene =
				new Scene(content);
		
		mainStage.setScene(scene);
		mainStage.setTitle("Pixel Sorting");
		mainStage.setOnCloseRequest(e->{
			if (coroutine != null && coroutine.isRunning())
				coroutine.kill();
		});
		mainStage.getIcons().add(new Image("file:resources/iconflower.jpg"));
		mainStage.show();		
	}

	public IntBuffer getBuffer(PixelReader reader) {
		IntBuffer buf = IntBuffer.allocate(width * height);
		reader.getPixels(0, 0, width, height, PixelFormat.getIntArgbInstance(), buf, width);
		return buf;
	}
	
	public void reset() {
		data = new WritableImage(
				image.getPixelReader()
				, width
				, height
				);
		reader = data.getPixelReader();
		writer = data.getPixelWriter();
	}
	
	// Update get the value in the yield coroutine
	// then unyields the coroutine.
	public void update (Coroutine<IntBuffer> c) {
		writer.setPixels(0, 0, width, height,  PixelFormat.getIntArgbInstance(), c.getValue(), width);
		graphics.clearRect(0, 0, width * scale, height * scale);
		graphics.drawImage(data, 0, 0, width * scale, height * scale);
		c.unyield();
	}
	
	// This is the main loop
	// It runs as long as the Coroutine it is
	// given is running.
	public void mainLoop (Coroutine<IntBuffer> c) {
		while(c.isRunning()) {
			try {
				// This is the fastest javafx can run before the 
				// gui locks up.
				Thread.sleep(1l);
				// Platform.runLater(Runnable r) prevents 'mysterious errors from afar'
				// It insures that update() runs with out effecting the main thread of
				// javafx.
				if (c.isSuppended())
					Platform.runLater(() -> update(c));
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
	
}
