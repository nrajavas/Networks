import socketserver
import threading
import random

suits = ('Hearts', 'Diamonds', 'Spades', 'Clubs')
ranks = ('Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine', 'Ten', 'Jack', 'Queen', 'King', 'Ace')
values = {'Two': 2, 'Three': 3, 'Four': 4, 'Five': 5, 'Six': 6, 'Seven': 7, 'Eight': 8, 'Nine': 9, 'Ten': 10, 'Jack': 10, 'Queen': 10, 'King': 10, 'Ace': 11}

class ThreadedTCPServer(socketserver.ThreadingMixIn, socketserver.TCPServer):
    daemon_threads = True
    allow_reuse_address = True

class PlayerHandler(socketserver.StreamRequestHandler):
    def handle(self):
      self.opponent = None
      client = f'{self.client_address} on {threading.currentThread().getName()}'
      print(f'Connected: {client}')
      self.initialize()
      self.wfile.write(f'\nBye!\n'.encode('utf-8'))
      print(f'Closed: {client}')

    def send(self, message):
      self.wfile.write(f'{message}\n'.encode('utf-8'))

    def initialize(self):
      deck = Deck()
      deck.shuffle()
      player_hand = Hand()
      player_hand.add_card(deck.deal())
      player_hand.add_card(deck.deal())

      dealer_hand = Hand()
      dealer_hand.add_card(deck.deal())
      dealer_hand.add_card(deck.deal())

      while True:
            data = self.rfile.readline()
            self.rfile.flush()
            command = data.decode("utf-8").strip()
            if not command:
                return
            if command == 'HIT':
                dealer_hand.add_card(deck.deal())
                player_hand.add_card(deck.deal())
                display_str = (
                  "Dealer Hand\n"
                  f"{show_hand(dealer_hand)}\n"
                  "Your Hand:\n"
                  f"{show_hand(player_hand)}\n"
                  "HIT or STAND again\n"
                )
                self.wfile.write(display_str.encode("utf-8"))
            elif command == 'STAND':
                display_str = (
                  "Dealer Hand\n"
                  f"{show_hand(dealer_hand)}\n"
                  "Your Hand:\n"
                  f"{show_hand(player_hand)}\n"
                )
                self.wfile.write(display_str.encode("utf-8"))
                return
            else:
                self.wfile.write("Bad input\n".encode("utf-8"))
            

def show_hand(hand):
  output = ""
  for card in hand.cards:
    output += str(card.rank) + " of " + str(card.suit) + "\n"
  return output

class Card:
  def __init__(self, suit, rank):
    self.suit = suit
    self.rank = rank

  def __str__(self):
    return self.rank + ' of ' + self.suit

  def __repr__(self):
    return str(self.__dict__)

class Deck:
  def __init__(self):
    self.deck = []
    for suit in suits:
      for rank in ranks:
        self.deck.append(Card(suit,rank))

  def __str__(self):
    deck_comp = ''
    for card in self.deck:
      deck_comp += '\n' + card.__str__()
    return 'The deck has:' + deck_comp

  def shuffle(self):
    random.shuffle(self.deck)

  def deal(self):
    single_card = self.deck.pop()
    return single_card

class Hand:
  def __init__(self):
    self.cards = []
    self.value = 0
    self.aces = 0

  def add_card(self, card):
    self.cards.append(card)
    self.value += values[card.rank]
    if card.rank == 'Ace':
      self.aces += 1

  def adjust_for_ace(self):
    while self.value > 21 and self.aces:
      self.value -= 10
      self.aces -= 1

with ThreadedTCPServer(('', 58902), PlayerHandler) as server:
    print(f'The Blackjack hand comparer server is running...')
    server.serve_forever()