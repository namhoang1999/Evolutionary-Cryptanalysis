package ec.app.gaevolve.lcaes;

import java.util.Arrays;

public class ApproximationTest {
    public static void main(String[] args) {
        SBoxPosition sbox = new SBoxPosition(3);
        double[][] lat = sbox.getLAT();

        // int[] arr = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 3, 5, 6, 9, 10, 12, 17, 18, 20, 24, 33, 34, 36, 40, 48, 65, 66, 68, 72, 80, 96, 129, 130, 132, 136, 144, 160, 192, 7, 11, 13, 14, 19, 21, 22, 25, 26, 28, 35, 37, 38, 41, 42, 44, 49, 50, 52, 56, 67, 69, 70, 73, 74, 76, 81, 82, 84, 88, 97, 98, 100, 104, 112, 131, 133, 134, 137, 138, 140, 145, 146, 148, 152, 161, 162, 164, 168, 176, 193, 194, 196, 200, 208, 224, 15, 23, 27, 29, 30, 39, 43, 45, 46, 51, 53, 54, 57, 58, 60, 71, 75, 77, 78, 83, 85, 86, 89, 90, 92, 99, 101, 102, 105, 106, 108, 113, 114, 116, 120, 135, 139, 141, 142, 147, 149, 150, 153, 154, 156, 163, 165, 166, 169, 170, 172, 177, 178, 180, 184, 195, 197, 198, 201, 202, 204, 209, 210, 212, 216, 225, 226, 228, 232, 240, 31, 47, 55, 59, 61, 62, 79, 87, 91, 93, 94, 103, 107, 109, 110, 115, 117, 118, 121, 122, 124, 143, 151, 155, 157, 158, 167, 171, 173, 174, 179, 181, 182, 185, 186, 188, 199, 203, 205, 206, 211, 213, 214, 217, 218, 220, 227, 229, 230, 233, 234, 236, 241, 242, 244, 248};
        // int[] arr = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 3, 5, 6, 9, 10, 12, 17, 18, 20, 24, 33, 34, 36, 40, 48, 65, 66, 68, 72, 80, 96, 129, 130, 132, 136, 144, 160, 192, 7, 11, 13, 14, 19, 21, 22, 25, 26, 28, 35, 37, 38, 41, 42, 44, 49, 50, 52, 56, 67, 69, 70, 73, 74, 76, 81, 82, 84, 88, 97, 98, 100, 104, 112, 131, 133, 134, 137, 138, 140, 145, 146, 148, 152, 161, 162, 164, 168, 176, 193, 194, 196, 200, 208, 224};
        int[] arr = new int[]{0, 1, 2, 4, 8, 16, 32, 64, 128, 3, 5, 6, 9, 10, 12, 17, 18, 20, 24, 33, 34, 36, 40, 48, 65, 66, 68, 72, 80, 96, 129, 130, 132, 136, 144, 160, 192};
        
        int[] pos1 = new int[]{0b00000010,
                               0b00000001,
                               0b01000000};
        sbox.setSBoxPosition(pos1);

        double bias1 = sbox.getBestBias();
        System.out.println(bias1);
        System.out.println(sbox.getBest());

        System.out.println(lat[0x2d][0x01]);
        System.out.println(lat[0x02][0x04]);
        System.out.println(lat[0x01][0x48]);

        // double best = 0;
        // for (int i = 1; i < arr.length; i++) {
        //     for (int j = 1; j < arr.length; j++) {
        //         int a = arr[i];
        //         int b = arr[j];
        //         if ((lat[a][b] > best) && (lat[a][b] != 0)) {
        //             best = lat[i][j];
        //         }
        //     }
        // }
        // System.out.println(best);
        

        // System.out.println(sbox.getBestBias());
        // double best = 0;
        // for (int i = 1; i < arr.length; i++) {
        //     System.out.println(i);
        //     for (int j = 1; j < arr.length; j++) {
        //         for (int k = 1; k < arr.length; k++) {
        //             int[] pos = new int[]{arr[i],arr[j],arr[k]};
        //             sbox.setSBoxPosition(pos);

        //             double bias = sbox.getBestBias();
        //             if (bias > best) {
        //                 best = bias;
        //                 System.out.println(Integer.toBinaryString(sbox.getIndex()) + " " + bias);
        //             }
        //         }
        //     }
        // }
    }
}
