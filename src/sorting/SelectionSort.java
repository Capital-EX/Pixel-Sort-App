package sorting;

import java.nio.IntBuffer;

import utils.Coroutine;
import static utils.Pixels.rgb;

public class SelectionSort extends Coroutine<IntBuffer> {
	
	private IntBuffer buffer;
	private int[] array;
	
	public SelectionSort(IntBuffer buf) {
		buffer = buf;
		array = buf.array();
	}

	@Override
	public void run() {
		setRunning(true);
		// For every value
		for (int i = 0; i < array.length - 1 && isRunning(); i++){
			// Assume the current value is the minimum value.
			int min = array[i];
			int mindex = i;
			
			// Search through the remaining elements
			for (int j = i + 1; j < array.length; j++){
				// Change min and the min index to
				// the lowest value we find
				if (rgb(min) > rgb(array[j])){
					min = array[j];
					mindex = j;
				}
			}
			
			// Swap the new found min value with the current value
			// if they are not the same
			if (mindex != i) {
				array[mindex] = array[i];
				array[i] = min;
			}
			try {
				yield(buffer);
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}

}
