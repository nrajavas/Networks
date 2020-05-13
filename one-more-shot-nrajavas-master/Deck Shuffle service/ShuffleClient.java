import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ShuffleClient {
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        try (var socket = new Socket(args[0], 59898)) {
            System.out.println("Ready to shuffle? Type YES to begin, and again every time a deck is printed and you wish to shuffle again. When you are done, type NO, and then hit return/enter.");
            var scanner = new Scanner(System.in);
            var in = new Scanner(socket.getInputStream());
            var out = new PrintWriter(socket.getOutputStream(), true);
            while (scanner.hasNextLine()) {
                out.println(scanner.nextLine());
                System.out.println(in.nextLine());
            }
            /*
              boolean playing = true;
              while (playing == true) {
                in = new Scanner(socket.getInputStream());
                while (scanner.hasNextLine()) {
                  out.println(scanner.nextLine());
                  System.out.println(in.nextLine());
                  if (in.equals("YES")) {
                    out.println(scanner.nextLine());
                    System.out.println(in.nextLine());
                  }
                  else {
                    playing = false;
                    return;
                  }
                }
              }
            */
        }
    }
}