package ec.app.gpevolve.lc.terminals;
import ec.*;
import ec.app.gpevolve.lc.*;
import ec.gp.*;

public class Flip extends GPNode {
    public String toString() {
        return "Flip";
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
        return 0;
    }

    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem) {
        Linear p = (Linear)problem;
        LinearData d = (LinearData)input;

        p.a.flipBit(p.x, p.y);

        d.x = 0;
        d.y = 0;
    }
}