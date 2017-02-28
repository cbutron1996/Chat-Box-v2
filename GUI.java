/**
 * Created by christianbutron on 2/26/17.
 */
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.net.*;

public class GUI extends JFrame {
    private ArrayList<JPanel> row;
    private JTextArea textArea;
    private JScrollPane scroll;
    private JTextField textField;
    private JButton sendButton;
    private Font font;

    private Socket server;
    private static PrintWriter pout;
    private static BufferedReader bin;

    private final int NUM_ROWS = 2, NUM_COLS = 2;

    public void send() {
        String request = textField.getText();
        pout.println(request);
        textField.setText("");
    }

    public void send(String text) {
        textArea.append(text + "\n");
    }

    public GUI(Socket server) throws Exception {
        super("Chat Box");

        this.server = server;
        OutputStream out = this.server.getOutputStream();
        pout = new PrintWriter(out, true);
        InputStream in = this.server.getInputStream();
        bin = new BufferedReader(new InputStreamReader(in));

        row = new ArrayList<JPanel>();
        font = new Font("Times New Roman", Font.BOLD, 16);

        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        GridLayout grid = new GridLayout(NUM_ROWS,NUM_COLS);
        setLayout(grid);

        for(int i = 0; i < NUM_ROWS; i++) {
            row.add(new JPanel());
            row.get(i).setLayout(new FlowLayout(FlowLayout.CENTER,10,10));
        }

        textArea = new JTextArea(10, 30);
        textArea.setFont(font);
        textArea.setEditable(false);
        textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        //textArea.setPreferredSize(new Dimension(350, 160));

        scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(0, 0, 500, 500);

        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        textField = new JTextField(20);
        textField.setFont(font);
        textField.setEditable(true);
        textField.setPreferredSize(new Dimension(200, 40));
        textField.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    send();
                }
            }
            public void keyReleased(KeyEvent e) { }
            public void keyTyped(KeyEvent e) { }
        });

        sendButton = new JButton("Send");
        sendButton.setPreferredSize(new Dimension(100, 40));
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                send();
            }
        });

        row.get(0).add(scroll);
        add(row.get(0));

        row.get(1).add(textField);
        row.get(1).add(sendButton);
        add(row.get(1));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
