// PacGrid.java
package src;

import ch.aplu.jgamegrid.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PacManGameGrid
{
  private final static int NUMB_HORI_CELL = 100;
  private int nbHorzCells;
  private int nbVertCells;
  private int[][] mazeArray;

  public PacManGameGrid(File xmlFile)
  {
    /*
    this.nbHorzCells = NUMB_HORI_CELL;
    this.nbVertCells = nbVertCells;
    mazeArray = new int[nbVertCells][nbHorzCells];
    String maze =
            "xxxxxxxxxxxxxxxxxxxx" + // 0
                    "x....x....g...x....x" + // 1
                    "xgxx.x.xxxxxx.x.xx.x" + // 2
                    "x.x.......i.g....x.x" + // 3
                    "x.x.xx.xx  xx.xx.x.x" + // 4
                    "x......x    x......x" + // 5
                    "x.x.xx.xxxxxx.xx.x.x" + // 6
                    "x.x......gi......x.x" + // 7
                    "xixx.x.xxxxxx.x.xx.x" + // 8
                    "x...gx....g...x....x" + // 9
                    "xxxxxxxxxxxxxxxxxxxx";// 10
    */
    System.out.println("0");

    SAXBuilder builder = new SAXBuilder();
    File selectedFile = xmlFile;

    Document document = null;
    System.out.println("1");

    try {
      document = (Document) builder.build(selectedFile);
    } catch (JDOMException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("2");
    Element rootNode = document.getRootElement();

    List sizeList = rootNode.getChildren("size");
    Element sizeElem = (Element) sizeList.get(0);
    int height = Integer.parseInt(sizeElem
            .getChildText("height"));
    int width = Integer
            .parseInt(sizeElem.getChildText("width"));

    this.nbHorzCells = width;
    this.nbVertCells = height;
    mazeArray = new int[height][width];

    List rows = rootNode.getChildren("row");
    String maze = "";
    for (int y = 0; y < rows.size(); y++) {
      Element cellsElem = (Element) rows.get(y);
      List cells = cellsElem.getChildren("cell");

      for (int x = 0; x < cells.size(); x++) {
        Element cell = (Element) cells.get(x);
        String cellValue = cell.getText();

        char tileNr = 'a';
        if (cellValue.equals("PathTile"))
          tileNr = ' ';
        else if (cellValue.equals("WallTile"))
          tileNr = 'x';
        else if (cellValue.equals("PillTile"))
          tileNr = '.';
        else if (cellValue.equals("GoldTile"))
          tileNr = 'g';
        else if (cellValue.equals("IceTile"))
          tileNr = 'i';
        else if (cellValue.equals("PacTile"))
          tileNr = 'p';
        else if (cellValue.equals("TrollTile"))
          tileNr = 'l';
        else if (cellValue.equals("TX5Tile"))
          tileNr = 't';
        else if (cellValue.equals("PortalWhiteTile"))
          tileNr = 'w';
        else if (cellValue.equals("PortalYellowTile"))
          tileNr = 'y';
        else if (cellValue.equals("PortalDarkGoldTile"))
          tileNr = 'o';
        else if (cellValue.equals("PortalDarkGrayTile"))
          tileNr = 'r';
        else
          tileNr = '0';

        maze += tileNr;
      }
    }

    // Copy structure into integer array
    for (int i = 0; i < nbVertCells; i++)
    {
      for (int k = 0; k < nbHorzCells; k++) {
        int value = toInt(maze.charAt(nbHorzCells * i + k));
        mazeArray[i][k] = value;
      }
    }
  }

  public int getCell(Location location)
  {
    return mazeArray[location.y][location.x];
  }
  private int toInt(char c)
  {
    if (c == 'x')
      return 0;
    if (c == '.')
      return 1;
    if (c == ' ')
      return 2;
    if (c == 'g')
      return 3;
    if (c == 'i')
      return 4;
    if (c == 'p')
      return 5;
    if (c == 'l')
      return 6;
    if (c == 't')
      return 7;
    if (c == 'w')
      return 8;
    if (c == 'y')
      return 9;
    if (c == 'o')
      return 10;
    if (c == 'r')
      return 11;

    return -1;
  }
}
