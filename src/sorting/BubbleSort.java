package sorting;

import java.nio.IntBuffer;
import utils.Coroutine;

import static utils.Pixels.rgb;

public class BubbleSort extends Coroutine<IntBuffer>  {
	private IntBuffer buffer;
	private int[] array;
	public void buffer(IntBuffer buf) {
		buffer = buf;
	}
	
	public BubbleSort(IntBuffer buf) {
		buffer = buf;
		array = buf.array();
	}
	
	@Override
	public void run() {
		boolean sorted = false;
		setRunning(true);
		// Look at to neighboring values and swap the left value with the right value
		// if the right value is less than the left.
		for (int i = 1; i < array.length && !sorted && isRunning(); i++) {
			// Assume the list is sorted to start
			sorted = true;
			for (int j = 0; j < array.length - i; j++) { 
				if (rgb(array[j + 1]) < rgb(array[j])) {
					int tmp = array[j + 1];
					array[j + 1] = array[j];
					array[j] = tmp;
					sorted = false;
				}
			}
			try {
				// Pause execution
				yield(buffer);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}
