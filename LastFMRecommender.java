/* Name: Scott Ti
 * Assignment: #4, A Social-Network Based Recommendation System for last.fm
 * Date: 6/16/2021
 */

import java.io.*;
import java.util.*;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Graph;

public class LastFMRecommender {
  // Prints the friends of the given user
  void listFriends(int user) throws IOException {
    Scanner sc = new Scanner(new File("user_friends.dat"));
    ArrayList<String> friends = new ArrayList<>();
    String[] data;
    sc.nextLine();
    while (sc.hasNextLine()) {
      data = sc.nextLine().split("\t");
      if (Integer.parseInt(data[0]) == user) {
        friends.add(data[1]);
      }
    }
    sc.close();
    System.out.print(friends);
  }

  // Prints the mutual friends between user1 and user2
  void commonFriends(int user1, int user2) throws IOException {
    Scanner sc = new Scanner(new File("user_friends.dat"));
    sc.nextLine();
    String line = "";
    String[] data;
    Graph friends = new Graph(2101);
    // saves the users and their friends into an undirected graph
    while (sc.hasNextLine()) {
      line = sc.nextLine();
      data = line.split("\t");
      friends.addEdge(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }
    sc.close();
    // Use a set to remove possible duplicates and later use retainAll for the
    // intersection
    Set<Integer> user1Set = new HashSet<>();
    Set<Integer> user2Set = new HashSet<>();
    // Users that are adjacent to user1 and user2 are mutual friends
    for (int i : friends.adj(user1)) {
      user1Set.add(i);
    }
    for (int i : friends.adj(user2)) {
      user2Set.add(i);
    }
    user1Set.retainAll(user2Set);
    System.out.print(user1Set);
  }

  // Prints the list of artists listened by both users
  void listArtists(int user1, int user2) throws IOException {
    Scanner sc = new Scanner(new File("user_artists.dat"));
    sc.nextLine();
    String line = "";
    String[] data;
    // Use a directed graph because some users may also be artists
    Digraph artists = new Digraph(92834);
    // Adds all connections between users and the artists they listen to
    // to the digraph
    while (sc.hasNextLine()) {
      line = sc.nextLine();
      data = line.split("\t");
      artists.addEdge(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }
    sc.close();

    Set<Integer> user1Set = new HashSet<>();
    Set<Integer> user2Set = new HashSet<>();
    for (int i : artists.adj(user1)) {
      user1Set.add(i);
    }
    for (int i : artists.adj(user2)) {
      user2Set.add(i);
    }
    // Now just get the intersection
    user1Set.retainAll(user2Set);
    System.out.print(user1Set);
  }

  // Prints the list of top 10 most popular artists listened by all users
  void listTop10() throws IOException {
    Scanner sc = new Scanner(new File("user_artists.dat"));
    sc.nextLine();
    String line = "";
    Map<Integer, Integer> chart = new HashMap<>();
    String[] data;
    Integer[] intData = new Integer[2];

    Map<Integer, String> artistData = new HashMap<>();
    // Finds the total weight for each artist
    // and puts it in a HashMap
    while (sc.hasNextLine()) {
      line = sc.nextLine();
      data = line.split("\t");

      intData[0] = Integer.parseInt(data[1]);
      intData[1] = Integer.parseInt(data[2]);

      if (chart.containsKey(intData[0])) {
        chart.put(intData[0], chart.get(intData[0]) + intData[1]);
      } else {
        chart.put(intData[0], intData[1]);
      }
    }
    sc.close();
    int maxKey = 0;
    int max = 0;
    Scanner sc1 = new Scanner(new File("artists.dat"));

    sc1.nextLine();

    while (sc1.hasNextLine()) {
      data = sc1.nextLine().split("\t");
      artistData.put(Integer.parseInt(data[0]), data[1]);

    }

    // Finds the max weight and then removes key/value pair from the list
    // O(10n)
    for (int i = 0; i < 10; i++) {
      maxKey = 0;
      max = 0;
      for (int k : chart.keySet()) {
        if (chart.get(k) > max) {
          max = chart.get(k);
          maxKey = k;
        }
      }
      System.out.printf("Artist: %s, Weight: %s\n", artistData.get(maxKey), chart.get(maxKey));
      chart.remove(maxKey);
    }
  }

  // recommends 10 most popular artists listened by the given user and his/her
  // friends
  void recommend10(int user) throws IOException {
    // This finds all the friends of the user
    // and puts it in an undirected graph
    Scanner sc = new Scanner(new File("user_friends.dat"));
    sc.nextLine();
    String line = "";
    String[] data;
    Graph friends = new Graph(2101);
    while (sc.hasNextLine()) {
      line = sc.nextLine();
      data = line.split("\t");
      friends.addEdge(Integer.parseInt(data[0]), Integer.parseInt(data[1]));
    }
    sc.close();

    Set<Integer> userSet = new HashSet<>();
    for (int i : friends.adj(user)) {
      userSet.add(i);
    }

    // This counts the weight of each artist and
    // totals the weights from the user and their friends
    Scanner sc1 = new Scanner(new File("user_artists.dat"));
    sc1.nextLine();
    Map<Integer, Integer> artistChart = new HashMap<>();
    Integer[] intData = new Integer[2];
    while (sc1.hasNextLine()) {
      line = sc1.nextLine();
      data = line.split("\t");
      intData[0] = Integer.parseInt(data[1]);
      intData[1] = Integer.parseInt(data[2]);
      // This adds the user's weights to the chart
      if (Integer.parseInt(data[0]) == user) {
        if (artistChart.containsKey(intData[0])) {
          artistChart.put(intData[0], artistChart.get(intData[0]) + intData[1]);
        } else {
          artistChart.put(intData[0], intData[1]);
        }
      }
      // This adds the user's friends' weights to the chart
      for (int friend : userSet) {
        if (Integer.parseInt(data[0]) == friend) {
          if (artistChart.containsKey(intData[0])) {
            artistChart.put(intData[0], artistChart.get(intData[0]) + intData[1]);
          } else {
            artistChart.put(intData[0], intData[1]);
          }
        }
      }
    }
    sc1.close();

    // This retrieves the artist's name with their id
    // and puts it in the HashMap artistData
    Map<Integer, String> artistData = new HashMap<>();
    Scanner sc2 = new Scanner(new File("artists.dat"));
    sc2.nextLine();
    while (sc2.hasNextLine()) {
      data = sc2.nextLine().split("\t");
      artistData.put(Integer.parseInt(data[0]), data[1]);

    }
    sc2.close();

    int maxKey = 0;
    int max = 0;
    // Finds the max weight and then removes key/value pair from the list
    for (int i = 0; i < 10; i++) {
      maxKey = 0;
      max = 0;
      for (int k : artistChart.keySet()) {
        if (artistChart.get(k) > max) {
          max = artistChart.get(k);
          maxKey = k;
        }
      }
      System.out.printf("Artist: %s, Weight: %s\n", artistData.get(maxKey), artistChart.get(maxKey));
      artistChart.remove(maxKey);
    }

  }
}
