/*
* BPA project by Aadi Joshi, Aditya Bhattacharya, Sanjay Raghav, Aadithya Ramakrishnan Sriram 
* 2022
*/
package summit.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.function.Predicate;

public class Time{

    public static final long INIT_TIME = timeMs();

    public static final long MS_IN_S = 1000;
    public static final long NS_IN_MS = 1000000;
    public static final long NS_IN_S = 1000000000;

    
    /** 
     * @return long
     */
    public static long timeMs(){
        return System.currentTimeMillis();
    }

    
    /** 
     * @return long
     */
    public static long timeNs(){
        return System.nanoTime();
    }

    
    /** 
     * @param nanos
     */
    public static void nanoDelay(long nanos) {
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

    
    /** 
     * @param sep
     * @return String
     */
    //[month day year][Time.timestamp(sep)]
    public static String datetimeStamp(char sep){
        LocalDate d = LocalDate.now();

        return "[" + f(d.getMonth().getValue()) + sep + 
                       f(d.getDayOfMonth()) + sep + 
                        f(d.getYear()) + "]" + 
                        timestamp(sep);
    }

    
    /** 
     * @param sep
     * @return String
     */
    //[hh `sep` mm `sep` ss]
    //[08:45:01]
    public static String timestamp(char sep){
        LocalTime t = LocalTime.now();
        return "[" + f(t.getHour()) + sep + f(t.getMinute()) + sep + f(t.getSecond()) + "]";
    }

    
    /** 
     * @param i
     * @return String
     */
    //format time to 2 numbers 
    //example : "9" becomes "09"
    private static String f(int i){
        return (i < 10) ? "0"+i : i+"";
    }
}