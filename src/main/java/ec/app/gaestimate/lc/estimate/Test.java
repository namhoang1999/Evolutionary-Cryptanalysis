package ec.app.gaestimate.lc.estimate;
import java.util.Random;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Random r = new Random();
        double sum = 0;
        for (int seed = 0; seed < 10; seed++) {
            r.setSeed(seed);
            for (int j = 0; j < 10; j++) {
                SBoxEstimate a = new SBoxEstimate("C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/data/5-round/data"+j+".txt");
                double best = 0;
                for (int i = 0; i < 204800; i++) {
                    int n = r.nextInt();
                    a.setPosition(n);
                    double est = a.estimate();
                    if (est > best) {
                        best = est;
                        System.out.println("data: " + j + " | " + i + " " + Integer.toBinaryString(n) + " " + est);
                    }
                }
                sum += best;
            }
        }
        System.out.println(sum/100);
    }    
}
