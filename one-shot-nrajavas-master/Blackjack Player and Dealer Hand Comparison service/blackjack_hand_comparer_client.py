import sys
import socket

if len(sys.argv) != 2:
    print('Pass the server IP as the sole command line argument')
else:
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
        sock.connect((sys.argv[1], 58902))
        print('You currently have two cards. Would you like to hit or stand? Type HIT or STAND')
        playing = True
        while playing:
            raw_line = sys.stdin.readline()
            line = raw_line.strip()
            if not line:
                # End of standard input, exit this entire script
                continue
            if line == "STAND":
              playing = False

            sock.sendall(f'{line}\n'.encode('utf-8'))
            while True:
                data = sock.recv(2048)
                print(data.decode("utf-8"), end='')
                if len(data) < 2048:
                    # No more of this message, go back to waiting for next message
                    break
