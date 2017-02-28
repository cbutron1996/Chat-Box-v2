/**
 * Created by christianbutron on 2/26/17.
 */
import java.net.*;
import java.io.*;

public class Client {
    private static Socket server;
    private static GUI gui;
    private static PrintWriter pout;
    private static BufferedReader bin;

    public static void main(String[] args) throws Exception {
        server = new Socket("127.0.0.1", 1337);
        gui = new GUI(server);
        OutputStream out = server.getOutputStream();
        pout = new PrintWriter(out, true);
        InputStream in = server.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));

        while(true) {
            String response = bin.readLine();
            if(response.startsWith("/quit")) {
                System.exit(0);
                break;
            }
            gui.send(response);
        }

        bin.close();
        pout.close();
        server.close();
    }

}
