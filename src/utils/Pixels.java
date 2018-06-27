package utils;

public class Pixels {
	// helper function
	// since java doesn't have unsigned ints
	// the argb representation of a color
	// can be negative messing up the sorting
	// `argb & 0x00_FF_FF_FF` truncates the number to 24 bit 
	// RGB which will always be positive.
	public static int rgb(int argb){
		return argb & 0x00_FF_FF_FF;
	}
}
