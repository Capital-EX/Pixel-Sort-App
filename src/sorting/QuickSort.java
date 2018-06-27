package sorting;

import java.nio.IntBuffer;

import utils.Coroutine;
import static utils.Pixels.rgb;

public class QuickSort extends Coroutine<IntBuffer> {
	private IntBuffer buffer;
	private int[] array;
	public QuickSort (IntBuffer buf) {
		buffer = buf;
		array = buffer.array();
	}
	
	
	// This quicksort takes only two ints lo (start or first value)
	// and hi (last or end value).
	// quickSort yields after a call to partition
	private void quickSort(int lo, int hi) throws InterruptedException {
		if (lo < hi && isRunning()) {
			int p = partition(lo, hi);
			// Yield the current state of the buffer
			yield(buffer);
			quickSort(lo, p - 1);
			quickSort(p + 1, hi);
		}
		
	}
	
	// This quicksort uses the last value to define the pivot point.
	// All values less than the pivot point are moved to the left
	// of where it will be inserted
	private int partition(int lo, int hi) {
		int pivot = array[hi];
		int i = lo - 1;
		// for i from lo to hi
		for (int j = lo; j < hi; j++) {
			// If the a value is less then the pivot point
			if (rgb(array[j]) <= rgb(pivot)) {
				// increment the insertion point
				i += 1;
				// swap list[i] with list[j]
				// since i and j don't increment at the same rate
				// this has the side effect of moving all values 
				// less than or equal to the pivot to the left.
				int tmp = array[i];
				array[i] = array[j];
				array[j] =  tmp;
			}
		}
		// swap the pivot value with the value at the
		// insertion point + 1
		array[hi] = array[i+1];
		array[i+1] = pivot;
		return i + 1;
	}

	@Override
	public void run() {
		setRunning(true);
		try {
			quickSort(0, buffer.capacity() - 1);
		} catch (InterruptedException e) {
			setRunning(false);
		}
		System.out.println("Stopping");
		setRunning(false);
	}

}
