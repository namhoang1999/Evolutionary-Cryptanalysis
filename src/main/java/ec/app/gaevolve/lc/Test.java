package ec.app.gaevolve.lc;

public class Test {
    public static void main(String[] args) {
        SBoxPosition sbox = new SBoxPosition(3);
        int[] a = sbox.maxValueIndexesU();
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);   
    }
}
