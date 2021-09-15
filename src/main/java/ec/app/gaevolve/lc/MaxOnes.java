package ec.app.gaevolve.lc;
import ec.*;
import ec.simple.*;
import ec.vector.*;
import ec.util.*;

public class MaxOnes extends Problem implements SimpleProblemForm {
    SBoxPosition position;
    double[] bias;
    double best;
    int[] nullCount;

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state,base);
        
        int genomeLength = state.parameters.getInt(new Parameter("pop.subpop.0.species.genome-size"),null,1);
        position = new SBoxPosition(genomeLength/4);
         
        switch (genomeLength) {
            case 12: 
                this.bias = Helper.readFile("C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/lc-3.txt",16*16*16); 
                this.best = 0.0791015625; break;
            case 16: 
                this.bias = Helper.readFile("C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/lc-4.txt",16*16*16*16); 
                this.best = 0.019775390625; break;
            case 20: 
                this.bias = Helper.readFile("C:/Users/sdipp/OneDrive/Desktop/Nam/Java/ecj/ecj/results/lc-5.txt",16*16*16*16*16); 
                this.best = 0.007415771484375; break;
            default:
                this.best = 1;
        }

        nullCount = new int[this.bias.length];
    }
    public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum)
        {
        if (ind.evaluated) return;

        if (!(ind instanceof BitVectorIndividual))
            state.output.fatal("Whoa!  It's not a BitVectorIndividual!!!",null);
        
        BitVectorIndividual ind2 = (BitVectorIndividual)ind;

        // Apply the position 
        position.setSBoxPosition(ind2.genome);
        int index = position.getIndex();
        nullCount[index] = 1;

        // No need to evaluate, we already pre-calculated everything!
        double sum = this.bias[index];
        //double sum = position.getBestBias();

        if (!(ind2.fitness instanceof SimpleFitness))
            state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
        ((SimpleFitness)ind2.fitness).setFitness(state,
            /// ...the fitness...
            sum,
            ///... is the individual ideal?  Indicate here...
            sum >= this.best);
        ind2.evaluated = true;
        }

        public void describe(
            final EvolutionState state, 
            final Individual ind, 
            final int subpopulation, 
            final int threadnum, 
            final int log) {
                int count = 0;
                for (int i = 0; i < nullCount.length; i++) 
                    if (nullCount[i] == 1) count++;
                System.out.println("Null count: " + count);
                state.output.println("Null count: " + count, log);
        }
    }
