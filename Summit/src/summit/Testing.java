package summit;
import java.awt.Point;
import java.util.Arrays;

import summit.gfx.Renderer;
import summit.util.Region;

public class Testing {

    public static void main(String args[]){

//         x: 66.4, y: 80.0, width: 32.0, height: 16.0
// x: 128.0, y: 80.0, width: 32.0, height: 16.0
// x: 189.6, y: 80.0, width: 32.0, height: 16.0
// 66  79

        System.out.println(new Region(66.4f, 80f, 32f, 16f).contains(66, 79));

        // int[][] arr = new int[][]{ {1, 2, 3}, {4, 5, 6}, {7, 8, 9} };

        // arr = Renderer.rotate(arr);

        // for (int[] is : arr) {
        //     System.out.println(Arrays.toString(is));
        // }

        // int[][] arr = new int[][]{{1,2,4,5,56,7,7,8,8}};

        // // int[][] arr2 = new int[][]{{234,5432,5432,4532,4523,2435,2543,4325}};

        // arr = Renderer.flip(arr, Renderer.FLIP_X);

        // // arr = arr2;

        // System.out.println(Arrays.toString(arr[0]) + "fdfdasfdas");

        //mem test
        // int[] a1 = new int[]{1,2,3,5};

        // int[] a2 = a1;

        // System.out.println(Arrays.toString(a1));

        // int[] a3 = new int[]{14,23,4321,4321,53};

        // a2 = a3;

        // System.out.println(Arrays.toString(a1));


        // int a = 43;
        // int b = 59;

        // int masked = (a) | (b << 8);

        // System.out.println(Integer.toBinaryString(a));

        // System.out.println("a: " + (masked >> 0 & 0b11111111));
        // System.out.println("b: " + (masked >> 8 & 0b11111111));
    }
}
