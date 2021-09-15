package ec.app.gpevolve.lc.terminals;
import ec.*;
import ec.app.gpevolve.lc.*;
import ec.gp.*;
import ec.util.*;
import java.io.*;

public class PositionERC extends ERC
    {
    public int x;
    public int y;

    public void resetNode(final EvolutionState state, final int thread)
        {
        x = state.random[thread].nextInt(Linear.maxX);
        y = state.random[thread].nextInt(Linear.maxY);
        }

    public int nodeHashCode()
        {
        // a reasonable hash code
        return this.getClass().hashCode() + x*4 + y;
        }

    public boolean nodeEquals(final GPNode node)
        {
        // check first to see if we're the same kind of ERC -- 
        // won't work for subclasses; in that case you'll need
        // to change this to isAssignableTo(...)
        if (this.getClass() != node.getClass()) return false;
        // now check to see if the ERCs hold the same value
        PositionERC n = (PositionERC)node;
        return (n.x==x && n.y==y);
        }

    public void readNode(final EvolutionState state, final DataInput dataInput) throws IOException
        {
        x = dataInput.readInt();
        y = dataInput.readInt();
        }

    public void writeNode(final EvolutionState state, final DataOutput dataOutput) throws IOException
        {
        dataOutput.writeInt(x);
        dataOutput.writeInt(y);
        }

    public String encode()
        { return Code.encode(x) + Code.encode(y); }

    public boolean decode(DecodeReturn dret)
        {
        // store the position and the string in case they
        // get modified by Code.java
        int pos = dret.pos;
        String data = dret.data;

        // decode
        Code.decode(dret);

        if (dret.type != DecodeReturn.T_INT) // uh oh!
            {
            // restore the position and the string; it was an error
            dret.data = data;
            dret.pos = pos;
            return false;
            }

        // store the data
        x = (int)(dret.l);

        // decode
        Code.decode(dret);

        if (dret.type != DecodeReturn.T_INT) // uh oh!
            {
            // restore the position and the string; it was an error
            dret.data = data;
            dret.pos = pos;
            return false;
            }

        // store the data
        y = (int)(dret.l);

        return true;
        }

    public String toStringForHumans()
        { return "[" + x + "," + y + "]"; }

    public void eval(final EvolutionState state,
        final int thread,
        final GPData input,
        final ADFStack stack,
        final GPIndividual individual,
        final Problem problem)
        {
        LinearData rd = ((LinearData)(input));
        rd.x = x;
        rd.y = y;
        }
    }
