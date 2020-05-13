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
public class CompareServer {

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
        try (var listener = new ServerSocket(59090)) {
            System.out.println("The hand comparer server is running...");
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
                String hitOrStand = in.nextLine();
                boolean running = true;
                Deck deck = new Deck();
                // System.out.println(deck.formatDeckString());
                deck.shuffle();
                // System.out.println(deck.formatDeckString());

                Hand playerHand = new Hand();
                playerHand.addCard(deck.deal());
                playerHand.addCard(deck.deal());

                Hand dealerHand = new Hand();
                dealerHand.addCard(deck.deal());
                dealerHand.addCard(deck.deal());

                while (running == true) {
                    System.out.println(hitOrStand);
                    if (hitOrStand.equals("HIT")) {
                      playerHand.addCard(deck.deal());
                      dealerHand.addCard(deck.deal());
                      out.println("DEALER HAND is: " + showHand(dealerHand) + " and YOUR HAND is: " + showHand(playerHand));
                    }
                    else if (hitOrStand.equals("STAND")) {
                      out.println("FINAL DEALER HAND is: " + showHand(dealerHand) + " and YOUR FINAL HAND is: " + showHand(playerHand));
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
                    hitOrStand = in.nextLine();
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

        public String showHand(Hand hand) {
          String output = new String();
          for (int i = 0; i < hand.cards.size(); i++) {
            if (i != hand.cards.size() - 1) {
              output += (hand.cards.get(i)).rank + " of " + (hand.cards.get(i)).suit + ", ";
            }
            else {
              output += (hand.cards.get(i)).rank + " of " + (hand.cards.get(i)).suit;
            }
          }
          return output;
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
          ArrayList<Card> deck = new ArrayList<Card>();
          String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
          String[] ranks = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
          Deck() {
            this.deck = new ArrayList<Card>();
            for (int i = 0; i < suits.length; i++) {
              for (int k = 0; k < ranks.length; k++) {
                Card cardToAdd = new Card(suits[i], ranks[k]);
                this.deck.add(cardToAdd);
              }
            }
          }
          public String formatDeckString() {
            Card card = new Card("", "");
            String deck_comp = "";
            for (int i = 0; i < this.deck.size(); i++) {
              if (i != this.deck.size() - 1) {
                deck_comp += this.deck.get(i).formatCardString() + ", ";
              }
              else {
                deck_comp += this.deck.get(i).formatCardString();
              }
            }
            return "The deck has: " + deck_comp;
          }

          public void shuffle() {
            Collections.shuffle(this.deck);
          }

          public Card deal() {
            Card single_card = this.deck.get(0);
            this.deck.remove(0);
            return single_card;
          }
        }

        class Hand {
          ArrayList<Card> cards = new ArrayList<Card>();
          int value = 0;
          int aces = 0;
          String[] suits = {"Hearts", "Diamonds", "Spades", "Clubs"};
          String[] ranks = {"Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Jack", "Queen", "King", "Ace"};
          Map<String, Integer> values = new HashMap<String, Integer>() {{
            put("Two", 2);
            put("Three", 3);
            put("Four", 4);
            put("Five", 5);
            put("Six", 6);
            put("Seven", 7);
            put("Eight", 8);
            put("Nine", 9);
            put("Ten", 10);
            put("Jack", 10);
            put("Queen", 10);
            put("King", 10);
            put("Ace", 11);
          }};

          Hand() {
            this.cards = new ArrayList<Card>();
            this.value = 0;
            this.aces = 0;
          }

          public void addCard(Card card) {
            this.cards.add(card);
            this.value += values.get(card.rank);
            if (card.rank == "Ace") {
              this.aces += 1;
            }
          }

          public void adjustForAce() {
            while (this.value > 21 && this.aces > 0) {
              this.value -= 10;
              this.aces -= 1;
            }
          }
        }
    }
}