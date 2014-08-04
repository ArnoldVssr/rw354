import java.net.*;
import java.io.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class MyClient 
{
	
	public static JFrame mainWindow = new JFrame();
	public static JTextField messageField = new JTextField(20);
	public static JTextArea chatArea = new JTextArea();
	public static JList onlineUsers = new JList();
	
	private static JButton connectButton = new JButton();
	private static JButton disconnectButton = new JButton();
	private static JButton sendButton = new JButton();
	private static JButton whisperButton = new JButton();
	private static JLabel messageLabel = new JLabel();
	private static JLabel chatLabel = new JLabel();
	private static JLabel onlineLabel = new JLabel();
	public static JLabel userNameLabel = new JLabel();
	private static JLabel userNameBox = new JLabel();
	private static JScrollPane chatScroller = new JScrollPane();
	private static JScrollPane onlineScroller = new JScrollPane();
	
	//log in window
	public static JFrame loginWindow = new JFrame();
	public static JTextField userNameBoxField = new JTextField(20);
	public static JTextField portNumberField = new JTextField(20);
	public static JTextField hostNameField = new JTextField(20);
	private static JButton enterButton = new JButton();
	private static JLabel enterUserNameLabel = new JLabel();
	private static JLabel enterPortLabel = new JLabel();
	private static JLabel enterHostLabel = new JLabel();
	private static JPanel loginPane = new JPanel();
		
	//client
	private static Socket socket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
	private static boolean isConnected = false;
	public static boolean notUnique = true;
	public static User user = new User();
	public static boolean readBusy = true;
	public static String username = "";
	public static String hostname = "";
	public static String portnum = "";
	public static String musername = "";
	public static String message = "";
	public static final class Lock { }
	public final static Object lock = new Lock();

	
	public static void BuildMainWindow()
	{
		mainWindow.getContentPane().setLayout(null);
		mainWindow.setSize(500, 320);
		//mainWindow.getContentPane().setBackground(Color.DARK_GRAY);
		
		//sendButton.setBackground(Color.GREEN);
		//sendButton.setForeground(Color.BLACK);
		sendButton.setText("Send");
		mainWindow.getContentPane().add(sendButton);
		sendButton.setBounds(10, 40, 115, 25);
		
		disconnectButton.setText("Disconnect");
		mainWindow.getContentPane().add(disconnectButton);
		disconnectButton.setBounds(130, 40, 115, 25);
		
		whisperButton.setText("Whisper");
		mainWindow.getContentPane().add(whisperButton);
		whisperButton.setBounds(10, 40, 115, 25);
		
		messageLabel.setText("Message:");
		mainWindow.getContentPane().add(messageLabel);
		messageLabel.setBounds(5, 6, 80, 20);
		
		messageField.requestFocus();
		mainWindow.getContentPane().add(messageField);
		messageField.setBounds(80, 4, 260, 30);
		
		chatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		chatLabel.setText("Lobby Chat:");
		mainWindow.getContentPane().add(chatLabel);
		chatLabel.setBounds(100, 70, 140, 16);
		
		chatArea.setColumns(20);
		chatArea.setLineWrap(true);
		chatArea.setRows(5);
		chatArea.setEditable(false);
		
		chatScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		chatScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		chatScroller.setViewportView(chatArea);
		mainWindow.getContentPane().add(chatScroller);
		chatScroller.setBounds(10, 90 , 330, 180);
		
		onlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		onlineLabel.setText("Online users:");
		mainWindow.getContentPane().add(onlineLabel);
		onlineLabel.setBounds(350, 70, 130, 16);
		
		onlineScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		onlineScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		onlineScroller.setViewportView(onlineUsers);
		mainWindow.getContentPane().add(onlineScroller);
		onlineScroller.setBounds(350, 90 , 130, 180);
		
		userNameLabel.setText("");
		mainWindow.getContentPane().add(userNameLabel);
		userNameLabel.setBounds(350, 10 , 140, 15);
		
		userNameBox.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(userNameBox);
		userNameBox.setBounds(340, 17 , 150, 20);
		
		mainWindow.setVisible(false);
		BuildLoginWindow();
	}
	
	public static void BuildLoginWindow()
	{
		loginWindow.setTitle("Cr@p Talk Config");
		loginWindow.setLayout(null);
		loginWindow.setSize(400, 180);
		
		enterHostLabel.setText("Host name:");
		loginWindow.getContentPane().add(enterHostLabel);
		enterHostLabel.setBounds(5, 5, 100, 20);
		
		hostNameField.requestFocus();
		loginWindow.getContentPane().add(hostNameField);
		hostNameField.setBounds(105, 5, 260, 20);
		
		enterPortLabel.setText("Port number:");
		loginWindow.getContentPane().add(enterPortLabel);
		enterPortLabel.setBounds(5, 35, 100, 20);
		
		loginWindow.getContentPane().add(portNumberField);
		portNumberField.setBounds(105, 35, 260, 20);
		
		enterUserNameLabel.setText("Username:");
		loginWindow.getContentPane().add(enterUserNameLabel);
		enterUserNameLabel.setBounds(5, 65, 100, 20);
		
		loginWindow.getContentPane().add(userNameBoxField);
		userNameBoxField.setBounds(105, 65, 260, 20);
		
		enterButton.setText("Login");
		loginWindow.getContentPane().add(enterButton);
		enterButton.setBounds(105, 95, 80, 20);
		
		DoLogin();
		loginWindow.setVisible(true);
	}
	
	public static void DoLogin()
	{
		enterButton.addActionListener(
			new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent event)
				{
					EnterChannel();
				}
			});
	}
	
	public static void EnterChannel()
	{
		if (!userNameBoxField.getText().equals("") && !hostNameField.getText().equals("")
				&& !portNumberField.getText().equals(""))
		{		
			String tempName = userNameBoxField.getText().trim();
			username = tempName;
			hostname = hostNameField.getText().trim();
			portnum = portNumberField.getText().trim();
			loginWindow.setVisible(false);
			sendButton.setEnabled(true);
			whisperButton.setEnabled(true);
			disconnectButton.setEnabled(true);
			connectButton.setEnabled(false);
			System.out.println("Doing connect");
			Communicate();
		}
		else
		{
			if (userNameBoxField.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Enter a username.");
			}
			else if(hostNameField.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Enter the servername.");
			}
			else if (portNumberField.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Enter a port number.");
			}
		}
	}
	
	
	public static void Communicate() 
	{
		//System.out.println(this);
		String inputName = "";
		String choice = "";
		InetAddress address;
		int port = -1;
		//BuildLoginWindow();
		//BuildMainWindow();
		loginWindow.setVisible(false);
		mainWindow.setVisible(true);

		while (!isConnected)
		{
			try 
			{
				socket = new Socket(hostname, Integer.parseInt(portnum));
				isConnected = true;
				
				sendbuf = new byte[socket.getSendBufferSize()];
	        	recbuf = new byte[socket.getReceiveBufferSize()];
				
				address = socket.getInetAddress();
				port = socket.getPort();
				user = new User(inputName, address,port);
				
				//build gui before
				MyThread io = new MyThread("io_" + user.getPort(),socket);
				io.start();
				

				while(notUnique)
				{
					System.out.println("Client says username was: " + username);
					synchronized(lock)
					{
						while (readBusy)
						{
							lock.wait();
						}
					}
					
					System.out.println("Notifying server of next send");
					User nuser = new User(username, address,port);
					sendbuf = ClientHandler.toByteArray(nuser);
					socket.getOutputStream().write(Message.USER);
					socket.getOutputStream().write(sendbuf);
					System.out.println("Client says username is: " + nuser.getName());
					socket.getInputStream().read(recbuf);
					notUnique = (Boolean) ClientHandler.toObject(recbuf);
					readBusy = true;
				}
				
				while(!choice.equalsIgnoreCase("bye"))
				{
					Message rec = new Message();
					try
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) ClientHandler.toObject(recbuf);			
					}
					finally
					{
						if (rec.getRecipient() != "" || rec.getOrigin().equalsIgnoreCase("server"))
						{
							System.out.println(rec.getOrigin() + ": " + rec.getMessage());
						}
						if (rec.getOrigin().equalsIgnoreCase("server"))
						{
							socket.wait(200);
							socket.close();
							break;
						}
					}
				}
			}
			catch (SocketException socketError) 
			{
				System.err.println(socketError.getMessage());
			} 
			catch (IOException e)
			{
				System.err.println("IO Exception");
			} 
			catch (ClassNotFoundException e) 
			{
				System.err.println(e.getMessage());
			} 
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) 
	{
		//MyClient client = new MyClient();
		//Communicate();
		BuildMainWindow();
		//BuildLoginWindow();
	}
}