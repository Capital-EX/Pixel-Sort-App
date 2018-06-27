package sorting;

import java.nio.IntBuffer;

import utils.Coroutine;
import static utils.Pixels.rgb;

// This implementation of MergeSort delay's the creation of 
// any new array until the merge step is performed.
public class MergeSort extends Coroutine<IntBuffer> {
	private IntBuffer buffer;
	private int[] array;
	public MergeSort(IntBuffer buf){
		buffer = buf;
		// Buffers have an internal array changes here
		// are reflected in the buffer
		array = buffer.array();
	}
	
	public void mergeSort (int start, int length) throws InterruptedException{
		if (length > 1 && isRunning()){
			// The length of the first sublist 
			// is 1/2 of the input sublist
			// And starts at the input sublist start location
			int firstLength = length / 2;
			mergeSort(start, firstLength);
			
			// The second sublist starts at the end of the first
			int secondStart = start + firstLength;
			// And has the length of the remaining values
			int secondLength = length - firstLength;
			mergeSort(secondStart, secondLength);
			
			// in order to update the buffer, I need to track where
			// each sublist is located and how long it is.
			merge(start, firstLength, secondStart, secondLength);
			yield(buffer);
		}
		
	}
	
	public void merge(int firstStart, int firstLength, int secondStart, int secondLength) throws InterruptedException {
		// Get the values for the current sublist
		int[] firstSublist = new int[firstLength];
		System.arraycopy(array, firstStart, firstSublist, 0, firstLength);
		int[] secondSublist =  new int[secondLength];
		System.arraycopy(array, secondStart, secondSublist, 0, secondLength);
		
		
		int firstIndex = 0;
		int secondIndex = 0;
		// To avoid creating a new buffer
		// the finalIndex (current3 in the book)
		// must point to the leftmost sublist in the main buffer.
		// This allows the merge function operate on the original buffer.
		int finalIndex = firstStart < secondStart ? firstStart : secondStart;
		
		while (firstIndex < firstLength && secondIndex < secondLength){
			int firstRGB = rgb(firstSublist[firstIndex]);
			int secondRGB = rgb(secondSublist[secondIndex]);
			// the increment operations will happen after the buffer.put is executed.
			if (firstRGB < secondRGB)
				array[finalIndex++] = firstSublist[firstIndex++];
			else 
				array[finalIndex++] = secondSublist[secondIndex++];
		}
		
		// If there are any remaining values merge them in.
		while (firstIndex < firstLength) 
			array[finalIndex++] = firstSublist[firstIndex++];
		
		while (secondIndex < secondLength)
			array[finalIndex++] = secondSublist[secondIndex++];
	}
	
	@Override
	public void run() {
		setRunning(true);
		try {
			mergeSort(0, buffer.capacity() - 1);
		} catch (InterruptedException e) {
			return;
		}
	}

}
