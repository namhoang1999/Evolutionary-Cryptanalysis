package ec.app.gpevolve.dc;
import ec.util.*;

import java.util.Arrays;

import ec.*;
import ec.app.gpevolve.lc.cipher.SBoxPosition;
import ec.gp.*;
import ec.gp.koza.*;

public class Differential extends GPProblem {
    private static final long serialVersionUID = 1;

    public SBoxPosition a;
    public int x,y;
    public static int maxX,maxY;
    public double best;

    public static double[][] APPROXIMATION;
    public double[] bias;
    public int[] nullCount;

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state, base);
        
        // verify our input is the right class (or subclasses from it)
        if (!(input instanceof DifferentialData))
            state.output.fatal("GPData class must subclass from " + DifferentialData.class, base.push(P_DATA), null);

        // Generate Linear SBoxPosition Table
        APPROXIMATION = Helper.generateLinearTable();
        int rounds = state.parameters.getInt(base.push("x"),null,1);

        // setup default position 
        a = new SBoxPosition(rounds);
        maxX = 4;
        maxY = rounds;

        switch (rounds) {
            case 3: 
                this.bias = Helper.readFile("C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/dc-3.txt",16*16*16); 
                this.best = 0.03515625; break;
            case 4: 
                this.bias = Helper.readFile("C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/dc-4.txt",16*16*16*16); 
                this.best = 0.0032958984375; break;
            case 5: 
                this.bias = Helper.readFile("C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/dc-5.txt",16*16*16*16*16); 
                this.best = 0.00061798095; break;
            default:
                this.best = 1;
        }

        nullCount = new int[bias.length];
    }
        
    public void evaluate(final EvolutionState state, 
        final Individual ind, 
        final int subpopulation,
        final int threadnum) {
            if (!ind.evaluated) { // don't bother reevaluating 
                DifferentialData input = (DifferentialData)(this.input);
                
                double fitness = 0;
                int hits = 0;
                a.defaultSettings();
                x = 0;
                y = 0;

                ((GPIndividual)ind).trees[0].child.eval(
                    state,threadnum,input,stack,((GPIndividual)ind),this);

                // No need to evaluate, we already pre-calculated everything!
                int index = a.getIndex();
                fitness = bias[index];

                // display index as hits
                hits = index;
                nullCount[index] = 1;

                // the fitness better be KozaFitness!
                KozaFitness f = ((KozaFitness)ind.fitness);
                f.setStandardizedFitness(state, this.best - fitness);
                f.hits = hits;
                ind.evaluated = true;
            }
        }

        public void describe(
            final EvolutionState state, 
            final Individual ind, 
            final int subpopulation, 
            final int threadnum, 
            final int log) {
                state.output.println("\n\nBest Individual's Map\n=====================", log);
                
                a.defaultSettings();
                x = 0;
                y = 0;

                ((GPIndividual)ind).trees[0].child.eval(
                    state,threadnum,input,stack,((GPIndividual)ind),this);

                state.output.println("Individual's Index: " + a.getIndex(),log);
                state.output.println("Individual's Bias: " + bias[a.getIndex()],log);
                state.output.println("input U: " + a.getBestU(),log);
                state.output.println("input V: " + a.getBestV(),log);
                state.output.println("SBox Positions: " + Arrays.toString(a.getSBoxPosition()),log);
                
                state.output.println(a.getVisualSBoxPosition(),log);

                int count = 0;
                for (int i = 0; i < nullCount.length; i++) 
                    if (nullCount[i] == 1) count++;
                System.out.println("Null count: " + count);
                state.output.println("Null count: " + count, log);
        }
}