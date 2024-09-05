package src.Portals;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import java.awt.*;

public class PortalDarkGray extends Portal {
    public PortalDarkGray(GGBackground bg, Location location) {
        super("portalDarkGrayTile.png");
        draw(bg, location);
    }
}
