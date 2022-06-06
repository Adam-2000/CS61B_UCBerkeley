/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;

public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxWidth = 0;
        for (String string : asciis) {
            maxWidth = Math.max(maxWidth, string.length());
        }
        String[] sorted = asciis.clone();
//        for (int i = maxWidth - 1; i >= 0; i--) {
//            sortHelperLSD(sorted, i);
//        }
        sortHelperMSD(sorted, 0, asciis.length, 0);
        return sorted;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // find max
        char max = Character.MIN_VALUE;
        char min = Character.MAX_VALUE;
        for (String string : asciis) {
            if (index >= string.length()) {
                continue;
            }
            char curr = string.charAt(index);
            max = max > curr ? max : curr;
            min = min < curr ? min : curr;
        }
        min--;
        if (max <= min) {
            return;
        }
        int countsLength = max - min + 1;
        // gather all the counts for each value
        int[] counts = new int[countsLength];
        for (String string : asciis) {
            if (index >= string.length()) {
                counts[0]++;
            } else {
                counts[string.charAt(index) - min]++;
            }
        }

        int[] starts = new int[countsLength];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (String string : asciis) {
            if (index >= string.length()) {
                sorted[starts[0]++] = string;
            } else {
                sorted[starts[string.charAt(index) - min]++] = string;
            }
        }

        System.arraycopy(sorted, 0, asciis, 0, asciis.length);
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        if (end - start <= 1) {
            return;
        }
        // find max
        char max = Character.MIN_VALUE;
        char min = Character.MAX_VALUE;
        for (int i = start; i < end; i++) {
            String string = asciis[i];
            if (index >= string.length()) {
                continue;
            }
            char curr = string.charAt(index);
            max = max > curr ? max : curr;
            min = min < curr ? min : curr;
        }
        min--;
        if (max <= min) {
            return;
        }
        int countsLength = max - min + 1;
        // gather all the counts for each value
        int[] counts = new int[countsLength];
        for (int i = start; i < end; i++) {
            String string = asciis[i];
            if (index >= string.length()) {
                counts[0]++;
            } else {
                counts[string.charAt(index) - min]++;
            }
        }
        int[] starts = new int[countsLength];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = start; i < end; i++) {
            String string = asciis[i];
            if (index >= string.length()) {
                sorted[starts[0]++] = string;
            } else {
                sorted[starts[string.charAt(index) - min]++] = string;
            }
        }

        System.arraycopy(sorted, 0, asciis, start, end - start);

        for (int i = 0; i < starts.length - 1; i++) {
            sortHelperMSD(asciis, start + starts[i], start + starts[i + 1], index + 1);
        }
    }

    @Test
    public void main() {
        String[] input = new String[]{"È#3ò", "p\u007F!\u0090>á\u001Ax"};
//        String[] input = new String[]{"318", "12", "", "91", "56", "7812",
//                                      "0941", "1230", "123", "847", "238"};
        String[] expected = input.clone();
        Arrays.sort(expected);
        expected = new String[]{"p\u007F!\u0090>á\u001Ax", "È#3ò"};
        assertArrayEquals(expected, sort(input));
    }
}
