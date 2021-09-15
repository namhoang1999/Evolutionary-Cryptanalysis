package ec.app.gpevolve.dc.internals;
import ec.*;
import ec.app.gpevolve.dc.*;
import ec.gp.*;

public class NextRound extends GPNode {
    public String toString() {
        return "NextR";
    }

    /*
      public void checkConstraints(final EvolutionState state,
      final int tree,
      final GPIndividual typicalIndividual,
      final Parameter individualBase)
      {
      super.checkConstraints(state,tree,typicalIndividual,individualBase);
      if (children.length!=2)
      state.output.error("Incorrect number of children for node " + 
      toStringForError() + " at " +
      individualBase);
      }
    */
    public int expectedChildren() {
        return 1;
    }

    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem) {
        Differential p = (Differential)problem;
        DifferentialData d = (DifferentialData) input;

        p.y = (p.y + 1) % Differential.maxY;
    }
}