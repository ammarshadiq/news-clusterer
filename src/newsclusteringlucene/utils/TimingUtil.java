/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package newsclusteringlucene.utils;

import java.text.NumberFormat;

public class TimingUtil {

    private static long[] TIME_FACTOR = {60 * 60 * 1000, 60 * 1000, 1000};

    /**
     * Calculate the elapsed time between two times specified in milliseconds.
     * @param start The start of the time period
     * @param end The end of the time period
     * @return a string of the form "XhYmZs" when the elapsed time is X hours, Y minutes and Z seconds or null if start > end.
     */
    public static String elapsedTime(long start, long end) {
        if (start > end) {
            return null;
        }

        long[] elapsedTime = new long[TIME_FACTOR.length];

        for (int i = 0; i < TIME_FACTOR.length; i++) {
            elapsedTime[i] = start > end ? -1 : (end - start) / TIME_FACTOR[i];
            start += TIME_FACTOR[i] * elapsedTime[i];
        }

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < elapsedTime.length; i++) {
            if (i > 0) {
                buf.append(":");
            }
            buf.append(nf.format(elapsedTime[i]));
        }
        return buf.toString();
    }
}
