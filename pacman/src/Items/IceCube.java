package src.Items;

import ch.aplu.jgamegrid.*;
import java.awt.*;

public class IceCube extends Item{
    public IceCube(GGBackground bg, Location location) {
        super("ice.png");
        draw(bg, location);
    }

    public void draw(GGBackground bg, Location location) {
        bg.setPaintColor(Color.blue);
        GameGrid gg = new GameGrid();
        bg.fillCircle(gg.toPoint(location), 5);
    }
}
