//client

import java.awt.Color;<br />
import java.awt.event.ActionEvent;<br />
import java.awt.event.ActionListener;<br />
import java.awt.event.FocusEvent;<br />
import java.awt.event.FocusListener;<br />
import java.awt.event.KeyEvent;<br />
import java.awt.event.KeyListener;<br />
import java.awt.event.WindowEvent;<br />
import java.awt.event.WindowFocusListener;<br />
import java.io.BufferedReader;<br />
import java.io.IOException;<br />
import java.io.InputStreamReader;<br />
import java.io.PrintWriter;<br />
import java.net.Socket;</p>
import javax.swing.JButton;<br />
import javax.swing.JFrame;<br />
import javax.swing.JMenu;<br />
import javax.swing.JMenuBar;<br />
import javax.swing.JMenuItem;<br />
import javax.swing.JOptionPane;<br />
import javax.swing.JScrollPane;<br />
import javax.swing.JTextArea;<br />
import javax.swing.text.DefaultCaret;</p>
public class JavaClient extends JFrame implements ActionListener, KeyListener,
        FocusListener {</p>
<p>   // Extra variables<br />
    static String message = &quot;&quot;;<br />
    static String userName = &quot;&quot;;<br />
    static String iPAddress = null;</p>
<p>   // Networking Variables<br />
    static Socket socket = null;<br />
    static PrintWriter writer = null;</p>
<p>   // // Graphics Variables<br />
    static JTextArea msgRec = new JTextArea(100, 50);<br />
    static JTextArea msgSend = new JTextArea(100, 50);<br />
    JButton send = new JButton(&quot;Send&quot;);<br />
    JScrollPane pane2, pane1;</p>
<p>   JMenuBar bar = new JMenuBar();</p>
<p>   JMenu messanger = new JMenu(&quot;Messanger&quot;);<br />
    JMenuItem logOut = new JMenuItem(&quot;Log Out&quot;);</p>
<p>   JMenu help = new JMenu(&quot;Help&quot;);<br />
    JMenuItem s_keys = new JMenuItem(&quot;Shortcut Keys&quot;);<br />
    JMenuItem about = new JMenuItem(&quot;about&quot;);</p>
<p>   public JavaClient() {<br />
        super(&quot;Java Client&quot;);<br />
        setBounds(0, 0, 407, 495);<br />
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);<br />
        setResizable(false);<br />
        setLayout(null);</p>
<p>       msgRec.setEditable(false);<br />
        msgRec.setBackground(Color.BLACK);<br />
        msgRec.setForeground(Color.WHITE);<br />
        msgRec.addFocusListener(this);<br />
        msgRec.setText(&quot;&quot;);</p>
<p>       msgRec.setWrapStyleWord(true);<br />
        msgRec.setLineWrap(true);</p>
<p>       pane2 = new JScrollPane(msgRec);<br />
        pane2.setBounds(0, 0, 400, 200);<br />
        pane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);<br />
        add(pane2);</p>
<p>       msgSend.setBackground(Color.LIGHT_GRAY);<br />
        msgSend.setForeground(Color.BLACK);<br />
        msgSend.setLineWrap(true);<br />
        msgSend.setWrapStyleWord(true);</p>
<p>       msgSend.setText(&quot;Write Message here&quot;);<br />
        msgSend.addFocusListener(this);<br />
        msgSend.addKeyListener(this);</p>
<p>       pane1 = new JScrollPane(msgSend);<br />
        pane1.setBounds(0, 200, 400, 200);<br />
        pane1.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);<br />
        add(pane1);</p>
<p>       send.setBounds(0, 400, 400, 40);<br />
        add(send);<br />
        send.addActionListener(this);</p>
<p>       bar.add(messanger);<br />
        messanger.add(logOut);<br />
        logOut.addActionListener(this);</p>
<p>       bar.add(help);<br />
        help.add(s_keys);<br />
        s_keys.addActionListener(this);<br />
        help.add(about);<br />
        about.addActionListener(this);</p>
<p>       setJMenuBar(bar);</p>
<p>       addWindowFocusListener(new WindowFocusListener() {<br />
            @Override<br />
            public void windowGainedFocus(WindowEvent e) {</p>
<p>               if (!msgRec.getText().equals(&quot;&quot;)) {<br />
                    System.out.println(&quot;Yes Focus&quot;);</p>
<p>                   writer.println(&quot;\t&lt;&lt;&lt;&quot; + userName + &quot;: Focusing to Comunication&gt;&gt;&gt;&quot;);<br />
                    writer.flush();</p>
<p>               }<br />
            }</p>
<p>           @Override<br />
            public void windowLostFocus(WindowEvent e) {<br />
                if (!msgRec.getText().equals(&quot;&quot;)) {<br />
                    writer.println(&quot;\t&lt;&lt;&lt;&quot; + userName + &quot;: Ignoring to Comunication&gt;&gt;&gt;&quot;);<br />
                    writer.flush();<br />
                }<br />
            }</p>
<p>       });</p>
<p>       if ((userName) != null) {<br />
            setVisible(true);<br />
        } else {<br />
            System.exit(0);<br />
        }<br />
    }</p>
<p>   /**<br />
     * @param args<br />
     */<br />
    public static void main(String[] args) throws Exception {<br />
        // TODO Auto-generated method stub</p>
<p>       userName = JOptionPane.showInputDialog(&quot;User Name (Client)&quot;);<br />
        iPAddress = JOptionPane.showInputDialog(&quot;Enter Server IpAddress&quot;);</p>
<p>       // swing thread<br />
        (new Thread(new Runnable() {<br />
            public void run() {<br />
                new JavaClient();</p>
<p>           }</p>
<p>       })).start();</p>
<p>       socket = new Socket(iPAddress, 8888);<br />
        msgRec.setText(&quot;Connected!&quot;);</p>
<p>       // listening port thread<br />
        (new Thread(new Runnable() {<br />
            public void run() {</p>
<p>               try {<br />
                    BufferedReader reader = new BufferedReader(<br />
                            new InputStreamReader(socket.getInputStream()));</p>
<p>                   String line = null;<br />
                    boolean testFlag = true;<br />
                    while ((line = reader.readLine()) != null) {<br />
                        msgRec.append(&quot;\n&quot; + line);<br />
                        cursorUpdate();</p>
<p>                       if (!reader.ready()) {<br />
                            testFlag = true;<br />
                        }<br />
                    }</p>
<p>               } catch (IOException ee) {<br />
                    try {<br />
                        socket.close();<br />
                    } catch (IOException eee) {<br />
                        eee.printStackTrace();<br />
                    }<br />
                    ee.printStackTrace();<br />
                }<br />
            }<br />
        })).start();</p>
<p>       try {<br />
            writer = new PrintWriter(socket.getOutputStream(), true);</p>
<p>       } catch (IOException e) {<br />
            try {<br />
                socket.close();<br />
            } catch (IOException eee) {<br />
            }<br />
        }<br />
    }</p>
<p>   // ActionEvents</p>
<p>   @Override<br />
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
    public void keyTyped(KeyEvent e) {<br />
    }</p>
<p>   @Override<br />
    public void keyReleased(KeyEvent e) {<br />
    }</p>
<p>   @Override<br />
    public void keyPressed(KeyEvent e) {</p>
<p>       if ((e.getKeyCode() == KeyEvent.VK_ENTER) &amp;&amp; e.isShiftDown()) {<br />
            msgSend.append(&quot;\n&quot;);</p>
<p>       } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {<br />
            sendMessage();<br />
        }</p>
<p>       else if ((e.getKeyCode() == KeyEvent.VK_X) &amp;&amp; e.isControlDown()) {<br />
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
    public void focusLost(FocusEvent e) {<br />
    }</p>
<p>   private void sendMessage() {<br />
        writer.println(userName + &quot; :&quot; + msgSend.getText());</p>
<p>       msgRec.append(&quot;\nMe: &quot; + msgSend.getText());<br />
        writer.flush();<br />
        cursorUpdate();</p>
<p>       msgSend.setText(&quot;&quot;);<br />
        msgSend.setCaretPosition(0);<br />
    }</p>
<p>   private static void cursorUpdate() {<br />
        // Update cursor position<br />
        DefaultCaret caret = (DefaultCaret) msgRec.getCaret();<br />
        caret.setDot(msgRec.getDocument().getLength());</p>
<p>       DefaultCaret caret2 = (DefaultCaret) msgSend.getCaret();<br />
        caret2.setDot(msgSend.getDocument().getLength());<br />
    }<br />
}</p>
<p>// A Java Client program will take a Server Ip address<br />