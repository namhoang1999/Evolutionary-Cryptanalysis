package ec.app.gaestimate.dc;
import ec.EvolutionState;
import ec.Individual;
import ec.Problem;
import ec.app.gaestimate.dc.estimate.SBoxEstimate;
import ec.simple.SimpleFitness;
import ec.simple.SimpleProblemForm;
import ec.util.Parameter;
import ec.vector.BitVectorIndividual;

public class MaxOnes extends Problem implements SimpleProblemForm {
    SBoxEstimate position;
    double[] bias;
    double best;

    public void setup(final EvolutionState state, final Parameter base) {
        super.setup(state,base);
        
        String path = state.parameters.getString(base.push("path"),null);
        this.position = new SBoxEstimate(path);
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
        position.setPosition(ind2.genome);

        // No need to evaluate, we already pre-calculated everything!
        //double sum = this.bias[position.getIndex()];
        double sum = position.estimate();

        if (!(ind2.fitness instanceof SimpleFitness))
            state.output.fatal("Whoa!  It's not a SimpleFitness!!!",null);
        ((SimpleFitness)ind2.fitness).setFitness(state,
            /// ...the fitness...
            sum,
            ///... is the individual ideal?  Indicate here...
            sum >= 1);
        ind2.evaluated = true;
        }
    }
