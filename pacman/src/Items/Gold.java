package src.Items;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.GameGrid;
import ch.aplu.jgamegrid.Location;

import java.awt.*;
public class Gold extends Item{
    public Gold(GGBackground bg, Location location) {
        super("gold.png");
        draw(bg, location);
    }
    public void draw(GGBackground bg, Location location) {
        bg.setPaintColor(Color.yellow);
        GameGrid gg = new GameGrid();
        bg.fillCircle(gg.toPoint(location), 5);
    }
}
