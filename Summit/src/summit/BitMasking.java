package summit;
import java.awt.Point;

public class BitMasking {

    public static void main(String args[]){

        //mem test
        Point p1 = new Point(100, 100);

        Point p2 = p1;

        p2.setLocation(200, 100);

        System.out.println(p1);

        // int a = 43;
        // int b = 59;

        // int masked = (a) | (b << 8);

        // System.out.println(Integer.toBinaryString(a));

        // System.out.println("a: " + (masked >> 0 & 0b11111111));
        // System.out.println("b: " + (masked >> 8 & 0b11111111));
    }
}
