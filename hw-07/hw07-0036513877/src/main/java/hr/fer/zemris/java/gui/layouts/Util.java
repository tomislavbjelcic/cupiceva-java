package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

public class Util {
	
	public static void distributeEvenly(int[] arr, int total) {
		if (total <= 0)
			return;
		int len = Objects.requireNonNull(arr, "Predano polje je null.").length;
		if (len < 1)
			return;
		if (len == 1) {
			arr[0] += total;
			return;
		}
		int min = total / len;
		int remaining = total % len;
		
		int remMin = 0;
		int remMax = len-1;
		int d1 = remaining - remMin;
		int d2 = remMax - remaining;
		boolean addMode = d1 <= d2;
		remaining = addMode ? remaining : len-remaining;
		
		for (int i=0; i<len; i++) {
			arr[i] = min + (addMode ? 0 : 1);
		}
		
		
		int gaps = remaining + 1;
		double dist = ((double)(len - 1)) / gaps;
		for (int i=0; i<remaining; i++) {
			int idx = (int) Math.round((i+1) * dist);
			arr[idx] += addMode ? 1 : -1;
		}
	}
	
}
