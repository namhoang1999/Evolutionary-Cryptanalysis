package ec.app.gaevolve.lc;

public class SBoxPosition {
    private int[] position;
    private int bestU, bestV;
    private int round;

    public SBoxPosition(int round) {
        this.round = round;
        this.defaultSettings();
    }

    private double calculateStatistic(int U, int V) {
        double bias = 1;
        if ((U == 0) || (V == 0)) return 0;
        for (int i = 0; i <= 0b1111; i += 4) {
            int blockU = U>>i & 0b1111;
            int blockV = V>>i & 0b1111;
            if ((blockU != 0) && (blockV != 0)) {
                bias *= 2 * HeysCipher.LAT[blockU][blockV];
            }
        }
        return bias/2;
    }

    /**
     * Given a V then find U so that LAT[U][V] is maximum
     * @return
     */
    public int[] maxValueIndexesU() {
        int[] indexes = new int[HeysCipher.LAT.length];

        for (int j = 0; j < HeysCipher.LAT.length; j++) { 
            double best = 0;
            for (int i = 0; i < HeysCipher.LAT.length; i++) {
                if (HeysCipher.LAT[i][j] > best) {
                    indexes[j] = i;
                    best = HeysCipher.LAT[i][j];
                }
            }
        }
        return indexes;
    }

    private double bestRoundApproximation(int U, int currPosition, int nextPosition) {
        int V;
        double curr, best = 0;
        if (Helper.bitSum(currPosition) == 1) {
            this.bestV = Helper.generateMask(currPosition, nextPosition, 0, 0, 0);
            best = calculateStatistic(U,this.bestV);   
        } else if (Helper.bitSum(currPosition) == 2) {
            for (int i = 1; i <= 0b1111; i++) {
                for (int j = 1; j <= 0b1111; j++) {
                    if ((i | j) == nextPosition) {
                        V = Helper.generateMask(currPosition, i, j, 0, 0);
                        curr = calculateStatistic(U,V);
                        if (curr > best) {
                            best = curr;
                            this.bestV = V;
                        }
                    }
                }
            }
        } else if (Helper.bitSum(currPosition) == 3) {
            for (int i = 1; i <= 0b1111; i++) {
                for (int j = 1; j <= 0b1111; j++) {
                    for (int k = 1; k <= 0b1111; k++) {
                        if ((i | j | k) == nextPosition) {
                            V = Helper.generateMask(currPosition, i, j, k, 0);
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
            for (int i = 1; i <= 0b1111; i++) {
                for (int j = 1; j <= 0b1111; j++) {
                    for (int k = 1; k <= 0b1111; k++) {
                        for (int l = 1; l <= 0b1111; l++) {
                            if ((i | j | k | l) == nextPosition) {
                                V = Helper.generateMask(currPosition, i, j, k, l);
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
        return best;
    }

    public double getBestBias() {
        double curr, best = 0;
        int currPosition = 0, U;

        for (int round = 0; round < this.position.length; round++) {
            if (this.position[round] == 0) return 0;
        }

        for (int round = 0; (this.position[round] != 0) && (round < this.position.length - 1); round++) {
            currPosition = this.position[round];
            if (round == 0) {
                if (Helper.bitSum(currPosition) == 1) {
                    for (int i = 1; i <= 0b1111; i++) {
                        U = Helper.generateMask(currPosition, i, 0, 0, 0);
                        curr = bestRoundApproximation(U,currPosition,this.position[round+1]);
                        if (curr > best) {
                            best = curr;
                            this.bestU = U;
                        }
                    }
                } else if (Helper.bitSum(currPosition) == 2) {
                    for (int i = 1; i <= 0b1111; i++) {
                        for (int j = 1; j <= 0b1111; j++) {
                            U = Helper.generateMask(currPosition, i, j, 0, 0);
                            curr = bestRoundApproximation(U,currPosition,this.position[round+1]);
                            if (curr > best) {
                                best = curr;
                                this.bestU = U;
                            }   
                        }
                    }
                } else if (Helper.bitSum(currPosition) == 3) {
                    for (int i = 1; i <= 0b1111; i++) {
                        for (int j = 1; j <= 0b1111; j++) {
                            for (int k = 1; k <= 0b1111; k++) {
                                U = Helper.generateMask(currPosition, i, j, k, 0);
                                curr = bestRoundApproximation(U,currPosition,this.position[round+1]);
                                if (curr > best) {
                                    best = curr;
                                    this.bestU = U;
                                }   
                            }
                        }
                    }
                } else if (Helper.bitSum(currPosition) == 4) {
                    for (int i = 1; i <= 0b1111; i++) {
                        for (int j = 1; j <= 0b1111; j++) {
                            for (int k = 1; k <= 0b1111; k++) {
                                for (int l = 1; l <= 0b1111; l++) {
                                    U = Helper.generateMask(currPosition, i, j, k, l);
                                    curr = bestRoundApproximation(U,currPosition,this.position[round+1]);
                                    if (curr > best) {
                                        best = curr;
                                        this.bestU = U;
                                    }   
                                }
                            }
                        }
                    }
                }
            } else {
                U = HeysCipher.pBoxEncrypt(this.bestV);
                curr = bestRoundApproximation(U,currPosition,position[round+1]);    
                best *= 2 * curr;
                if (best == 0) return 0;
            }
        }

        return best;
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
            index <<= 4;
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

    public void setSBoxPosition(boolean[] pos) {
        defaultSettings(); // must reset position array to 0
        for (int i = 0; i < this.position.length; i++) {
            for (int j = i*4; j < i*4+4; j++) {
                this.position[i] ^= (pos[j] ? 1 : 0);
                if (j != i*4+3) this.position[i] <<= 1;
            }
        }
        //this.position = pos;
    }

    public int getBestU() {
        return this.bestU;
    }

    public int getBestV() {
        return this.bestV;
    }

/**
 * toString() method
 */
    @Override
    public String toString() {
        return "{" +
            " position='" + getSBoxPosition() + "'" +
            ", best='" + getBestBias() + "'" +
            ", bestU='" + getBestU() + "'" +
            ", bestV='" + getBestV() + "'" +
            "}";
    }

    public String toStringApproximation() {
        String out = "";
        for (int i = 0; i < position.length; i++) {
            out += Integer.toBinaryString(position[i]) + " ";
        }
        return out;
    }
}
