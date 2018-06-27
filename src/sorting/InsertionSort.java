package sorting;

import java.nio.IntBuffer;

import utils.Coroutine;
import static utils.Pixels.rgb;

public class InsertionSort extends Coroutine<IntBuffer> {
	private IntBuffer buffer;
	private int[] array;
	
	public InsertionSort(IntBuffer buf) {
		buffer = buf;
		array = buf.array();
	}

	@Override
	public void run() {
		setRunning(true);
		for (int i = 1; i < array.length && isRunning(); i++) {
			// pick the current value
			int currentElement = array[i];
			int k;
			// shift every value befor the currentElement to the right
			// if that value is greater than current element
			for (k = i - 1; k >= 0 && rgb(array[k]) > rgb(currentElement); k--){
				array[k + 1] = array[k];
			}
			// insert current Element into the position k + 1;
			// this removes the duplicated value at k + 1;
			array[k + 1] = currentElement;
			try {
				yield(buffer);
			} catch (InterruptedException e) {
				return;
			}
		}
		
	}

}
