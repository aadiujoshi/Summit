/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Predicate;

public class Time{

    public static final long INIT_TIME = timeMs();

    public static final long MS_IN_S = 1000;
    public static final long NS_IN_MS = 1000000;
    public static final long NS_IN_S = 1000000000;

    public static long timeMs(){
        return System.currentTimeMillis();
    }

    public static long timeNs(){
        return System.nanoTime();
    }

    public static void nanoDelay(long nanos)
    {
        if(nanos < 0) return;
        
        final long end = System.nanoTime() + nanos;
        long timeLeft = nanos;
        do {
            timeLeft = end - System.nanoTime();
        } while (timeLeft > 0);
    }

    /**
     * Waits while the Predicate {@code bool} returns true.
     * No value is passed to the Predicate when calling {@code test()}
     * 
     * @param bool The {@link Predicate} to wait for while true
     */
    public static void waitWhile(Predicate<Object> bool){
        while(bool.test(null)){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static String timestamp(char sep){
        LocalTime t = LocalTime.now();
        return "[" + t.getHour() + sep + t.getMinute() + sep + t.getSecond() + "]";
    }
}