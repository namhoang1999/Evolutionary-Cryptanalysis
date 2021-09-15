package ec.app.gaevolve.lcaes;

public class SBoxPosition {
    private int[] position, argMaxV, argMaxU;
    private long bestU, bestV;
    private int round;
    private double[][] LAT,DDT;
    private String outputBest = "";

    public SBoxPosition(int r) {
        this.round  = r;
        this.defaultSettings();
        this.LAT = Cipher.generateLinearTable();
        this.DDT = Cipher.generateDifferentialTable();
        this.argMaxV = maxValueIndexesV();
        this.argMaxU = maxValueIndexesU();
    }

    /**
     * Given a U then find V so that LAT[U][V] is maximum
     * @return
     */
    public int[] maxValueIndexesV() {
        int[] indexes = new int[LAT.length];

        for (int i = 0; i < LAT.length; i++) { 
            double best = 0;
            for (int j = 0; j < LAT.length; j++) {
                if (LAT[i][j] > best) {
                    indexes[i] = j;
                    best = LAT[i][j];
                }
            }
        }
        return indexes;
    }

    /**
     * Given a V then find U so that LAT[U][V] is maximum
     * @return
     */
    public int[] maxValueIndexesU() {
        int[] indexes = new int[LAT.length];

        for (int j = 0; j < LAT.length; j++) { 
            double best = 0;
            for (int i = 0; i < LAT.length; i++) {
                if (LAT[i][j] > best) {
                    indexes[j] = i;
                    best = LAT[i][j];
                }
            }
        }
        return indexes;
    }

    private double lastRound(long U) {
        double bias = 1;
        long V = 0;
        for (int i = 0; i < 64; i+=8) {
            int blockU = (int)(Long.rotateRight(U, i) & 0xff);
            int blockV = this.argMaxV[blockU];
            bias *= 2 * LAT[blockU][blockV];
            V ^=  Long.rotateLeft(blockV, i);
        }
        this.bestV = V;
        return bias/2;
    }

    public double calculateStatistic(long U, long V) {
        if ((U == 0) || (V == 0)) return 0;
        double bias = 0.5;
        for (int i = 0; i < 64; i += 8) {
            int blockU = (int)(Long.rotateRight(U, i) & 0b11111111);
            int blockV = (int)(Long.rotateRight(V, i) & 0b11111111);
            if (blockU != 0) 
                bias *= 2 * LAT[blockU][blockV];
        }
        return bias;
    }

    private double bestRoundApproximation(long U, int currPosition, int nextPosition) {
        long V;
        double curr, best = 0;

        int compareMask = nextPosition;

        if (Helper.bitSum(currPosition) == 1) {
            this.bestV = generateMask(currPosition, nextPosition, 0, 0, 0, 0, 0, 0, 0);
            best = calculateStatistic(U,this.bestV);   
        } else if (Helper.bitSum(currPosition) == 2) {
            for (int i = 1; i < 256; i++) {
                for (int j = 1; j < 256; j++) {
                    if ((i | j) == compareMask) {
                        V = generateMask(currPosition, i, j, 0, 0, 0, 0, 0, 0);
                        curr = calculateStatistic(U,V);
                        if (curr > best) {
                            best = curr;
                            this.bestV = V;
                        }
                    }
                }
            }
        } else if (Helper.bitSum(currPosition) == 3) {
            for (int i = 1; i < 256; i++) {
                for (int j = 1; j < 256; j++) {
                    for (int k = 1; k < 256; k++) {
                        if ((i | j | k) == compareMask) {
                            V = generateMask(currPosition, i, j, k, 0, 0, 0, 0, 0);
                            curr = calculateStatistic(U,V);
                            if (curr > best) {
                                best = curr;
                                this.bestV = V;
                            }
                        }
                    }       
                }
            }
        } else if (Helper.bitSum(currPosition) == 4) {
            for (int i = 1; i < 256; i++) {
                for (int j = 1; j < 256; j++) {
                    for (int k = 1; k < 256; k++) {
                        for (int l = 1; l < 256; l++) {
                            if ((i | j | k | l) == compareMask) {
                                V = generateMask(currPosition, i, j, k, l, 0, 0, 0, 0);
                                curr = calculateStatistic(U,V);
                                if (curr > best) {
                                    best = curr;
                                    this.bestV = V;
                                }
                            }
                        }           
                    }       
                }
            }
        } else if (Helper.bitSum(currPosition) == 5) {
            for (int a = 1; a < 256; a++) {
                for (int b = 1; b < 256; b++) {
                    for (int c = 1; c < 256; c++) {
                        for (int d = 1; d < 256; d++) {
                            for (int e = 1; e < 256; e++) {
                                if ((a | b | c | d | e) == compareMask) {
                                    V = generateMask(currPosition, a, b, c, d, e, 0, 0, 0);
                                    curr = calculateStatistic(U,V);
                                    if (curr > best) {
                                        best = curr;
                                        this.bestV = V;
                                    }
                                }
                            }
                        }
                    }
                }    
            }
        } else if (Helper.bitSum(currPosition) == 6) {
            for (int a = 1; a < 256; a++) {
                for (int b = 1; b < 256; b++) {
                    for (int c = 1; c < 256; c++) {
                        for (int d = 1; d < 256; d++) {
                            for (int e = 1; e < 256; e++) {
                                for (int f = 1; f < 256; f++) {
                                    if ((a | b | c | d | e | f) == compareMask) {
                                        V = generateMask(currPosition, a, b, c, d, e, f, 0, 0);
                                        curr = calculateStatistic(U,V);
                                        if (curr > best) {
                                            best = curr;
                                            this.bestV = V;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }    
            }
        } else if (Helper.bitSum(currPosition) == 7) {
            for (int a = 1; a < 256; a++) {
                for (int b = 1; b < 256; b++) {
                    for (int c = 1; c < 256; c++) {
                        for (int d = 1; d < 256; d++) {
                            for (int e = 1; e < 256; e++) {
                                for (int f = 1; f < 256; f++) {
                                    for (int g = 1; g < 256; g++) {
                                        if ((a | b | c | d | e | f | g) == compareMask) {
                                            V = generateMask(currPosition, a, b, c, d, e, f, g, 0);
                                            curr = calculateStatistic(U,V);
                                            if (curr > best) {
                                                best = curr;
                                                this.bestV = V;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }    
            }
        } else if (Helper.bitSum(currPosition) == 8) {
            for (int a = 1; a < 256; a++) {
                for (int b = 1; b < 256; b++) {
                    for (int c = 1; c < 256; c++) {
                        for (int d = 1; d < 256; d++) {
                            for (int e = 1; e < 256; e++) {
                                for (int f = 1; f < 256; f++) {
                                    for (int g = 1; g < 256; g++) {
                                        for (int h = 1; h < 256; h++) {
                                            if ((a | b | c | d | e | f | g | h) == compareMask) {
                                                V = generateMask(currPosition, a, b, c, d, e, f, g, h);
                                                curr = calculateStatistic(U,V);
                                                if (curr > best) {
                                                    best = curr;
                                                    this.bestV = V;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }    
            }
        }
        return best;
    }

    public double getBestBias() {
        double curr, best = 0;
        int currPosition = 0;
        long U;

        for (int round = 0; round < this.position.length; round++) 
            if (this.position[round] == 0) return 0;
        
        for (int round = 0; round < this.position.length; round++) {
            currPosition = this.position[round];
            if (round == 0) {
                if (Helper.bitSum(currPosition) == 1) {
                    int i = this.position[1];
                    this.bestV = generateMask(currPosition, i, 0, 0, 0, 0, 0, 0, 0);
                    U          = generateMask(currPosition, this.argMaxU[i], 0, 0, 0, 0, 0, 0, 0);
                    curr =  calculateStatistic(U, this.bestV);
                    
                    if (curr > best) {
                        best = curr;
                        this.bestU = U;
                    }
                } else if (Helper.bitSum(currPosition) == 2) {
                    for (int i = 1; i < 256; i++) {
                        for (int j = 1; j < 256; j++) {
                            this.bestV = generateMask(currPosition, i, j, 0, 0, 0, 0, 0, 0);
                            U          = generateMask(currPosition, this.argMaxU[i], this.argMaxU[j], 0, 0, 0, 0, 0, 0);
                            curr =  calculateStatistic(U, this.bestV);
                            
                            if (curr > best) {
                                best = curr;
                                this.bestU = U;
                            }
                        }
                    }
                } else if (Helper.bitSum(currPosition) == 3) {
                    for (int i = 1; i < 256; i++) {
                        for (int j = 1; j < 256; j++) {
                            for (int k = 1; k < 256; k++) {
                                this.bestV = generateMask(currPosition, i, j, k, 0, 0, 0, 0, 0);
                                U          = generateMask(currPosition, this.argMaxU[i], this.argMaxU[j], this.argMaxU[k], 0, 0, 0, 0, 0);
                                curr =  calculateStatistic(U, this.bestV);
                                
                                if (curr > best) {
                                    best = curr;
                                    this.bestU = U;
                                }
                            }
                        }
                    }
                } else if (Helper.bitSum(currPosition) == 4) {
                    for (int i = 1; i < 256; i++) {
                        for (int j = 1; j < 256; j++) {
                            for (int k = 1; k < 256; k++) {
                                for (int l = 1; l < 256; l++) {
                                    this.bestV = generateMask(currPosition, i, j, k, l, 0, 0, 0, 0);
                                    U          = generateMask(currPosition, this.argMaxU[i], this.argMaxU[j], this.argMaxU[k], this.argMaxU[l], 0, 0, 0, 0);
                                    curr =  calculateStatistic(U, this.bestV);
                                    
                                    if (curr > best) {
                                        best = curr;
                                        this.bestU = U;
                                    }
                                }
                            }
                        }
                    }
                } else if (Helper.bitSum(currPosition) == 5) {
                    for (int a = 1; a < 256; a++) {
                        for (int b = 1; b < 256; b++) {
                            for (int c = 1; c < 256; c++) {
                                for (int d = 1; d < 256; d++) {
                                    for (int e = 1; e < 256; e++) {
                                        this.bestV = generateMask(currPosition, a, b, c, d, e, 0, 0, 0);
                                        U          = generateMask(currPosition, this.argMaxU[a], this.argMaxU[b], this.argMaxU[c], this.argMaxU[d], this.argMaxU[e], 0, 0, 0);
                                        curr =  calculateStatistic(U, this.bestV);
                                        
                                        if (curr > best) {
                                            best = curr;
                                            this.bestU = U;
                                        }
                                    }
                                }
                            }
                        }    
                    }
                } else if (Helper.bitSum(currPosition) == 6) {
                    for (int a = 1; a < 256; a++) {
                        for (int b = 1; b < 256; b++) {
                            for (int c = 1; c < 256; c++) {
                                for (int d = 1; d < 256; d++) {
                                    for (int e = 1; e < 256; e++) {
                                        for (int f = 1; f < 256; f++) {
                                            this.bestV = generateMask(currPosition, a, b, c, d, e, f, 0, 0);
                                            U          = generateMask(currPosition, this.argMaxU[a], this.argMaxU[b], this.argMaxU[c], this.argMaxU[d], 
                                                                                    this.argMaxU[e], this.argMaxU[f], 0, 0);
                                            curr =  calculateStatistic(U, this.bestV);
                                            
                                            if (curr > best) {
                                                best = curr;
                                                this.bestU = U;
                                            }
                                        }
                                    }
                                }
                            }
                        }    
                    }
                } else if (Helper.bitSum(currPosition) == 7) {
                    for (int a = 1; a < 256; a++) {
                        for (int b = 1; b < 256; b++) {
                            for (int c = 1; c < 256; c++) {
                                for (int d = 1; d < 256; d++) {
                                    for (int e = 1; e < 256; e++) {
                                        for (int f = 1; f < 256; f++) {
                                            for (int g = 1; g < 256; g++) {
                                                this.bestV = generateMask(currPosition, a, b, c, d, e, f, g, 0);
                                                U          = generateMask(currPosition, this.argMaxU[a], this.argMaxU[b], this.argMaxU[c], this.argMaxU[d], 
                                                                                        this.argMaxU[e], this.argMaxU[f], this.argMaxU[g], 0);
                                                curr =  calculateStatistic(U, this.bestV);
                                                
                                                if (curr > best) {
                                                    best = curr;
                                                    this.bestU = U;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }    
                    }
                } else if (Helper.bitSum(currPosition) == 8) {
                    for (int a = 1; a < 256; a++) {
                        for (int b = 1; b < 256; b++) {
                            for (int c = 1; c < 256; c++) {
                                for (int d = 1; d < 256; d++) {
                                    for (int e = 1; e < 256; e++) {
                                        for (int f = 1; f < 256; f++) {
                                            for (int g = 1; g < 256; g++) {
                                                for (int h = 1; h < 256; h++) {
                                                    this.bestV = generateMask(currPosition, a, b, c, d, e, f, g, h);
                                                    U          = generateMask(currPosition, this.argMaxU[a], this.argMaxU[b], this.argMaxU[c], this.argMaxU[d], 
                                                                                            this.argMaxU[e], this.argMaxU[f], this.argMaxU[g], this.argMaxU[h]);
                                                    curr =  calculateStatistic(U, this.bestV);
                                                    
                                                    if (curr > best) {
                                                        best = curr;
                                                        this.bestU = U;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }    
                    }
                }
                outputBest += "U_" + round + ": " + Helper.hexPrint(this.bestU,16) + "\n"; 
                outputBest += "V_" + round + ": " + Helper.hexPrint(this.bestV,16) + "\n";
            } else {
                U = Cipher.pBoxEncrypt(this.bestV);
                
                if (round < this.position.length - 1) best *= 2 * bestRoundApproximation(U,currPosition,position[round+1]);
                else                                  best *= 2 * lastRound(U);

                outputBest += "U_" + round + ": " + Helper.hexPrint(U,16) + "\n"; 
                outputBest += "V_" + round + ": " + Helper.hexPrint(this.bestV,16) + "\n";
            }
            if (best == 0) return 0; 
        }

        return best;
    }

    public long generateMask(int n, int a, int b, int c, int d, int e, int f, int g, int h) {
        int[] arr = new int[]{a,b,c,d,e,f,g,h};
        int next = 0;
        long out = 0;
        for (int i = 7; i >= 0; i--) {
            int bit = Helper.getBit(n, i);
            if (bit == 1) {
                out ^= Long.rotateLeft(arr[next],8*i);
                next++;
                if (arr[next] == 0) return out;
            } 
        }
        return out;
    }

    /**
     * Set the default settings
     */
    public void defaultSettings() {
        //this.position = new int[]{0b0010,0b0010,0b0010,0b1110};
        this.position = new int[this.round];
    }

    /**
     * Mutator and Acccessor Methods
     */

    /**
     * Visualise and pretty print S-box position to a grid
     * @return visual representation of S-box position
     */
    public String getVisualSBoxPosition() {
        String out = "\n-------------\n";
        for (int i = 0; i < position.length; i++) {
            for (int j = 3; j >= 0; j--) {
                if (Helper.getBit(position[i], j) == 1) {
                    out += "|x ";
                } else {
                    out += "|  ";
                }
            }
            out += "|\n-------------\n";
        }

        return out;
    }

    /**
     * Encode S-box position into a 16-bit binary index
     * @return 16-bit binary index from S-box position
     */
    public int getIndex() {
        int index = 0;
        for (int i = 0; i < position.length; i++) {
            index <<= 8;
            index ^= position[i];
        }
        return index;
    }

    /**
     * Flip bit at a certain position
     * @param x coordinate x
     * @param y coordinate y
     */
    public void flipBit(int x, int y) {
        this.position[y] ^= (1 << x);
    }

    /**
     * Return the total number of active S-box
     * @return total number of active S-box
     */
    public int getTotalActiveSBox() {
        int count = 0;
        for (int round = 0; (round < this.position.length) && (this.position[round] != 0); round++) {
            count += Helper.bitSum(this.position[round]);
        }
        return count;
    }

    public int[] getSBoxPosition() {
        return this.position;
    }

    public void setSBoxPosition(int[] pos) {
        this.position = pos;
    }

    public void setSBoxPosition(boolean[] pos) {
        defaultSettings(); // must reset position array to 0
        for (int i = 0; i < this.position.length; i++) {
            for (int j = i*8; j < i*8+8; j++) {
                this.position[i] ^= (pos[j] ? 1 : 0);
                if (j != i*8+7) this.position[i] <<= 1;
            }
        }
    }

    public long getBestU() {
        return this.bestU;
    }

    public long getBestV() {
        return this.bestV;
    }

    public double[][] getLAT() {
        return this.LAT;
    }

/**
 * toString() method
 */
    // @Override
    // public String toString() {
    //     return "{" +
    //         " position='" + getSBoxPosition() + "'" +
    //         ", best='" + getBestBias() + "'" +
    //         ", bestU='" + getBestU() + "'" +
    //         ", bestV='" + getBestV() + "'" +
    //         "}";
    // }

    public String toStringApproximation() {
        String out = "";
        for (int i = 0; i < position.length; i++) {
            out += Integer.toBinaryString(position[i]) + " ";
        }
        return out;
    }

    public String getBest() {
        return this.outputBest;
    }
}
