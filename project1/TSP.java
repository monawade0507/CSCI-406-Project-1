import java.io.*;
import java.lang.*;
import java.util.Vector;
import java.util.Scanner;
import java.util.Stack;
import java.lang.Math;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
  Sources:
  GeeksforGeeks: geeksforgeeks.org/write-a-c-program-to-ptint-all-permutations-of-a-given-string/
*/

class Points {
  public double x;
  public double y;
}

public class TSP {

  // storage for corridate points from file
  public static Vector<Points> nodes;
  // file name
  public static String file;
  // amount of points aka nodes
  public static int numOfNodes;
  // Found permutations
  public static Vector<String> permutations;
  // Timestamp
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

  public static void nearestNeighbor () {
    // time stamp
    Timestamp beginTimestamp = new Timestamp(System.currentTimeMillis());
    System.out.println("Start time: " + beginTimestamp);

    // Nearest Neighbor Heuristic
    /*
      Starting from some point P0, we walk first to its nearest neighbor P1.
      From P1, we walk to its nearest unvisted neightbor, thus excluding only
      P0 as a candidate. Repeat this process until we run out of unvisted
      points, after which we return to P0 to close off the tour.
    */
    double tour = 0.0;
    Vector<Points> path = new Vector<Points>();
    // starting point (aka sp) is the first point in the input list
    Points sp = new Points();
    sp = nodes.elementAt(0);
    path.addElement(sp);
    // Beginning point
    Points begin = new Points();
    begin = sp;
    // remove starting point from unvisited Points
    nodes.remove(0);
    int i = 0;
    while (!nodes.isEmpty()) {
      i ++;
      double minDist = 500.0;
      int minElement = -1;

      // last point Visited
      // return to begin
      if (nodes.size() == 1) {
        double tempDist = Math.sqrt( Math.pow( (nodes.elementAt(0).x - sp.x), 2) + Math.pow( (nodes.elementAt(0).y - sp.y), 2) );
        tour += tempDist;
        // returning to beginning point
        tempDist = Math.sqrt( Math.pow( (nodes.elementAt(0).x - begin.x), 2) + Math.pow( (nodes.elementAt(0).y - begin.y), 2) );
        tour += tempDist;
        path.addElement(nodes.elementAt(0));
        path.addElement(begin);
        break;
      }


      // select the nearest node
      for (int j = 0; j < nodes.size(); j++) {
        // find distance -> sqrt( pwr((x2 - x1), 2) + pwr((y2 - y1), 2) )
        double tempDist = Math.sqrt( Math.pow( (nodes.elementAt(j).x - sp.x), 2) + Math.pow( (nodes.elementAt(j).y - sp.y), 2) );
        if (tempDist < minDist) {
          minDist = tempDist;
          minElement = j;
        }
      }

      // visit nearest node
      if (minElement != -1) {
        sp = nodes.elementAt(minElement);
        tour += minDist;
        nodes.remove(minElement);
        path.addElement(sp);
      }
    } // end of while loop

    // end Timestamp
    Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
    System.out.println("End time: " + endTimestamp);
    long diff = endTimestamp.getTime() - beginTimestamp.getTime();
    System.out.println("Time diff: " + diff);

    // Completed tour
    System.out.println("Total tour length: " + tour);
    System.out.println("Tour Path ..... ");
    // list out tour path
    for (int k = 0; k < path.size(); k++) {
      System.out.println(path.elementAt(k).x + "  " + path.elementAt(k).y);
    }

  } // end of nearestNeighbor function

  public static void exhaustive () {
    // exhaustive algorithm
    /*
      Enumerating all possible orderings of the set of points, and then
      selecting the ordering tht minimizes that total length.
    */
    // begin Timestamp
    Timestamp beginTimestamp = new Timestamp(System.currentTimeMillis());
    System.out.println("End time: " + beginTimestamp);

    double minDist = 500.0;
    permutations = new Vector<String>();
    double tour = 0.0;
    String path = "";

    // create string from n input coordinate points
    String nPermute = "";
    for(int i = 0; i < numOfNodes; i++) {
      nPermute += Integer.toString(i);
    }

    // Determine all of the permutations and populate permutations Vector
    permute(nPermute, 0, nPermute.length() - 1);
    System.out.println("Number of permutations found: " +  permutations.size());

    // Find the minimum distance
    for (int i = 0; i < permutations.size(); i++) {
      double tempDist = cost(permutations.elementAt(i));
      if (tempDist < minDist) {
        minDist = tempDist;
        tour = tempDist;
        path = permutations.elementAt(i);
      }
    }

    // Timestamp end
    Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
    System.out.println("End time: " + endTimestamp);
    long diff = endTimestamp.getTime() - beginTimestamp.getTime();
    System.out.println("Time diff: " + diff);

    // Completed tour
    System.out.println("Total tour length: " + tour);
    System.out.println("Tour Path ..... ");
    // list out tour path
    for (int k = 0; k < path.length(); k++) {
      System.out.println(nodes.elementAt(Integer.parseInt(Character.toString(path.charAt(k)))).x + "  " + nodes.elementAt(Integer.parseInt(Character.toString(path.charAt(k)))).y);
    }
    System.out.println(nodes.elementAt(Integer.parseInt(Character.toString(path.charAt(0)))).x + "  " + nodes.elementAt(Integer.parseInt(Character.toString(path.charAt(0)))).y);


  } // end of exhaustive function

  // permute function
  public static void permute(String str, int l, int r) {
    /*
      str = string to calculate permutations for
      l = starting index
      r = end index
    */

    if (l == r) {
      permutations.addElement(str);
    }
    else {
      for (int i = l; i <= r; i++) {
        str = swap(str, l, i);
        permute(str, l+1, r);
        str = swap(str, l, i);
      }
    }
  } // end of permute function

  // swap function - need for permute function
  public static String swap (String a, int i, int j) {
    /*
      a = string value
      i = position 1;
      j = position 2;
    */
    char temp;
    char[] charArray = a.toCharArray();
    temp = charArray[i];
    charArray[i] = charArray[j];
    charArray[j] = temp;
    return String.valueOf(charArray);
  } // end of swap function

  public static double cost(String str) {
    // Determine the cost of the str
    double tempFinal = 0.0;
    for (int i = 0; i < str.length(); i++) {
      // at the last element in the string; need to return to the beginning
      if (i + 1 >= str.length()) {
        tempFinal += Math.sqrt( Math.pow( (nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(0)))).x -
                                           nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(i)))).x), 2) +
                                Math.pow( (nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(0)))).y -
                                           nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(i)))).y), 2) );
      }
      else {
        tempFinal += Math.sqrt( Math.pow( (nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(i + 1)))).x -
                                           nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(i)))).x), 2) +
                                Math.pow( (nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(i + 1)))).y -
                                           nodes.elementAt(Integer.parseInt(Character.toString(str.charAt(i)))).y), 2) );
      }
    }
    return tempFinal;
  }


  // read contents of the input file and store informcation in points vector
  public static void readFile () {
    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
      int count = 0;
      for (String line; (line = br.readLine()) != null;) {
        // process lines read in file
        if (count == 0) {
          // only the first line should have the amount of points (aka nodes)
          // should be a single value
          try {
            numOfNodes = Integer.parseInt(line);
            nodes = new Vector<Points>(numOfNodes);
          } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
          }
        }
        else {
          // remaining lines should be a pair of x and y values
          String[] splitted = line.split(" ");
          Points pts = new Points();
          try {
            pts.x = Double.parseDouble(splitted[0]);    //Integer.parseInt(splitted[0]);
            pts.y = Double.parseDouble(splitted[1]);    //Integer.parseInt(splitted[1]);
            nodes.addElement(pts);
          } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
          }
        }
        count ++;
      }
      // line is not visible here
      br.close();
    } catch (FileNotFoundException f) {
      System.out.println(f.getMessage());
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

  }

  public static void main (String [] args) {
    System.out.println("Enter file for TSP: ");
    Scanner scan = new Scanner(System.in);
    file = scan.next();
    readFile();
    System.out.println("Enter 'nn' for nearest neighbor or 'e' for exhaustive: ");
    String choice = scan.next();
    if (choice.equals("nn")) {
      nearestNeighbor();
    }
    else {
      exhaustive();
    }
  }
}
