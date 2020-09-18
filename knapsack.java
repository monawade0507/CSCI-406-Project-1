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

  Avg Description:
  A thief robbing a store finds n items. The ith item is
  worth v dollars and weighs w pounds, where v and w are positive integers.
  The thief wants to take as valuable a load as possible, but can carry at most W pounds in his/her knapsack, for some
  integer W.

*/

class Item {
  public double v;  // v = dollars
  public double w;  // w = weight
}


public class knapsack {

  public static int maxWeight = 0;
  public static int numOfItems = 0;
  public static Vector<Item> items;
  public static String file;
  public static Vector<String> permutations;
  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");

  // read contents of the input file and store information in points vector **************************************
  public static void readFile () {
    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
      int count = 0;
      for (String line; (line = br.readLine()) != null;) {
        // process lines read in file
        if (count == 0) {
          // only the first line should have the max weight amount
          // should be a single value
          try {
            maxWeight = Integer.parseInt(line);
            System.out.println("Max Weight found to be: " + maxWeight);
          } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
          }
          count++;
          continue;
        }
        else if (count == 1) {
           // on the second line should be the amount of items
           // should be a single value
           try {
             numOfItems = Integer.parseInt(line);
             System.out.println("Number of items is: " + numOfItems);
             items = new Vector<Item>(numOfItems);
           } catch (NumberFormatException e) {
             System.out.println(e.getMessage());
           }
           count++;
           continue;
        }
        else {
          // remaining lines should be a pair of x and y values
          String[] splitted = line.split(" ");
          Item itm = new Item();
          try {
            itm.v = Double.parseDouble(splitted[0]);    // Integer.parseInt(splitted[0]);
            itm.w = Double.parseDouble(splitted[1]);    // Integer.parseInt(splitted[1]);
            items.addElement(itm);
            System.out.println("Item added to Vector: Dollar = " + itm.v + " Weight = " + itm.w);
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

  }  // end of readFile function ************************************************************************

  // permute function  ***********************************************************************************
  public static void permute(String str, int l, int r) {
    /*
      str = string to calculate permutations for
      l = starting index
      r = end index
    */

    if (l == r) {
      permutations.addElement(str);
      //Timestamp beginTimestamp = new Timestamp(System.currentTimeMillis());
      //System.out.println("Finished a permutation @ " + beginTimestamp);
    }
    else {
      for (int i = l; i <= r; i++) {
        str = swap(str, l, i);
        permute(str, l + 1, r);
        str = swap(str, l, i);
      }
    }
  } // end of permute function ****************************************************************************

  // swap function - need for permute function ***********************************************************
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
  } // end of swap function  ******************************************************************************

  public static void exhaustive () {  // exhaustive function **********************************************
    // begin Timestamp
    Timestamp beginTimestamp = new Timestamp(System.currentTimeMillis());
    System.out.println("Begin time: " + beginTimestamp);

    permutations = new Vector<String>();
    // create string from n input coordinate points
    String nPermute = "";
    for(int i = 0; i < numOfItems; i++) {
      nPermute += Integer.toString(i);
    }

    // Determine all of the permutations and populate permutations Vector
    permute(nPermute, 0, nPermute.length() - 1);
    System.out.println("Number of permutations found: " +  permutations.size());
    Timestamp endTimestamp = new Timestamp(System.currentTimeMillis());
    System.out.println("End time: " + endTimestamp);
    long diff = endTimestamp.getTime() - beginTimestamp.getTime();
    System.out.println("Time diff: " + diff);
  }  // end of exhaustive function ************************************************************************


  public static void main (String [] args) {  // main function ********************************************
    System.out.println("Enter file for KnapSack: ");
    Scanner scan = new Scanner(System.in);
    file = scan.next();
    readFile();

    exhaustive();
    /*
    System.out.println("Enter 'nn' for nearest neighbor or 'e' for exhaustive: ");
    String choice = scan.next();
    if (choice.equals("nn")) {
      nearestNeighbor();
    }
    else {
      exhaustive();
    }
    */
  } // end of the main function *************************************************************************
} // end of the Knapsack Class **************************************************************************
