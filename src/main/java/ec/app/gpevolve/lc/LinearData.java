/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package ec.app.gpevolve.lc;
import ec.gp.*;

public class LinearData extends GPData
    {
    public int x, y;    // return value

    public void copyTo(final GPData gpd) {   
        LinearData d = (LinearData)gpd;
        d.x = x;
        d.y = y;
    }
}


