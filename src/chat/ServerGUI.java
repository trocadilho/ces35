package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.LineBorder;

/*
 * The server as a GUI
 */
public class ServerGUI extends JFrame implements ActionListener, WindowListener {
	
	private static final long serialVersionUID = 1L;
	// the stop and start buttons
	private JButton stopStart;
	// JTextArea for the chat room and the events
	private JTextArea chat, event;
	// The port number
	private JTextField tPortNumber;
	// my server
	private Server server;
	
	
	// server constructor that receive the port to listen to for connection as parameter
	ServerGUI(int port) {
		super("Chat Server");
		setResizable(false);
		getContentPane().setBackground(Color.BLACK);
		server = null;
		// in the NorthPanel the PortNumber the Start and Stop buttons
		JPanel north = new JPanel();
		north.setBackground(new Color(6, 94, 82));
		JLabel label = new JLabel("Port number: ");
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Arial", Font.BOLD, 11));
		north.add(label);
		tPortNumber = new JTextField("  " + port);
		tPortNumber.setBackground(new Color(255, 255, 255));
		tPortNumber.setCaretColor(new Color(0, 0, 0));
		tPortNumber.setForeground(new Color(0, 0, 0));
		tPortNumber.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tPortNumber.setBorder(new LineBorder(new Color(171, 173, 179)));
		tPortNumber.setSelectedTextColor(new Color(255, 255, 255));
		tPortNumber.setSelectionColor(new Color(51, 153, 255));
		north.add(tPortNumber);
		getContentPane().add(north, BorderLayout.NORTH);
		// to stop or start the server, we start with "Start"
		stopStart = new JButton("Start");
		stopStart.setForeground(Color.WHITE);
		stopStart.setContentAreaFilled(false);
		stopStart.setFont(new Font("Arial", Font.PLAIN, 11));
		stopStart.addActionListener(this);
		north.add(stopStart);
		
		// the event and chat room
		JPanel center = new JPanel(new GridLayout(2,1));
		chat = new JTextArea(5,5);
		chat.setForeground(new Color(0, 0, 0));
		chat.setBackground(new Color(204, 204, 255));
		chat.setSelectionColor(new Color(51, 153, 255));
		chat.setSelectedTextColor(new Color(255, 255, 255));
		chat.setEditable(false);
		appendRoom("Chat room.\n");
		center.add(new JScrollPane(chat));
		event = new JTextArea(5,5);
		event.setForeground(new Color(0, 0, 0));
		event.setBackground(new Color(204, 204, 255));
		event.setSelectionColor(new Color(51, 153, 255));
		event.setSelectedTextColor(new Color(255, 255, 255));
		event.setEditable(false);
		appendEvent("Events log.\n");
		center.add(new JScrollPane(event));	
		getContentPane().add(center);
		
		// need to be informed when the user click the close button on the frame
		addWindowListener(this);
		setSize(400, 600);
		setVisible(true);
	}		

	// append message to the two JTextArea
	// position at the end
	void appendRoom(String str) {
		chat.append(str);
		chat.setCaretPosition(chat.getText().length() - 1);
	}
	void appendEvent(String str) {
		event.append(str);
		try{
			event.setCaretPosition(chat.getText().length() - 1);
		} 
		catch (IllegalArgumentException a){
		}
		
	}
	
	// start or stop where clicked
	public void actionPerformed(ActionEvent e) {
		// if running we have to stop
		if(server != null) {
			server.stop();
			server = null;
			tPortNumber.setEditable(true);
			stopStart.setText("Start");
			return;
		}
      	// OK start the server	
		int port;
		try {
			port = Integer.parseInt(tPortNumber.getText().trim());
		}
		catch(Exception er) {
			appendEvent("Invalid port number");
			return;
		}
		// create a new Server
		server = new Server(port, this);
		// and start it as a thread
		new ServerRunning().start();
		stopStart.setText("Stop");
		tPortNumber.setEditable(false);
	}
	
	// entry point to start the Server
	public static void main(String[] arg) {
		// start server default port 1500
		new ServerGUI(1500);
	}

	/*
	 * If the user click the X button to close the application
	 * I need to close the connection with the server to free the port
	 */
	public void windowClosing(WindowEvent e) {
		// if my Server exist
		if(server != null) {
			try {
				server.stop();			// ask the server to close the connection
			}
			catch(Exception eClose) {
			}
			server = null;
		}
		// dispose the frame
		dispose();
		System.exit(0);
	}
	// I can ignore the other WindowListener method
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	/*
	 * A thread to run the Server
	 */
	class ServerRunning extends Thread {
		public void run() {
			server.start();         // should execute until ii fails
			// the server failed
			stopStart.setText("Start");
			tPortNumber.setEditable(true);
			appendEvent("Server interrupted.\n");
			server = null;
		}
	}

}


