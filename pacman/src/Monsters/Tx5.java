package src.Monsters;

import ch.aplu.jgamegrid.Location;
import src.Game;

public class Tx5 extends Monster {

    public Tx5(Game game) {
        super(game, "m_tx5.gif");
    }

    @Override
    public void walkApproach() {
        Location pacLocation = game.pacActor.getLocation();

        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        setDirection(compassDir);
        if (!isVisited(next) && canMove(next)) {
            setLocation(next);
            game.getGameCallback().monsterLocationChanged(this);
            addVisitedList(next);
        } else{
            super.walkApproach();
        }
    }
    public String getType() {
        return "Tx5";
    }
}
