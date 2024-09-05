package src.Monsters;

import ch.aplu.jgamegrid.*;
import src.Game;

import java.awt.Color;
import java.util.*;

public class Monster extends Actor{
  protected Game game;
  protected String sprite;
  protected ArrayList<Location> visitedList = new ArrayList<>();
  protected final int listLength = 10;
  private boolean stopMoving = false;
  protected int seed = 0;
  protected Random randomiser = new Random(0);
  protected boolean portalImmuned = false;

  public Monster(Game game, String sprite)
  {
    super("sprites/" + sprite);
    this.game = game;
    this.sprite = sprite;
  }

  public void stopMoving(int seconds) {
    this.stopMoving = true;
    Timer timer = new Timer(); // Instantiate Timer Object
    int SECOND_TO_MILLISECONDS = 1000;
    final Monster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.stopMoving = false;
      }
    }, seconds * SECOND_TO_MILLISECONDS);
  }

  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  public void setStopMoving(boolean stopMoving) {
    this.stopMoving = stopMoving;
  }

  public void act()
  {
    if (stopMoving) {
      return;
    }

    walkApproach();
    if (getDirection() > 150 && getDirection() < 210)
      setHorzMirror(false);
    else
      setHorzMirror(true);
  }

  public void walkApproach()
  {
    Location pacLocation = game.pacActor.getLocation();
    double oldDirection = getDirection();

    Location.CompassDirection compassDir =
            getLocation().get4CompassDirectionTo(pacLocation);
    setDirection(compassDir);

    // Random walk
    int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
    setDirection(oldDirection);
    turn(sign * 90);  // Try to turn left/right
    Location next = getNextMoveLocation();
    if (canMove(next))
    {
      setLocation(next);
    }
    else
    {
      setDirection(oldDirection);
      next = getNextMoveLocation();
      if (canMove(next)) // Try to move forward
      {
        setLocation(next);
      }
      else
      {
        setDirection(oldDirection);
        turn(-sign * 90);  // Try to turn right/left
        next = getNextMoveLocation();
        if (canMove(next))
        {
          setLocation(next);
        }
        else
        {

          setDirection(oldDirection);
          turn(180);  // Turn backward
          next = getNextMoveLocation();
          setLocation(next);
        }
      }
    }

    game.getGameCallback().monsterLocationChanged(this);
    addVisitedList(next);
  }

  public void addVisitedList(Location location)
  {
    visitedList.add(location);
    if (visitedList.size() == listLength)
      visitedList.remove(0);
  }

  public boolean isVisited(Location location)
  {
    for (Location loc : visitedList)
      if (loc.equals(location))
        return true;
    return false;
  }

  public boolean canMove(Location location)
  {
    Color c = getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
            || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0) {
      return false;
    }
    else {
      portalImmuned = false;
      return true;
    }
  }

  public String getType() {
    return this.getClass().getSimpleName();
  }

  public boolean isPortalImmuned() {
    return portalImmuned;
  }

  public void setPortalImmuned(boolean portalImmuned) {
    this.portalImmuned = portalImmuned;
  }
}


