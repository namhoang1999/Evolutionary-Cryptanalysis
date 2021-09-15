package ec.app.gaestimate.lc.estimate;

public class SBoxEstimate {
    public int[] plaintext, ciphertext; 
    public int plaintextMask, ciphertextMask, position; // 16-bit mask position
    public final int SIZE = 3000;

    public SBoxEstimate(String path) {
        //"data/data0.txt"
        int[] data = Helper.readFile(path, SIZE);
        plaintext  = new int[data.length];
        ciphertext = new int[data.length];

        for (int i = 0; i < data.length; i++) {
            plaintext[i]  = data[i] >> 16;
            ciphertext[i] = data[i] & 0xff;
        }
    }

    public double estimate() {
        if (plaintextMask == 0 || ciphertextMask == 0) return 0;
        double approximation = 0;
        double best = 0;
        if (Helper.bitSum(this.position) == 1) {
            for (int a = 1; a <= 0b1111; a++) {
                int activeSboxMask = activeSBox(this.position);
                int subKeyBits = generateMask(activeSboxMask, a, 0, 0, 0);
                int mask = generateMask(activeSboxMask, 0xf, 0, 0, 0);
                approximation = bestBias(subKeyBits,mask);
                if (approximation > best) best = approximation;
            }
        } else if (Helper.bitSum(this.position) == 2) {
            for (int a = 1; a <= 0b1111; a++) {
                for (int b = 1; b <= 0b1111; b++) {
                    int activeSboxMask = activeSBox(this.position);
                    int subKeyBits = generateMask(activeSboxMask, a, b, 0, 0);
                    int mask = generateMask(activeSboxMask, 0xf, 0xf, 0, 0);
                    approximation = bestBias(subKeyBits,mask);
                    if (approximation > best) best = approximation;
                }   
            }
        } else if (Helper.bitSum(this.position) == 3) {
            for (int a = 1; a <= 0b1111; a++) {
                for (int b = 1; b <= 0b1111; b++) {
                    for (int c = 1; c <= 0b1111; c++) {
                        int activeSboxMask = activeSBox(this.position);
                        int subKeyBits = generateMask(activeSboxMask, a, b, c, 0);
                        int mask = generateMask(activeSboxMask, 0xf, 0xf, 0xf, 0);
                        approximation = bestBias(subKeyBits,mask);
                        if (approximation > best) best = approximation;
                    }
                }   
            }
        } else if (Helper.bitSum(this.position) == 4) {
            for (int a = 1; a <= 0b1111; a++) {
                for (int b = 1; b <= 0b1111; b++) {
                    for (int c = 1; c <= 0b1111; c++) {
                        for (int d = 1; d <= 0b1111; d++) {
                            int activeSboxMask = activeSBox(this.position);
                            int subKeyBits = generateMask(activeSboxMask, a, b, c, d);
                            int mask = generateMask(activeSboxMask, 0xf, 0xf, 0xf, 0xf);
                            approximation = bestBias(subKeyBits,mask);
                            if (approximation > best) best = approximation;
                        }
                    }
                }   
            }
        }
        // return Math.abs(best-1500)/3000;
        if (best == 0) return 0;
        return Math.abs(best-1500)/3000;
    }

    public double bestBias(int k, int m) {
        if ((plaintextMask == 0) || (ciphertextMask == 0)) return 0;
        int a = 0, count = 0;
        for (int i = 0; i < ciphertext.length; i++) {
            a = ciphertext[i] & m;
            a = a ^ k;
            a = HeysCipher.sBoxEncrypt(a, false);

            count += Helper.bitXOR(plaintextMask & plaintext[i]) ^ Helper.bitXOR(ciphertextMask & ciphertext[i]); 
        }
        return count;
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
        
        plaintextMask  = this.position >> 16;
        ciphertextMask = this.position & 0xff;
    }

    public void setPosition(int a) {
        this.position = a;
        plaintextMask  = this.position >> 16;
        ciphertextMask = this.position & 0xff;
    }
}