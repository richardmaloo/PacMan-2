package src.Portals;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import java.awt.*;

public class PortalDarkGold extends Portal{
    public PortalDarkGold(GGBackground bg, Location location) {
        super("portalDarkGoldTile.png");
        draw(bg, location);
    }
}
