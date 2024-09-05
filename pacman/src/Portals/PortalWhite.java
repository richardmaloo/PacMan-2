package src.Portals;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
public class PortalWhite extends Portal {
    public PortalWhite(GGBackground bg, Location location) {
        super("portalWhiteTile.png");
        draw(bg, location);
    }
}
