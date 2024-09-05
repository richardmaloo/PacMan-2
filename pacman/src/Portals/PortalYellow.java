package src.Portals;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
public class PortalYellow extends Portal {
    public PortalYellow(GGBackground bg, Location location) {
        super("portalYellowTile.png");
        draw(bg, location);
    }

}
