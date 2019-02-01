 // Sever App Code //
 
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
public class JavaServer extends JFrame implements ActionListener, KeyListener,
        FocusListener {
  // Extra variables<br />
    static String message = &quot;&quot;;
    static String userName = &quot;&quot;;
  // Networking Variables
    static ServerSocket server = null;
    static Socket socket = null;
    static PrintWriter writer = null;
// // Graphics Variables
    static JTextArea msgRec = new JTextArea(100, 50);
    static JTextArea msgSend = new JTextArea(100, 50);
    JButton send = new JButton(&quot;Send&quot;);
    JScrollPane pane2, pane1;
   JMenuBar bar = new JMenuBar();
   JMenu messanger = new JMenu(&quot;Messanger&quot;);
    JMenuItem logOut = new JMenuItem(&quot;Log Out&quot;);
   JMenu help = new JMenu(&quot;Help&quot;);
    JMenuItem s_keys = new JMenuItem(&quot;Shortcut Keys&quot;);
    JMenuItem about = new JMenuItem(&quot;about&quot;);
   public JavaServer() {
        super(&quot;Java Server&quot;);
        setBounds(0, 0, 407, 495);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null);
       msgRec.setEditable(false);
        msgRec.setBackground(Color.BLACK);
        msgRec.setForeground(Color.WHITE);
        msgRec.addFocusListener(this);
        msgRec.setText(&quot;&quot;);
       msgRec.setWrapStyleWord(true);
        msgRec.setLineWrap(true);
       pane2 = new JScrollPane(msgRec);
        pane2.setBounds(0, 0, 400, 200);
        pane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane2);</p>
       msgSend.setBackground(Color.LIGHT_GRAY);
        msgSend.setForeground(Color.BLACK);
        msgSend.setLineWrap(true);
        msgSend.setWrapStyleWord(true);
       msgSend.setText(&quot;Write Message here&quot;);
        msgSend.addFocusListener(this);
        msgSend.addKeyListener(this);
      pane1 = new JScrollPane(msgSend);
        pane1.setBounds(0, 200, 400, 200);
        pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane1);</p>
       send.setBounds(0, 400, 400, 40);
        add(send);<br />
        send.addActionListener(this);
       bar.add(messanger);
        messanger.add(logOut);
        logOut.addActionListener(this);
       bar.add(help);
        help.add(s_keys);
        s_keys.addActionListener(this);
        help.add(about);
        about.addActionListener(this);
       setJMenuBar(bar);
       addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
               if (!msgRec.getText().equals(&quot;&quot;)) {
                    System.out.println(&quot;Yes Focus&quot;);
                      writer.println(&quot;\t&amp;amp;amp;amp;lt;&amp;amp;amp;amp;lt;&amp;amp;amp;amp;lt;&quot; + userName + &quot;: Focusing to Comunication&amp;amp;amp;amp;gt;&amp;amp;amp;amp;gt;&amp;amp;amp;amp;gt;&quot;);
                    writer.flush();
                 }
             }
             @Override
            public void windowLostFocus(WindowEvent e) {
                if (!msgRec.getText().equals(&quot;&quot;)) {
                    writer.println(&quot;\t&amp;amp;amp;amp;lt;&amp;amp;amp;amp;lt;&amp;amp;amp;amp;lt;&quot; + userName + &quot;: Ignoring to Comunication&amp;amp;amp;amp;gt;&amp;amp;amp;amp;gt;&amp;amp;amp;amp;gt;&quot;);<br />
                    writer.flush();
                }
            }
<p>       });
<p>       if ((userName) != null) {<br />
            setVisible(true);<br />
        } else {<br />
            System.exit(0);<br />
        }<br />
    }</p>
<p>   /**<br />
     * @param args<br />
     */</p>
<p>   public static void main(String[] args) throws Exception {<br />
        // TODO Auto-generated method stub<br />
        userName = JOptionPane.showInputDialog(&quot;User Name (Server)&quot;);</p>
<p>       // swing thread<br />
        (new Thread(new Runnable() {<br />
            public void run() {<br />
                new JavaServer();<br />
            }</p>
<p>       })).start();</p>
<p>       server = new ServerSocket(8888);<br />
        System.out.println(server.getInetAddress().getLocalHost());</p>
<p>       socket = server.accept();</p>
<p>       msgRec.setText(&quot;Connected!&quot;);<br />
        // listening port thread<br />
        (new Thread(new Runnable() {<br />
            public void run() {</p>
<p>               try {<br />
                    BufferedReader reader = new BufferedReader(<br />
                            new InputStreamReader(socket.getInputStream()));</p>
<p>                   String line = null;<br />
                    boolean testFlag = true;<br />
                    while ((line = reader.readLine()) != null) {</p>
<p>                       msgRec.append(&quot;\n&quot; + line);</p>
<p>                       // Cursor Update<br />
                        cursorUpdate();<br />
                    }</p>
<p>               } catch (IOException ee) {<br />
                    try {<br />
                        server.close();<br />
                        socket.close();<br />
                    } catch (IOException eee) {<br />
                        eee.printStackTrace();<br />
                    }<br />
                    ee.printStackTrace();<br />
                }</p>
<p>           }<br />
        })).start();</p>
<p>       try {<br />
            writer = new PrintWriter(socket.getOutputStream(), true);</p>
<p>       } catch (IOException e) {<br />
            try {<br />
                server.close();<br />
                socket.close();<br />
            } catch (IOException eee) {<br />
            }<br />
        }<br />
    }</p>
<p>   // ActionEvents<br />
    @Override<br />
    public void actionPerformed(ActionEvent e) {<br />
        Object scr = e.getSource();</p>
<p>       if (scr == send) {<br />
            sendMessage();<br />
        } else if (scr == logOut) {</p>
<p>           System.exit(0);</p>
<p>       } else if (scr == s_keys) {</p>
<p>           JOptionPane.showMessageDialog(this,<br />
                    &quot;(shift+Enter) for new line while writing message&quot;<br />
                            + &quot;\n(ctrl+x) for quit&quot;);</p>
<p>       } else if (scr == about) {<br />
            JOptionPane.showMessageDialog(this,<br />
                    &quot;Messanger 1.0\ndeveloped @IMCS University of Sindh&quot;);<br />
        }<br />
    }</p>
<p>   // / KeyBoardEvents</p>
<p>   @Override<br />
    public void keyTyped(KeyEvent e) 
<p>   }</p>
<p>   @Override<br />
    public void keyReleased(KeyEvent e) {</p>
<p>   }</p>
<p>   @Override<br />
    public void keyPressed(KeyEvent e) {</p>
<p>       if ((e.getKeyCode() == KeyEvent.VK_ENTER) &amp;amp;amp;amp;amp;&amp;amp;amp;amp;amp; e.isShiftDown()) {<br />
            msgSend.append(&quot;\n&quot;);</p>
<p>       } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {<br />
            sendMessage();<br />
        }</p>
<p>       else if ((e.getKeyCode() == KeyEvent.VK_X) &amp;amp;amp;amp;amp;&amp;amp;amp;amp;amp; e.isControlDown()) {<br />
            System.exit(0);<br />
        }<br />
    }</p>
<p>   // FocusEvents<br />
    @Override<br />
    public void focusGained(FocusEvent e) {</p>
<p>       if (e.getSource() == msgRec) {<br />
            if (!(msgRec.getText().equals(&quot;&quot;) || msgRec.getText().equals(<br />
                    &quot;Connected!&quot;))) {</p>
<p>               writer.println(&quot;\t ***&quot; + userName<br />
                        + &quot;: The Msg is being read......***&quot;);<br />
                writer.flush();<br />
            }<br />
        } else if (e.getSource() == msgSend) {<br />
            // Set Mesg sending area clear<br />
            if (msgSend.getText().equals(&quot;Write Message here&quot;)) {<br />
                msgSend.setText(&quot;&quot;);<br />
            } else {<br />
                writer.println(&quot;\t ***&quot; + userName<br />
                        + &quot;: The Msg is being typed......***&quot;);<br />
                writer.flush();<br />
            }</p>
<p>       }<br />
    }</p>
<p>   @Override<br />
    public void focusLost(FocusEvent e) {</p>
<p>   }</p>
<p>   private void sendMessage() {<br />
        writer.println(userName + &quot; :&quot; + msgSend.getText());<br />
        writer.flush();</p>
<p>       msgRec.append(&quot;\nMe: &quot; + msgSend.getText());</p>
<p>       cursorUpdate();</p>
<p>       msgSend.setText(&quot;&quot;);<br />
        msgSend.setCaretPosition(0);<br />
    }</p>
<p>   private static void cursorUpdate() {<br />
        // Update cursor position<br />
        DefaultCaret caret = (DefaultCaret) msgRec.getCaret();<br />
        caret.setDot(msgRec.getDocument().getLength());</p>
<p>       DefaultCaret caret2 = (DefaultCaret) msgSend.getCaret();<br />
        caret2.setDot(msgSend.getDocument().getLength());<br />
    }</p>
<p>}<br />