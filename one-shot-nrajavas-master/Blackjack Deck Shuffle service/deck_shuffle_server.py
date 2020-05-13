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
      try:
        self.initialize()
      except Exception as e:
        print(e)
      finally:
        try:
          self.wfile.write(f'Bye!\n'.encode('utf-8'))
        except:
          pass
      print(f'Closed: {client}')

    def send(self, message):
      self.wfile.write(f'{message}\n'.encode('utf-8'))

    def initialize(self):
      deck = Deck()
      deck.shuffle()

      while True:
          data = self.rfile.readline()
          self.rfile.flush()
          command = data.decode("utf-8").strip()
          if not command:
              return
          if command == 'YES':
              deck.shuffle()
              display_str = (
                  f"{deck}\n"
                  "Shuffle again?\n"
                )
              self.wfile.write(display_str.encode("utf-8"))
          elif command == 'NO':
              self.wfile.write((f'{deck}'.encode('utf-8')).encode("utf-8"))
              return
          else:
              self.wfile.write("Bad input\n".encode("utf-8"))

class Card:
  def __init__(self, suit, rank):
    self.suit = suit
    self.rank = rank

  def __str__(self):
    return self.rank + ' of ' + self.suit

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

with ThreadedTCPServer(('', 58901), PlayerHandler) as server:
    print(f'The Blackjack deck shuffler server is running...')
    server.serve_forever()
