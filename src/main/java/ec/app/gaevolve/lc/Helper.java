package ec.app.gaevolve.lc;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;

public class Helper {
	public static int coverCount = 0;

	/**
	 * Generate random round keys
	 * @param round: number of rounds
	 * @return an array of round keys
	 */
	public static int[] randomKeys(int round) {
		// Using random library is not secure at all!
		Random r = new Random();
		
		int[] keys = new int[round];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = r.nextInt(65535);
		}
		
		return keys;
	}

	/**
	 * Print a number in a nice binary or hex format
	 * @param n: Number to print
	 * @param base: the base of the format (2-binary, 16-hex)
	 */
	public static String intf(int n, int base) {
		String a = "";
		if (base == 16) 
			a = String.format("%4s", Integer.toHexString(n)).replace(" ","0");
		else if (base == 2) 
			a = String.format("%4s.%4s.%4s.%4s", Integer.toBinaryString((n& 0xf000)>>12), 
				Integer.toBinaryString((n& 0x0f00)>>8), 
				Integer.toBinaryString((n& 0x00f0)>>4), 
				Integer.toBinaryString(n& 0x000f)).replace(" ","0");
		return a;
	}
	
	/**
	 * Return the i-th bit of the binary n 
	 * @param n: Input number
	 * @param i: index of the bit to extract (0 is the most right)
	 * @return i-th bit of n
	 */
	public static int getBit(int n, int i) {
        return (n >> i) & 1;
    }
	
	/**
	 * Set bit of a binary
	 * @param n: Input number
	 * @param i: Index of the bit to set (0 is the most right)
	 * @param v: Value of the bit to set
	 * @return New binary with i-th bit set
	 */
	public static int setBit(int n, int i, int v) {
		if (v == 1) return n |= (1 << i);
		else		return n &= ~(1 << i);
	}

	/**
     * Calculate set bits from a binary (e.g. bitSum(0101) == 2)
     * @param x binary input
     * @return total number of set bits in the binary
     */
    static int bitSum(int x) {
        int count = 0;
        for (int val = x; val > 0; count++) {
            val = val & (val - 1); 
        }
        return count;
    }

	public static int generateMask(int n, int i, int j, int k, int l) {
        if (n == 0b0000)      return 0;
        else if (n == 0b0001) return i;
        else if (n == 0b0010) return i << 4;
        else if (n == 0b0011) return i << 4 ^ j; 
        else if (n == 0b0100) return i << 8;
        else if (n == 0b0101) return i << 8 ^ j;
        else if (n == 0b0110) return i << 8 ^ j << 4;
        else if (n == 0b0111) return i << 8 ^ j << 4 ^ k;
        else if (n == 0b1000) return i << 12;
        else if (n == 0b1001) return i << 12 ^ j;
        else if (n == 0b1010) return i << 12 ^ j << 4;
        else if (n == 0b1011) return i << 12 ^ j << 4 ^ k;
        else if (n == 0b1100) return i << 12 ^ j << 8;
        else if (n == 0b1101) return i << 12 ^ j << 8 ^ k;
        else if (n == 0b1110) return i << 12 ^ j << 8 ^ k << 4;
        else if (n == 0b1111) return i << 12 ^ j << 8 ^ k << 4 ^ l;
        return 0;
    }
	
	/**
	 * Generate Differential Distribution table for Differential Cryptanalysis
	 * @return Linear Approximation of the input S-Box
	 */
	public static double[][] generateLinearTable() {
        double[][] approximation = new double[16][16];
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                double count = 0;
                for (int i = 0; i < 16; i++) {
                    int a = i&x ^ HeysCipher.sBox[i]&y;
                    int f = 0;
                    while (a > 0) {
                        f ^= a & 1;
                        a >>= 1;
                    }
                    if (f != 0) count += 1;
                }
                approximation[x][y] = Math.abs(count-8)/16;
            }
        }
        return approximation;
    }

	/**
	 * Generate Differential Distribution table for Differential Cryptanalysis
	 * @return Linear Approximation of the input S-Box
	 */
	public static double[][] generateDifferentialTable() {
        double[][] ddt = new double[16][16];
        for (int x = 0; x <= 0b1111; x++) {
            int y = HeysCipher.sBox[x];
			for (int x2 = 0; x2 <= 0b1111; x2++) {
                int dx = x ^ x2;
				
				int y2 = HeysCipher.sBox[x2];
				int dy = y ^ y2;
				
				ddt[dx][dy]++;
            }
        }
        return ddt;
    }

	/**
	 * Write an array to a file
	 * @param path: Path to file
	 * @param data: Array to write
	 */
	public static void writeFile(String path, double[] arr) {
		try {
				FileWriter myWriter = new FileWriter(path,false);
				for (int i = 0; i < arr.length; i++) {
					myWriter.write(arr[i] + " ");
				}
			  
		    	myWriter.close();
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
	}

	/**
	 * Read plaintext-ciphertext from a file into an array
	 * @param path: Path to file
	 * @return AArray of plaintext-ciphertext pairs 
	 */
	public static double[] readFile(String path, int length) {
		System.out.println("Reading from: " + path);
		double[] arr = new double[length];
		try {
			Scanner scanner = new Scanner(new File(path));
			String[] line = scanner.nextLine().split(" ");
			for (int i = 0; i < line.length; i++) {
				arr[i] = Double.parseDouble(line[i]);
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return arr;
	}

}
