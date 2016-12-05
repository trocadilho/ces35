package chat;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.JOptionPane;

/*
 * The Client with its GUI
 */
public class ClientGUI extends JFrame implements ActionListener, KeyListener {

	private static final long serialVersionUID = 1L;
	// will first hold "Username:", later on "Enter message"
	private JLabel label;
	// to hold the Username and later on the messages
	private JTextField tf;
	// to hold the server address and the port number
	private JTextField tfServer, tfPort;
	// to Logout and get the list of the users
	private JButton login, logout, whoIsIn;
	// for the chat room
	private JTextArea ta;
	// if it is for connection
	private boolean connected, saved;
	// the Client object
	private Client client;
	// the default port number
	private int defaultPort, choice;
	private String username, pass, passconfirm, oldpass;
	private String defaultHost;
	private String userLastMessage = "";

	// Constructor connection receiving a socket number
	ClientGUI(String host, int port) {

		super("Chat Client");
		setResizable(false);
		getContentPane().setBackground(Color.BLACK);
		defaultPort = port;
		defaultHost = host;
		
		// The NorthPanel with:
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		northPanel.setBackground(Color.BLACK);
		// the server name and the port number
		JPanel serverAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		serverAndPort.setBackground(Color.BLACK);
		// the two JTextField with default value for server address and port number
		tfServer = new JTextField(host);
		tfServer.setCaretColor(new Color(0, 0, 0));
		tfServer.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tfServer.setForeground(new Color(0, 0, 0));
		tfServer.setBackground(new Color(255, 255, 255));
		tfServer.setSelectionColor(new Color(51, 153, 255));
		tfServer.setSelectedTextColor(new Color(255, 255, 255));
		tfServer.setBorder(null);
		tfPort = new JTextField("" + port);
		tfPort.setCaretColor(new Color(0, 0, 0));
		tfPort.setBackground(new Color(255, 255, 255));
		tfPort.setForeground(new Color(0, 0, 0));
		tfPort.setSelectionColor(new Color(51, 153, 255));
		tfPort.setSelectedTextColor(new Color(255, 255, 255));
		tfPort.setBorder(null);
		tfPort.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		JLabel label_1 = new JLabel("Server Address:  ");
		label_1.setForeground(Color.WHITE);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBackground(new Color(6, 94, 82));
		label_1.setOpaque(true);
		label_1.setFont(new Font("Arial", Font.BOLD, 11));
		serverAndPort.add(label_1);
		serverAndPort.add(tfServer);
		JLabel label_2 = new JLabel("Port Number:  ");
		label_2.setBorder(null);
		label_2.setForeground(Color.WHITE);
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBackground(new Color(6, 94, 82));
		label_2.setOpaque(true);
		label_2.setFont(new Font("Arial", Font.BOLD, 11));
		serverAndPort.add(label_2);
		serverAndPort.add(tfPort);
		JLabel label_3 = new JLabel("");
		label_3.setOpaque(true);
		label_3.setBackground(new Color(6, 94, 82));
		serverAndPort.add(label_3);
		// adds the Server an port field to the GUI
		northPanel.add(serverAndPort);

		// the Label and the TextField
		label = new JLabel("Enter your username below", SwingConstants.CENTER);
		label.setForeground(Color.WHITE);
		label.setOpaque(true);
		label.setBackground(new Color(6, 94, 82));
		label.setFont(new Font("Arial", Font.BOLD, 11));
		northPanel.add(label);
		tf = new JTextField("Anonymous");
		tf.setBorder(null);
		tf.setCaretColor(new Color(0, 0, 0));
		tf.setFont(new Font("Monospaced", Font.PLAIN, 11));
		tf.setForeground(new Color(0, 0, 0));
		tf.setSelectionColor(new Color(51, 153, 255));
		tf.setSelectedTextColor(new Color(255, 255, 255));
		tf.setBackground(new Color(255, 255, 255));
		tf.setFocusTraversalKeysEnabled(false);
		tf.addKeyListener(this);
		northPanel.add(tf);
		getContentPane().add(northPanel, BorderLayout.NORTH);

		// The CenterPanel which is the chat room
		ta = new JTextArea("Welcome to the Chat room\n", 5, 5);
		ta.setBorder(null);
		ta.setForeground(new Color(0, 0, 0));
		ta.setBackground(new Color(204, 204, 255));
		ta.setSelectionColor(new Color(51, 153, 255));
		ta.setSelectedTextColor(new Color(255, 255, 255));
		ta.setFont(new Font("Monospaced", Font.PLAIN, 13));
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.setBackground(Color.BLACK);
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		getContentPane().add(centerPanel, BorderLayout.CENTER);

		// the 3 buttons
		login = new JButton("Login");
		login.setContentAreaFilled(false);
		login.setBackground(Color.BLACK);
		login.setForeground(new Color(255, 255, 255));
		login.setFont(new Font("Arial", Font.PLAIN, 11));
		login.addActionListener(this);
		logout = new JButton("Logout");
		logout.setForeground(new Color(255, 255, 255));
		logout.setContentAreaFilled(false);
		logout.setFont(new Font("Arial", Font.PLAIN, 11));
		logout.addActionListener(this);
		logout.setEnabled(false);		// you have to login before being able to logout
		whoIsIn = new JButton("Who is in");
		whoIsIn.setContentAreaFilled(false);
		whoIsIn.setForeground(new Color(255, 255, 255));
		whoIsIn.setFont(new Font("Tahoma", Font.PLAIN, 11));
		whoIsIn.addActionListener(this);
		whoIsIn.setEnabled(false);		// you have to login before being able to Who is in

		JPanel southPanel = new JPanel();
		southPanel.setBackground(new Color(6, 94, 82));
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(whoIsIn);
		getContentPane().add(southPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();


	}
	
	public static boolean challengeConfirm(String sender){
		if(JOptionPane.showConfirmDialog(null, "User " + sender + " has challenged you to a battle on Pokémon Showdown. Accept?", "Incoming challenge", JOptionPane.YES_NO_OPTION) == 0)
			return true;
		else
			return false;
	}

	// called by the Client to append text in the TextArea 
	void append(String str) {
		ta.append(str);
		try{
			ta.setCaretPosition(ta.getText().length() - 1);
		} 
		catch (IllegalArgumentException a){
		}
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	// called by the GUI if the connection failed
	// we reset our buttons, label, textfield
	void connectionFailed() {
		login.setEnabled(true);
		login.setText("Login");
		logout.setEnabled(false);
		whoIsIn.setEnabled(false);
		label.setText("Enter your username below");
		tf.setText("Anonymous");
		// reset port number and host name as a construction time
		tfPort.setText("" + defaultPort);
		tfServer.setText(defaultHost);
		// let the user change them
		tfServer.setEditable(true);
		tfPort.setEditable(true);
		// don't react to a <CR> after the username
		tf.removeActionListener(this);
		connected = false;
	}
		
	/*
	* Button or JTextField clicked
	*/
	public void actionPerformed(ActionEvent e) {
		boolean sendmessage = true;
		Object o = e.getSource();
		// if it is the Logout button

		if(o == login && !connected) 
		{
			// empty serverAddress ignore it
			String server = tfServer.getText().trim();
			if(server.length() == 0)
				return;
			// empty or invalid port number, ignore it
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;   // nothing I can do if port number is not valid
			}
				
			username = tf.getText();
			
			if(username.length()==0)
			{
				this.append("Please enter a username.\n");
				return;
			}
			else if(username.contains(" "))
			{
				this.append("Please enter a username without spaces.\n");
				return;
			}
			// try creating a new Client with GUI
			client = new Client(server, port, username, this);
			// test if we can start the Client
			if(!client.start()) 
				return;
			
			
			client.sendMessage(new ChatMessage(ChatMessage.LOGIN, username));
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			
			if(client.multipleLogin)
			{
				try {
					client.getsOutput().writeObject(new ChatMessage(ChatMessage.LOGOUT, username));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return;
			}
			
			if(!client.getloginExists()){
				JPasswordField field1 = new JPasswordField();
				JPasswordField field2 = new JPasswordField();
					
				Object[] fields1 = {
						"<html><span style='color:green'>First timers have to pick a password!<br><br></span>Password:</html>", field1,
						"Password (confirm):", field2
				};
				Object[] fields2 = {
						"<html><span style='color:red'>Password can't be empty!<br><br></span>Password:</html>", field1,
						"Password (confirm):", field2
				};
				Object[] fields3 = {
						"<html><span style='color:red'>Passwords don't match!<br><br></span>Password:</html>", field1,
						"Password (confirm):", field2
				};
					
				choice = JOptionPane.showConfirmDialog(null, fields1, "New user registration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
				saved = false;
				while(choice == JOptionPane.OK_OPTION && !saved){
					pass = String.copyValueOf(field1.getPassword());
					passconfirm = String.copyValueOf(field2.getPassword());
					if(pass.equals(""))
						choice = JOptionPane.showConfirmDialog(null, fields2, "New user registration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
					else{
						if(!pass.equals(passconfirm))
							choice = JOptionPane.showConfirmDialog(null, fields3, "New user registration", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
						else{
							try {
								client.getsOutput().writeObject(new ChatMessage(ChatMessage.MODIFYPASSWORD, username+" "+pass));
								
								JOptionPane.showMessageDialog(null, "User added to the database!");
								saved = true;
								
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							
						}
					}
				}
				if(!saved)
				{
					try {
						client.getsOutput().writeObject(new ChatMessage(ChatMessage.LOGOUT, username));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return;
				}
					
			}
			else{
				JPasswordField field1 = new JPasswordField();
				Object[] fields1 = {
						"<html><span style='color:red'>The name you chose is registered.<br><br></span>Password: </html>", field1,
				};
				Object[] fields2 = {
						"<html><span style='color:red'>Wrong password.<br><br></span>Password: </html>", field1,
				};
				
				choice = JOptionPane.showConfirmDialog(null, fields1, "Password confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				saved = false;
				while(choice == JOptionPane.OK_OPTION && !saved){
					pass = String.copyValueOf(field1.getPassword());
					try {
						client.getsOutput().writeObject(new ChatMessage(ChatMessage.CHECKPASSWORD, username+" "+pass));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					
					if(!client.getcheckPassword())
						choice = JOptionPane.showConfirmDialog(null, fields2, "Password confirmation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
					else
						saved = true;
				}
				if(!saved)
				{
					try {
						client.getsOutput().writeObject(new ChatMessage(ChatMessage.LOGOUT, username));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					return;
				}
			}
			
			tf.setText("");
			label.setText("Enter your message below");
			connected = true;
				
			// disable login button
			login.setText("Change password");
			// enable the 2 buttons
			logout.setEnabled(true);
			whoIsIn.setEnabled(true);
			// disable the Server and Port JTextField
			tfServer.setEditable(false);
			tfPort.setEditable(false);
			// Action listener for when the user enter a message
			tf.addActionListener(this);
				
			
			sendmessage = false;
		}
		else if(o == login && connected) {
			JPasswordField field1 = new JPasswordField();
			JPasswordField field2 = new JPasswordField();
			JPasswordField field3 = new JPasswordField();
				
			Object[] fields1 = {
					"<html><span style='color:green'>Change your password!<br><br></span>Old Password:</html>", field1,
					"New password:", field2,
					"New password (confirm):", field3
			};
			Object[] fields2 = {
					"<html><span style='color:red'>Old password is incorrect!<br><br></span>Old Password:</html>", field1,
					"New password:", field2,
					"New password (confirm):", field3
			};
			Object[] fields3 = {
					"<html><span style='color:red'>Password can't be empty!<br><br></span>Old Password:</html>", field1,
					"New password:", field2,
					"New password (confirm):", field3
			};
			Object[] fields4 = {
					"<html><span style='color:red'>Passwords don't match!<br><br></span>Old Password:</html>", field1,
					"New password:", field2,
					"New password (confirm):", field3
			};
			
			choice = JOptionPane.showConfirmDialog(null, fields1, "Security settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
			saved = false;
			while(choice == JOptionPane.OK_OPTION && !saved){
				oldpass = String.copyValueOf(field1.getPassword());
				pass = String.copyValueOf(field2.getPassword());
				passconfirm = String.copyValueOf(field3.getPassword());
				
				try {
					client.getsOutput().writeObject(new ChatMessage(ChatMessage.CHECKPASSWORD, username+" "+oldpass));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				if(!client.getcheckPassword())
					choice = JOptionPane.showConfirmDialog(null, fields2, "Security settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
				else{
					if(pass.equals(""))
						choice = JOptionPane.showConfirmDialog(null, fields3, "Security settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
					else{
						if(!pass.equals(passconfirm))
							choice = JOptionPane.showConfirmDialog(null, fields4, "Security settings", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
						else{
							try {
								client.getsOutput().writeObject(new ChatMessage(ChatMessage.MODIFYPASSWORD, username+" "+pass));
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							JOptionPane.showMessageDialog(null, "Password changed!");
							saved = true;
						}
					}
				}
			}
			sendmessage = false;
			return;
		}

		if(o == logout) {
			this.append("You have logged out.\n");
			client.sendMessage(new ChatMessage(ChatMessage.LOGOUT, username));
			return;
		}
		// if it the who is in button
		if(o == whoIsIn) {
			client.sendMessage(new ChatMessage(ChatMessage.WHOISIN, ""));				
			return;
		}
		
		// OK it is coming from the JTextField
		if(connected && sendmessage) {
			// just have to send the message
			if(tf.getText().startsWith("/challenge "))
			{
				String challenger = tf.getText().substring(11);
				client.sendMessage(new ChatMessage(ChatMessage.CHALLENGE, challenger + " has challenged you"));
			}
			else
				client.sendMessage(new ChatMessage(ChatMessage.MESSAGE, tf.getText()));
			if(tf.getText().startsWith("/msg ")){
				if(tf.getText().indexOf(" ",5) != -1){
					userLastMessage = tf.getText().substring(5, tf.getText().indexOf(" ",5));
				}
			}
			tf.setText("");
			return;
		}

		
	}

	// to start the whole thing the server
	public static void main(String[] args) {
		new ClientGUI("localhost", 1500);
	}

	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();
		if(id == KeyEvent.VK_TAB && tf.getText().equals(""))
		{
			if(userLastMessage.equals(""))
				tf.setText("/msg ");
			else
				tf.setText("/msg " + userLastMessage + " ");
		}
		return;
		
	}

	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent e) {

		
	}

}


