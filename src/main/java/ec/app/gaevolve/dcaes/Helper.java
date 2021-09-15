package ec.app.gaevolve.dcaes;
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
	public static long[] randomKeys(int round,int seed) {
		// Using random library is not secure at all!
		Random r = new Random();
		r.setSeed(seed);
		
		long[] keys = new long[round];
		for (int i = 0; i < keys.length; i++) {
			keys[i] = r.nextLong();
		}
		
		return keys;
	}


	public static String binaryPrint(long inputBlock, int mode) {
		switch(mode) {
			case 16: return String.format("%16s", Long.toBinaryString(inputBlock)).replace(' ', '0');
			case 32: return String.format("%32s", Long.toBinaryString(inputBlock)).replace(' ', '0');
			case 64: return String.format("%64s", Long.toBinaryString(inputBlock)).replace(' ', '0');
			default: return "[PrettyPrint] mode parameter is not correctly defined!";
		}
	}

	
	public static String binaryPrint(int inputBlock, int mode) {
		switch(mode) {
			case  8: return String.format( "%8s", Integer.toBinaryString(inputBlock)).replace(' ', '0');
			case 16: return String.format("%16s", Integer.toBinaryString(inputBlock)).replace(' ', '0');
			case 32: return String.format("%32s", Integer.toBinaryString(inputBlock)).replace(' ', '0');
			case 64: return String.format("%64s", Integer.toBinaryString(inputBlock)).replace(' ', '0');
			default: return "[PrettyPrint] mode parameter is not correctly defined!";
		}
	}

	public static String hexPrint(long inputBlock, int mode) {
		switch(mode) {
			case  2: return String.format( "%2s", Long.toHexString(inputBlock)).replace(' ', '0');
			case  4: return String.format( "%4s", Long.toHexString(inputBlock)).replace(' ', '0');
			case  8: return String.format( "%8s", Long.toHexString(inputBlock)).replace(' ', '0');
			case 16: return String.format("%16s", Long.toHexString(inputBlock)).replace(' ', '0');
			default: return "[PrettyPrint] mode parameter is not correctly defined!";
		}
	}

	
	public static String hexPrint(int inputBlock, int mode) {
		switch(mode) {
			case  1: return String.format( "%1s", Integer.toHexString(inputBlock)).replace(' ', '0');
			case  2: return String.format( "%2s", Integer.toHexString(inputBlock)).replace(' ', '0');
			case  4: return String.format( "%4s", Integer.toHexString(inputBlock)).replace(' ', '0');
			case  8: return String.format( "%8s", Integer.toHexString(inputBlock)).replace(' ', '0');
			case 16: return String.format("%16s", Integer.toHexString(inputBlock)).replace(' ', '0');
			default: return "[PrettyPrint] mode parameter is not correctly defined!";
		}
	}
	
    public static int getBit(long n, int i) {
        return (int)(Long.rotateRight(n,i) & 1);
    }

    public static long setBit(long n, int i) {
        return n ^ Long.rotateLeft(1, i);
    }

	/**
     * Calculate set bits from a binary (e.g. bitSum(0101) == 2)
     * @param x binary input
     * @return total number of set bits in the binary
     */
    public static int bitSum(int x) {
        int count = 0;
        for (int val = x; val > 0; count++) {
            val = val & (val - 1); 
        }
        return count;
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

