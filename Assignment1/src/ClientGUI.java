import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.*;
import java.security.Key;

import javax.swing.*;


public class ClientGUI
{
	private static MyClient chatClient;
	public static String userName = "";
	public static String hostName = "";
	public static String portNumber = "";
	public static KeyListener mal = new KeyListener()
	{
		
		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void keyPressed(KeyEvent arg0) {
			arg0.getID();
			
		}
	};
	
	//reader/writers
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
	public static boolean writeBusy = false;
	
	//main window
	public static JFrame mainWindow = new JFrame();
	public static JTextField messageField = new JTextField(20);
	public static JTextArea chatArea = new JTextArea();
	public static JList onlineUsers = new JList();
	
	private static JButton connectButton = new JButton();
	private static JButton disconnectButton = new JButton();
	private static JButton sendButton = new JButton();
	private static JButton whisperButton = new JButton();
	private static JLabel messageLabel = new JLabel("Message: ");
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
	public static JButton enterButton = new JButton();
	private static JLabel enterUserNameLabel = new JLabel();
	private static JLabel enterPortLabel = new JLabel();
	private static JLabel enterHostLabel = new JLabel();
	private static JPanel loginPane = new JPanel();
	
	//whisper Window
	public static JFrame whisperWindow = new JFrame();
	public static JTextField toUserField = new JTextField(20);
	public static JTextField whisperMessageField = new JTextField(20);
	private static JButton sendOneButton = new JButton("Send");
	private static JLabel toUserLabel = new JLabel("Enter username: ");
	private static JLabel whisperMessageLabel = new JLabel("Enter username: ");
	private static JPanel whisperPane = new JPanel();
	
	public static void main(String[] args)
	{
		BuildMainWindow();
		Initialize();
	}
	
	public static void BuildMainWindow()
	{
		mainWindow.setTitle("Cr@p Talk");
		mainWindow.setSize(500, 320);
		mainWindow.setResizable(false);
		ConfigureMainWindow();
		MainWindowAction();
		BuildLoginWindow();
	}
	
	public static void Initialize()
	{
		sendButton.setEnabled(false);
		disconnectButton.setEnabled(false);
		whisperButton.setEnabled(false);
		connectButton.setEnabled(true);
	}
	
	
	public static void Connect()
	{
		try
		{
			//final int port = 6066;
			//final String hostName = "localhost";
			
			//Socket clientSocket = new Socket(hostName, port);
			//recbuf = new byte[clientSocket.getReceiveBufferSize()];
			//sendbuf = new byte[clientSocket.getSendBufferSize()];
			
			//System.out.println("You connected to: " + hostName);
			//chatClient = new MyClient("client_" + clientSocket.getPort(), clientSocket);
			
			//output = new PrintWriter(clientSocket.getOutputStream());
			//System.out.println("username sent to server");
			//output.println(userName);
			//output.flush();
			
			loginWindow.setVisible(false);
			mainWindow.setVisible(true);
			
			chatClient = new MyClient();
			chatClient.start();
		}
		catch (Exception e)
		{
			System.out.println("error in connect from clientgui");
			JOptionPane.showMessageDialog(null, "Server not responding");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static void ConfigureMainWindow()
	{
		mainWindow.getContentPane().setLayout(null);
		mainWindow.setSize(500, 320);
		
		sendButton.setText("Send");
		mainWindow.getContentPane().add(sendButton);
		sendButton.setBounds(130, 40, 115, 25);
		
		disconnectButton.setText("Disconnect");
		mainWindow.getContentPane().add(disconnectButton);
		disconnectButton.setBounds(350, 40, 115, 25);
		
		whisperButton.setText("Whisper");
		mainWindow.getContentPane().add(whisperButton);
		whisperButton.setBounds(10, 40, 115, 25);
		
		messageLabel.setText("Message:");
		mainWindow.getContentPane().add(messageLabel);
		messageLabel.setBounds(5, 6, 80, 20);
		
		messageField.requestFocus();
		messageField.addKeyListener(mal);
		mainWindow.getContentPane().add(messageField);
		messageField.setBounds(80, 4, 260, 30);
		
		chatLabel.setHorizontalAlignment(SwingConstants.CENTER);
		chatLabel.setText("Conversation");
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
	}
	
	public static void BuildLoginWindow()
	{		
		loginWindow.setTitle("Cr@p Talk Config");
		loginWindow.setLayout(null);
		loginWindow.setSize(400, 180);
		
		enterHostLabel.setText("Host name:");
		loginWindow.getContentPane().add(enterHostLabel);
		enterHostLabel.setBounds(5, 5, 100, 20);
		
		//hostNameField.requestFocus();
		hostNameField.setText("localhost");
		loginWindow.getContentPane().add(hostNameField);
		hostNameField.setBounds(105, 5, 260, 20);
		
		enterPortLabel.setText("Port number:");
		loginWindow.getContentPane().add(enterPortLabel);
		enterPortLabel.setBounds(5, 35, 100, 20);
		
		portNumberField.setText("6066");
		loginWindow.getContentPane().add(portNumberField);
		portNumberField.setBounds(105, 35, 260, 20);
		
		enterUserNameLabel.setText("Username:");
		loginWindow.getContentPane().add(enterUserNameLabel);
		enterUserNameLabel.setBounds(5, 65, 100, 20);
		
		userNameBoxField.setText("tim");
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
					EnterChat();
				}
			});
	}
	
	public static void EnterChat()
	{
		if (!userNameBoxField.getText().equals(""))
		{		
			userName = userNameBoxField.getText().trim();		
			hostName = hostNameField.getText().trim();
			portNumber = portNumberField.getText().trim();
			loginWindow.setVisible(false);
			sendButton.setEnabled(true);
			disconnectButton.setEnabled(true);
			connectButton.setEnabled(false);
			//System.out.println("Doing connect");
			Connect();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Enter a username.");
		}
	}
	
	public static void MainWindowAction()
	{
		sendButton.addActionListener(
			new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent event)
				{
					SendButton();
				}
			});
		if (messageField.getKeyListeners().equals((KeyEvent.VK_ENTER)))
		{
			SendButton();
		}
		/*disconnectButton.addActionListener(
			new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent event)
				{
					DisconnectButton();
				}
			});*/
	}
		
	public static void SendButton()
	{
		if (!messageField.getText().equals(""))
		{
			char isWhisp = messageField.getText().trim().charAt(0);
			if (isWhisp == '@')
			{
				String temp = messageField.getText().trim();
				int firstSpace = temp.indexOf(" ");
				String toUser = temp.substring(1, firstSpace);
				String message = temp.substring(firstSpace +1);
				
				Message whisper = new Message(userName, toUser, message);
				MyClient.Send(whisper);
				messageField.setText("");
				messageField.requestFocus();
				
			}
			else
			{
				//messageField.ge
				Message lobby = new Message(userName, "", messageField.getText());
				MyClient.Send(lobby);
				messageField.setText("");
				messageField.requestFocus();
			}
		}
	}
	
	/*public static void DisconnectButton()
	{
		try
		{
			chatClient.Disconnect();
		}
		catch (Exception e)
		{
			System.out.println("error in action disconnect in clientgui");
			e.printStackTrace();
		}
	}*/
}