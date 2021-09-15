/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.gpevolve.dc;
import ec.gp.*;

public class DifferentialData extends GPData
    {
    public int x, y;    // return value

    public void copyTo(final GPData gpd) {   
        DifferentialData d = (DifferentialData)gpd;
        d.x = x;
        d.y = y;
    }
}


