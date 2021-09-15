package ec.app.gaestimate.dc.estimate;

public class SBoxEstimate {
    public int[] pairs; 
    public int deltaP, deltaC, position; // 16-bit mask position
    public final int SIZE = 3000;

    public SBoxEstimate(String path) {
        pairs = Helper.readFile(path, SIZE);
    }

    public double estimate() {
        int subKey, mask = activeSBox(this.position);
        double approximation = 0, best = 0;
        
        if (Helper.bitSum(this.position) == 1) {
            for (int a = 1; a <= 0b1111; a++) {
                subKey = generateMask(mask, a, 0, 0, 0);
                approximation = bestBias(subKey);
                if (approximation > best) best = approximation;
            }
        } else if (Helper.bitSum(this.position) == 2) {
            for (int a = 1; a <= 0b1111; a++) {
                for (int b = 1; b <= 0b1111; b++) {
                    subKey = generateMask(mask, a, b, 0, 0);
                    approximation = bestBias(subKey);
                    if (approximation > best) best = approximation;
                }   
            }
        } else if (Helper.bitSum(this.position) == 3) {
            for (int a = 1; a <= 0b1111; a++) {
                for (int b = 1; b <= 0b1111; b++) {
                    for (int c = 1; c <= 0b1111; c++) {
                        subKey = generateMask(mask, a, b, c, 0);
                        approximation = bestBias(subKey);
                        if (approximation > best) best = approximation;
                    }
                }   
            }
        } else if (Helper.bitSum(this.position) == 4) {
            for (int a = 1; a <= 0b1111; a++) {
                for (int b = 1; b <= 0b1111; b++) {
                    for (int c = 1; c <= 0b1111; c++) {
                        for (int d = 1; d <= 0b1111; d++) {
                            subKey = generateMask(mask, a, b, c, d);
                            approximation = bestBias(subKey);
                            if (approximation > best) best = approximation;
                        }
                    }
                }   
            }
        }
        if (best == 0) return 0;
        return best/SIZE;
    }

    public double bestBias(int k) {
        if (deltaP == 0 || deltaP == 0) return 0;

        int p1,p2,c1,c2,y1,y2,count=0;
        for (p1 = 0; p1 < pairs.length; p1++) {
            p2 = p1 ^ deltaP;
            c1 = pairs[p1];
            if (p2 < pairs.length) {	
                c2 = pairs[p2];

                y1 = c1 ^ k;
                y1 = HeysCipher.sBoxEncrypt(y1, false);
                
                y2 = c2 ^ k;
                y2 = HeysCipher.sBoxEncrypt(y2, false);

                if ((y1^y2) == deltaC) count++;
            }
        }
        return count/2;
    }

    public int activeSBox(int n) {
        int mask = 0;
        for (int i = 0; i < 16; i+= 4) 
            if (((n >> i) & 0xf) > 0) mask = Helper.setBit(mask, i/4, 1);
        return mask;
    }

    public int generateMask(int n, int a, int b, int c, int d) {
        int[] arr = new int[]{a,b,c,d};
        int next = 0, out = 0;
        for (int i = 3; i >= 0; i--) {
            int bit = Helper.getBit(n, i);
            if (bit == 1) {
                out ^= arr[next] << (4*i);
                next++;
                if ((next == arr.length) || (arr[next] == 0)) return out;
            } 
        }
        return out;
    }

    public void setPosition(boolean[] a) {
        this.position = 0;
        for (int i = 0; i < a.length; ++i) 
            if (a[i]) this.position ^= 1 << (a.length - i - 1);
        
        deltaP = this.position >>> 16;
        deltaC = this.position & 0xff;
    }

    public void setPosition(int a) {
        this.position = a;
        deltaP = this.position >>> 16;
        deltaC = this.position & 0xff;
    }
}