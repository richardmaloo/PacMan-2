package src.Monsters;

import src.Game;

public class Troll extends Monster {

    public Troll(Game game) {
        super(game, "m_troll.gif");
    }
    public String getType() {
        return "Troll";
    }
}
