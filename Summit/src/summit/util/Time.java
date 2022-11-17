package summit.util;

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
}