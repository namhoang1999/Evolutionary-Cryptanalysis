/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.gaevolve.lcaes;
import ec.*;
import ec.simple.*;
import ec.vector.*;

public class MaxOnes extends Problem implements SimpleProblemForm
    {

    public void evaluate(final EvolutionState state,
        final Individual ind,
        final int subpopulation,
        final int threadnum)
        {
        if (ind.evaluated) return;

        if (!(ind instanceof BitVectorIndividual))
            state.output.fatal("Whoa!  It's not a BitVectorIndividual!!!",null);
        
        BitVectorIndividual ind2 = (BitVectorIndividual)ind;
        
        SBoxPosition a = new SBoxPosition(3);
        
        // Set position
        a.setSBoxPosition(ind2.genome.clone());
        double sum = a.getBestBias();
        
        System.out.println(Helper.hexPrint(a.getIndex(), 4) + " " + sum);    

        if (!(ind2.fitness instanceof SimpleFitness))
            state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
        ((SimpleFitness)ind2.fitness).setFitness(state,
            /// ...the fitness...
            sum,
            ///... is the individual ideal?  Indicate here...
            sum >= 0.0791015625);
        ind2.evaluated = true;
        }
    }
