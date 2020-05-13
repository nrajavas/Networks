import java.io.IOException;
import java.io.PrintWriter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.Random;
import java.lang.String;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;

/**
 * A server program which accepts requests from clients to Shuffle strings.
 * When a client connects, a new thread is started to handle it. Receiving
 * client data, capitalizing it, and sending the response back is all done on
 * the thread, allowing much greater throughput because more clients can be
 * handled concurrently.
 */
public class ShuffleServer {

    /**
     * Runs the server. When a client connects, the server spawns a new thread to do
     * the servicing and immediately returns to listening. The application limits
     * the number of threads via a thread pool (otherwise millions of clients could
     * cause the server to run out of resources by allocating too many threads).
     */
    String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
    String[] ranks = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
    // HashMap<String, Integer> values = new HashMap<String, Integer>();
    // values.put("Two", 2);
    // values.put("Three", 3);
    // values.put("Four", 4);
    // values.put("Five", 5);
    // values.put("Six", 6);
    // values.put("Seven", 7);
    // values.put("Eight", 8);
    // values.put("Nine", 9);
    // values.put("Ten", 10);
    // values.put("Jack", 10);
    // values.put("Queen", 10);
    // values.put("King", 10);
    // values.put("Ace", 11);

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898)) {
            System.out.println("The deck shuffle server is running...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Shuffler(listener.accept()));
            }
        }
    }

    private static class Shuffler implements Runnable {
        private Socket socket;
        int[] deck = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
        String[] ranks = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};

        Shuffler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {
                var in = new Scanner(socket.getInputStream());
                var out = new PrintWriter(socket.getOutputStream(), true);
                String yesOrNo = in.nextLine();
                boolean running = true;
                // String shuffledDeck = Arrays.toString(ShuffleDeck(deck));
                Deck deck = new Deck();
                System.out.println(deck.formatDeckString());
                deck.shuffle();
                System.out.println("Shuffled deck" + deck.formatDeckString());
                // System.out.println(deck);
                //System.out.println(yesOrNo);
                while (running == true) {
                  // if (! in.hasNextLine()) {
                  //   return;
                  // }
                    System.out.println(yesOrNo);
                    if (yesOrNo.equals("YES")) {
                      deck.shuffle();
                      // out.println(Arrays.toString(ShuffleDeck(deck)));
                      out.println(deck.formatDeckString());
                    }
                    else if (yesOrNo.equals("NO")) {
                      out.println("Bye!");
                      running = false;
                      in.nextLine();
                      in.close();
                      return;
                    }
                    else {
                      out.println("Bad input");
                      running = false;
                      in.nextLine();
                      in.close();
                      return;
                    }
                    yesOrNo = in.nextLine();
                }
            } catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                }
                System.out.println("Closed: " + socket);
            }
        }

        class Card {
            String suit;
            String rank;

            Card (String suit, String rank) {
              this.suit = suit;
              this.rank = rank;
            }
            public String formatCardString() {
              return this.rank + " of " + this.suit;
            }
        }

        class Deck {
          ArrayList<String> deck = new ArrayList<String>();
          String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
          String[] ranks = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
          Deck() {
            this.deck = new ArrayList<String>();
            for (int i = 0; i < suits.length; i++) {
              for (int k = 0; k < ranks.length; k++) {
                String cardToAdd = new Card(suits[i], ranks[k]).formatCardString();
                this.deck.add(cardToAdd);
              }
            }
          }
          public String formatDeckString() {
            Card card = new Card("", "");
            String deck_comp = "";
            for (int i = 0; i < this.deck.size(); i++) {
              if (i != this.deck.size() - 1) {
                deck_comp += this.deck.get(i) + ", ";
              }
              else {
                deck_comp += this.deck.get(i);
              }
            }
            return "The deck has: " + deck_comp;
          }

          public void shuffle() {
            Collections.shuffle(this.deck);
          }
        }

        // public static int[] ShuffleDeck(int[] deck) {
        //     Random rgen = new Random();
        //     for (int i = 0; i < deck.length; i++) {
        //       int randomPosition = rgen.nextInt(deck.length);
        //       int temp = deck[i];
        //       deck[i] = deck[randomPosition];
        //       deck[randomPosition] = temp;
        //     }
        //     return deck;
        // }
    }
}