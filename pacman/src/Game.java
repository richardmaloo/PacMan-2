// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.Items.Gold;
import src.Items.IceCube;
import src.Items.Pill;
import src.Monsters.Troll;
import src.Monsters.Tx5;
import src.Portals.PortalDarkGold;
import src.Portals.PortalDarkGray;
import src.Portals.PortalWhite;
import src.Portals.PortalYellow;
import src.mapeditor.editor.Controller;
import src.utility.GameCallback;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends GameGrid
{
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid;

  public PacActor pacActor;

  private ArrayList<Troll> trolls;
  private ArrayList<Tx5> tx5s;
  private ArrayList<IceCube> iceCubes;
  private ArrayList<Gold> goldPieces;
  private ArrayList<Pill> pills;
  private ArrayList<PortalDarkGold> portalDarkGolds;
  private ArrayList<PortalDarkGray> portalDarkGrays;
  private ArrayList<PortalWhite> portalWhites;
  private ArrayList<PortalYellow> portalYellows;
  private ArrayList<Location> pillAndItemLocations;
  private ArrayList<Location> propertyPillLocations;
  private ArrayList<Location> propertyGoldLocations;
  private GameCallback gameCallback;
  private Properties properties;
  private final int seed;

  public Game(GameCallback gameCallback, Properties properties, File path)
  {
    //Setup game
    super(nbHorzCells, nbVertCells, 20, false);
    seed = Integer.parseInt(properties.getProperty("seed"));

    boolean hasPacmanBeenHit = false;
    if(path.isDirectory()){
      File[] files = getMapsXml(path);
      for(File file : files){
        hasPacmanBeenHit = runGame(gameCallback, properties, file);
        if(hasPacmanBeenHit){
          break;
        }
      }
    } else{
      runGame(gameCallback, properties, path);
    }

    if(!hasPacmanBeenHit){
      String title = "YOU WIN";
      setTitle(title);
      gameCallback.endOfGame(title);
      delay(1000);
    }

    if(path.isDirectory()){
      new Controller(null);
    } else{
      new Controller(path);
  }

    this.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    this.getFrame().dispose();
  }

  public boolean teleport(Actor actor){
    int move = -1;
    for (PortalDarkGold portalDarkGold : portalDarkGolds) {
      if (portalDarkGold.getLocation().equals(actor.getLocation())) {
        move = portalDarkGolds.indexOf(portalDarkGold);
        break;
      }
    }

    if(move != -1){
      if(move == 0){
        actor.setLocation(portalDarkGolds.get(1).getLocation());
      } else{
        actor.setLocation(portalDarkGolds.get(0).getLocation());
      }
      return true;
    }

    for (PortalDarkGray portalDarkGray : portalDarkGrays) {
      if (portalDarkGray.getLocation().equals(actor.getLocation())) {
        move = portalDarkGrays.indexOf(portalDarkGray);
        break;
      }
    }

    if(move != -1){
      if(move == 0){
        actor.setLocation(portalDarkGrays.get(1).getLocation());
      } else{
        actor.setLocation(portalDarkGrays.get(0).getLocation());
      }
      return true;
    }

    for (PortalWhite portalWhite : portalWhites) {
      if (portalWhite.getLocation().equals(actor.getLocation())) {
        move = portalWhites.indexOf(portalWhite);
        break;
      }
    }

    if(move != -1){
      if(move == 0){
        actor.setLocation(portalWhites.get(1).getLocation());
      } else{
        actor.setLocation(portalWhites.get(0).getLocation());
      }
      return true;
    }

    for (PortalYellow portalYellow : portalYellows) {
      System.out.println("Yellow: " + portalYellow.getLocation());
      if (portalYellow.getLocation().equals(actor.getLocation())) {
        move = portalYellows.indexOf(portalYellow);
        break;
      }
    }

    if(move != -1){
      if(move == 0){
        actor.setLocation(portalYellows.get(1).getLocation());
      } else{
        actor.setLocation(portalYellows.get(0).getLocation());
      }
      return true;
    }

    return false;
  }

  public boolean runGame(GameCallback gameCallback, Properties properties, File xmlFile){
    this.pacActor = new PacActor(this);
    this.trolls = new ArrayList<>();
    this.tx5s = new ArrayList<>();
    this.iceCubes = new ArrayList<>();
    this.goldPieces = new ArrayList<>();
    this.pills = new ArrayList<>();
    this.portalDarkGolds = new ArrayList<>();
    this.portalDarkGrays = new ArrayList<>();
    this.portalWhites = new ArrayList<>();
    this.portalYellows = new ArrayList<>();
    this.pillAndItemLocations = new ArrayList<>();
    this.propertyPillLocations = new ArrayList<>();
    this.propertyGoldLocations = new ArrayList<>();
    this.grid = new PacManGameGrid(xmlFile);
    this.gameCallback = gameCallback;
    this.properties = properties;

    setSimulationPeriod(100);
    setTitle("[PacMan in the TorusVerse]");
    System.out.println("Go");

    //Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
    loadPillAndItemsLocations();


    GGBackground bg = getBg();
    drawGrid(bg);

    addKeyRepeatListener(pacActor);
    setKeyRepeatPeriod(150);

    //Run the game
    doRun();
    show();
    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    boolean hasPacmanBeenHit;
    boolean hasPacmanEatAllPills;
    setupPillAndItemsLocations();
    int maxPillsAndItems = countPillsAndItems();

    do {
      boolean trollHit = false;
      boolean tx5Hit = false;
      for (Troll troll : trolls) {
        trollHit = troll.getLocation().equals(pacActor.getLocation());

        if (trollHit)
          break;

        if(!troll.isPortalImmuned()){
          if(teleport(troll)){
            troll.setPortalImmuned(true);
          }
        }
      }

      for (Tx5 tx5 : tx5s) {
        tx5Hit = tx5.getLocation().equals(pacActor.getLocation());
        if (tx5Hit)
          break;

        if(!tx5.isPortalImmuned()) {
            if(teleport(tx5)){
                tx5.setPortalImmuned(true);
            }
        }
      }

      hasPacmanBeenHit = trollHit || tx5Hit;
      hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;

      if (!pacActor.isPortalImmuned()) {
        if(teleport(pacActor)){
          pacActor.setPortalImmuned(true);
        }
      }

      delay(10);
    } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
    delay(120);

    Location loc = pacActor.getLocation();

    for(Troll troll : trolls) {
      troll.setStopMoving(true);
    }

    for(Tx5 tx5 : tx5s) {
      tx5.setStopMoving(true);
    }

    pacActor.removeSelf();

    String title;
    if (hasPacmanBeenHit) {
      bg.setPaintColor(Color.red);
      title = "GAME OVER";
      addActor(new Actor("sprites/explosion3.gif"), loc);
    } else {
      bg.setPaintColor(Color.yellow);
      title = "LEVEL COMPLETED";
    }
    setTitle(title);
    gameCallback.endOfGame(title);
    doPause();
    reset();

    return hasPacmanBeenHit;
  }

  public void reset(){
    delay(1000);
    for (Actor actor : this.getActors()) {
      actor.removeSelf();
    }
  }

  public File[] getMapsXml(File folder){
    File[] files = folder.listFiles();
    ArrayList<File> fileNames = new ArrayList<File>();

    // Iterate over the files and print their names
    if (files != null) {
      for (File file : files) {
        if (file.isFile()) {
          String fileName = file.getName();
          boolean startsWithNumber = fileName.matches("^\\d.*");
          if(startsWithNumber)
            fileNames.add(file);
        }
      }
    }
    // Convert ArrayList to array

    return fileNames.toArray(new File[0]);
  }

  public GameCallback getGameCallback() {
    return gameCallback;
  }

  private int countPillsAndItems() {
    int pillsAndItemsCount = 0;
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          pillsAndItemsCount++;
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          pillsAndItemsCount++;
        }
      }
    }
    if (propertyPillLocations.size() != 0) {
      pillsAndItemsCount += propertyPillLocations.size();
    }

    if (propertyGoldLocations.size() != 0) {
      pillsAndItemsCount += propertyGoldLocations.size();
    }

    return pillsAndItemsCount;
  }

  public ArrayList<Location> getPillAndItemLocations() {
    return pillAndItemLocations;
  }

  private void loadPillAndItemsLocations() {
    String pillsLocationString = properties.getProperty("Pills.location");
    if (pillsLocationString != null) {
      String[] singlePillLocationStrings = pillsLocationString.split(";");
      for (String singlePillLocationString: singlePillLocationStrings) {
        String[] locationStrings = singlePillLocationString.split(",");
        propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }

    String goldLocationString = properties.getProperty("Gold.location");
    if (goldLocationString != null) {
      String[] singleGoldLocationStrings = goldLocationString.split(";");
      for (String singleGoldLocationString: singleGoldLocationStrings) {
        String[] locationStrings = singleGoldLocationString.split(",");
        propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }
  }
  private void setupPillAndItemsLocations() {
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && propertyPillLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 3 &&  propertyGoldLocations.size() == 0) {
          pillAndItemLocations.add(location);
        }
        if (a == 4) {
          pillAndItemLocations.add(location);
        }
      }
    }


    if (propertyPillLocations.size() > 0) {
      for (Location location : propertyPillLocations) {
        pillAndItemLocations.add(location);
      }
    }
    if (propertyGoldLocations.size() > 0) {
      for (Location location : propertyGoldLocations) {
        pillAndItemLocations.add(location);
      }
    }
  }

  private void drawGrid(GGBackground bg)
  {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a != 0)
          bg.fillCell(location, Color.lightGray);
        if (a == 1 && propertyPillLocations.size() == 0) { // Pill
          putPill(bg, location);
        } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
          putGold(bg, location);
        } else if (a == 4) {
          putIce(bg, location);
        } else if (a == 5) {
          putPacman(location);
        } else if (a == 6) {
          putTroll(location);
        } else if (a == 7) {
          putTx5(location);
        } else if(a == 8){
          putPortalWhite(bg, location);
        } else if(a == 9){
          putPortalYellow(bg, location);
        } else if(a == 10){
          putPortalDarkGold(bg, location);
        } else if(a == 11){
          putPortalDarkGray(bg, location);
        }
      }
    }

    for (Location location : propertyPillLocations) {
      putPill(bg, location);
    }

    for (Location location : propertyGoldLocations) {
      putGold(bg, location);
    }
  }

  private void putPill(GGBackground bg, Location location){
    Pill pill = new Pill(bg, location);
    this.pills.add(pill);
    addActor(pill, location);
  }

  private void putGold(GGBackground bg, Location location){
    Gold gold = new Gold(bg, location);
    this.goldPieces.add(gold);
    addActor(gold, location);
  }

  private void putIce(GGBackground bg, Location location){
    IceCube ice = new IceCube(bg, location);
    this.iceCubes.add(ice);
    addActor(ice, location);
  }

  private void putPortalDarkGold(GGBackground bg, Location location){
    PortalDarkGold portalDarkGold = new PortalDarkGold(bg, location);
    this.portalDarkGolds.add(portalDarkGold);
    addActor(portalDarkGold, location);
  }

  private void putPortalDarkGray(GGBackground bg, Location location){
    PortalDarkGray portalDarkGray = new PortalDarkGray(bg, location);
    this.portalDarkGrays.add(portalDarkGray);
    addActor(portalDarkGray, location);
  }

  private void putPortalWhite(GGBackground bg, Location location){
    PortalWhite portalWhite = new PortalWhite(bg, location);
    this.portalWhites.add(portalWhite);
    addActor(portalWhite, location);
  }

  private void putPortalYellow(GGBackground bg, Location location){
      PortalYellow portalYellow = new PortalYellow(bg, location);
        this.portalYellows.add(portalYellow);
        addActor(portalYellow, location);
  }
  private void putPacman(Location location){
    pacActor.setSeed(seed);
    pacActor.setSlowDown(3);
    addActor(pacActor, location);
  }

  private void putTroll(Location location){
    Troll troll = new Troll(this);
    troll.setSeed(seed);
    troll.setSlowDown(3);
    trolls.add(troll);
    addActor(troll, location, Location.NORTH);
  }

  private void putTx5(Location location){
    Tx5 tx5 = new Tx5(this);
    tx5.setSeed(seed);
    tx5.setSlowDown(3);
    tx5.stopMoving(5);
    tx5s.add(tx5);
    addActor(tx5, location, Location.NORTH);
  }

  private void putActor(GGBackground bg, Location location){
    IceCube ice = new IceCube(bg, location);
    this.iceCubes.add(ice);
    addActor(ice, location);
  }


  public void removeItem(String type,Location location){
    if(type.equals("gold")){
      for (Actor item : this.goldPieces){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }else if(type.equals("ice")){
      for (Actor item : this.iceCubes){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          for(Troll troll : trolls)
            troll.stopMoving(3);
          for(Tx5 tx5 : tx5s)
            tx5.stopMoving(3);

          item.hide();
        }
      }
    }
  }

  public int getNumHorzCells(){
    return Game.nbHorzCells;
  }
  public int getNumVertCells(){
    return Game.nbVertCells;
  }
}
