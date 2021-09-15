/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.tutorial4;
import ec.util.*;
import ec.*;
import ec.gp.*;
import ec.gp.koza.*;
import ec.simple.*;

public class MultiValuedRegression extends GPProblem implements SimpleProblemForm
    {
    private static final long serialVersionUID = 1;

    public double currentX;
    public double currentY;
    public double[] x,y,e;
    
    public void setup(final EvolutionState state,
        final Parameter base)
        {
        super.setup(state, base);
        
        // verify our input is the right class (or subclasses from it)
        if (!(input instanceof DoubleData))
            state.output.fatal("GPData class must subclass from " + DoubleData.class,
                base.push(P_DATA), null);

        x = new double[]{1,1,1,1,1,1,1,1,0,0,0,0,0,0,0,0};
        y = new double[]{0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7};
        e = new double[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
        }
        
    public void evaluate(final EvolutionState state, 
        final Individual ind, 
        final int subpopulation,
        final int threadnum)
        {
        if (!ind.evaluated)  // don't bother reevaluating
            {
            DoubleData input = (DoubleData)(this.input);
        
            int hits = 0;
            double sum = 0.0;
            double expectedResult;
            double result;
            for (int i = 0; i < x.length; i++)
                {
                currentX = x[i];
                currentY = y[i];
                expectedResult = e[i];
                ((GPIndividual)ind).trees[0].child.eval(
                    state,threadnum,input,stack,((GPIndividual)ind),this);

                result = Math.abs(expectedResult - input.x);
                if (result == 0.0) hits++;
                sum += result;                  
                }

            // the fitness better be KozaFitness!
            KozaFitness f = ((KozaFitness)ind.fitness);
            f.setStandardizedFitness(state, sum);
            f.hits = hits;
            ind.evaluated = true;
            }
        }
    }

