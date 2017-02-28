/**
 * Created by christianbutron on 2/27/17.
 */
import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static final int portNumber = 1337;

    private static ArrayList<clientRunnable> clientThread;


    public static void main(String[] args) throws Exception {
        serverSocket = new ServerSocket(portNumber);
        clientThread = new ArrayList<clientRunnable>();

        while(true) {
            clientSocket = serverSocket.accept();
            clientRunnable curr = new clientRunnable(clientSocket);
            clientThread.add(curr);
            curr.start();
        }
    }

    //public static class clientRunnable implements Runnable {
    public static class clientRunnable extends Thread {
        private Socket clientSocket;
        private BufferedReader bin;
        private PrintWriter pout;
        private String name;
        private Client client;

        public clientRunnable(Socket clientSocket) throws Exception {
            this.clientSocket = clientSocket;

            InputStream in = clientSocket.getInputStream();
            bin = new BufferedReader(new InputStreamReader(in));
            OutputStream out = clientSocket.getOutputStream();
            pout = new PrintWriter(out, true);
        }

        public void run() {
            try {
                while (true) {
                    pout.println("Enter your name.");
                    name = bin.readLine();
                    break;
                }

                pout.println("Welcome to the chatroom, " + name + ".");
                pout.println("To leave enter /quit in a new line.");
                synchronized (this) {
                    for (int i = 0; i < clientThread.size(); i++) {
                        if (clientThread.get(i) != this) {
                            clientThread.get(i).pout.println("*** " + name + " entered the chat room! ***");
                        }
                    }
                }

                while (true) {
                    String line = bin.readLine();
                    if (line.startsWith("/quit")) {
                        break;
                    }
                    synchronized (this) {
                        for (int i = 0; i < clientThread.size(); i++) {
                            if (clientThread.get(i) != this) {
                                clientThread.get(i).pout.println("<" + name + "> " + line);
                            }
                        }
                    }
                    pout.println("<" + name + "> " + line);
                }

                synchronized (this) {
                    for (int i = 0; i < clientThread.size(); i++) {
                        if (clientThread.get(i) != this) {
                            clientThread.get(i).pout.println("*** The user " + name + " is leaving the chat room. ***");
                        }
                    }
                }
                pout.println("*** Bye " + name + " ***");
                pout.println("/quit");

                synchronized (this) {
                    for (int i = 0; i < clientThread.size(); i++) {
                        if (clientThread.get(i) == this) {
                            clientThread.remove(i);
                        }
                    }
                }

                bin.close();
                pout.close();
                clientSocket.close();

            } catch(Exception ex) {

            }
        }
    }
}
