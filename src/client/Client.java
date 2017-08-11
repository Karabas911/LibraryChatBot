package client;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
//Client part of application
public class Client extends JFrame implements Runnable {
	
	private static final String WINDOW_NAME = "Library Chat Bot";
	private static final int SERVER_PORT = Server.PORT;
    private static final String ADDRESS = "127.0.0.1";
    
    private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private InputStream sin;
	private OutputStream sout;
	
    private JTextField txtEnter;
    private JTextArea txtChat;
    private JButton sendButton;
    private JScrollPane scroll;
	
    //Creating some graphic UI   
    public Client(String name) {
    	super(name);
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	this.setSize(500,500);
    	this.setVisible(true);
    	this.setResizable(false);
    	this.setVisible(true);
    	this.setLayout(null);
    	this.setLocationRelativeTo(null);
    	
    	txtChat = new JTextArea();
    	txtChat.setLocation(2,0);
    	txtChat.setSize(490,430);
    	scroll = new JScrollPane(txtChat,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	txtChat.setLineWrap(true);
    	txtChat.setWrapStyleWord(true);
    	txtChat.setEditable(false);
    	
    	txtEnter = new JTextField();
    	txtEnter.setLocation(2, 440);
    	txtEnter.setSize(420,30);
    	txtEnter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				sendButton.doClick();
			}
    		
    	});
    	
    	sendButton = new JButton("Send");
    	sendButton.setLocation(420,440);
    	sendButton.setSize(70,29);
    	sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String uText = txtEnter.getText();
				txtEnter.setText("");
				txtChat.append("You: "+uText+"\n");
				sendData(uText);
			}    		
    	});
    	
    	this.add(txtEnter);
    	this.add(txtChat);
    	this.add(sendButton);
    	this.add(scroll);
    	this.repaint();
    	
    }
    
	public static void main(String[] args) {		
		new Thread(new Server()).start();
		new Thread(new Client(WINDOW_NAME)).start();
		try {
	        Database.createDbLibraryTable();
	    } catch (SQLException e) {
	        System.out.println(e.getMessage());
	    }
	}

	@Override
	public void run() {
		 try {
			 	// Try connect to the server
	            InetAddress ipAddress = InetAddress.getByName(ADDRESS);
	            socket = new Socket(ipAddress, SERVER_PORT);
	            
	            //Make Input and Output Stream to send and get messages 
	            sin = socket.getInputStream();
	            sout = socket.getOutputStream();
	            
	            // Convert Stream to another type 
	            in = new DataInputStream(sin);
	            out = new DataOutputStream(sout);
	        } catch (Exception x) {
	            x.printStackTrace();
	        }
	}
	// Function which send data to server and get answer from it
	private void sendData(String str) {
			String line = str;
			try {
				out.writeUTF(line);
				line = in.readUTF();			
				} catch (IOException e) {
					e.printStackTrace();
			}
			txtChat.append("Bot: " + line+"\n");
	}

}
