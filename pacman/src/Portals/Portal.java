package src.Portals;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

public class Portal extends Actor {

    public Portal(String sprite){
        super("sprites/" + sprite);
    }
    public void draw(GGBackground bg, Location location) {
        GameGrid gg = new GameGrid();
        bg.fillCircle(gg.toPoint(location), 5);
    }
}
