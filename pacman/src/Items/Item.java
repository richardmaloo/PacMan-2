package src.Items;

import ch.aplu.jgamegrid.*;
public class Item extends Actor{
    public Item(){super();}
    public Item(String sprite){
        super("sprites/" + sprite);
    }
    public void draw(GGBackground bg, Location location) {
        GameGrid gg = new GameGrid();
        bg.fillCircle(gg.toPoint(location), 5);
    }
}
