import java.io.*;
import java.net.*;
import javax.swing.*;


public class ClientGUI
{
	private static Client chatClient;
	public static String userName = "anon";
	
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
	private static JButton enterButton = new JButton("Enter");
	private static JLabel enterUserNameLabel = new JLabel("Enter username: ");
	private static JPanel loginPane = new JPanel();
	
	public static void main(String[] args)
	{
		BuildMainWindow();
		Initialize();
	}
	
	public static void BuildMainWindow()
	{
		mainWindow.setTitle("Cr@p Talk");
		mainWindow.setSize(500, 320);
		ConfigureMainWindow();
		MainWindowAction();
		mainWindow.setVisible(true);
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
			final int port = 6066;
			final String hostName = "localhost";
			
			Socket clientSocket = new Socket(hostName, port);
			System.out.println("You connected to: " + hostName);
						
			chatClient = new Client(clientSocket);
			PrintWriter output = new PrintWriter(clientSocket.getOutputStream());
			System.out.println("username sent to server");
			output.println(userName);
			output.flush();
			
			Thread clientThread = new Thread(chatClient);
			clientThread.start();
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
		sendButton.setBounds(248, 40, 81, 25);
		
		whisperButton.setText("Whisper");
		mainWindow.getContentPane().add(whisperButton);
		whisperButton.setBounds(338, 40, 100, 25);
		
		disconnectButton.setText("Disconnect");
		mainWindow.getContentPane().add(disconnectButton);
		disconnectButton.setBounds(10, 40, 115, 25);
		
		connectButton.setText("Connect");
		mainWindow.getContentPane().add(connectButton);
		connectButton.setBounds(130, 40, 110, 25);
		
		messageLabel.setText("Message:");
		mainWindow.getContentPane().add(messageLabel);
		messageLabel.setBounds(5, 6, 80, 20);
		
		messageField.requestFocus();
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
		userNameLabel.setBounds(348, 0 , 140, 15);
		
		userNameBox.setHorizontalAlignment(SwingConstants.CENTER);
		mainWindow.getContentPane().add(userNameBox);
		userNameBox.setBounds(340, 17 , 150, 20);
	}
	
	public static void BuildLoginWindow()
	{
		loginWindow.setTitle("Set Username");
		loginWindow.setSize(400, 100);
		loginPane = new JPanel();
		loginPane.add(enterUserNameLabel);
		userNameBoxField.setText("");
		loginPane.add(userNameBoxField);
		loginPane.add(enterButton);
		loginWindow.add(loginPane);
		System.out.println("stared login action");
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
			String tempName = userNameBoxField.getText().trim();		
			userName = tempName;
			//userNameLabel.setText("Username: " + userName);
			mainWindow.setTitle(userName);
			//Server.userList.add(userName);
			loginWindow.setVisible(false);
			sendButton.setEnabled(true);
			whisperButton.setEnabled(true);
			disconnectButton.setEnabled(true);
			connectButton.setEnabled(false);
			System.out.println("Doing connect");
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
					SendMessage();
				}
			});
		
		disconnectButton.addActionListener(
			new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent event)
				{
					Disconnect();
				}
			});
		
		connectButton.addActionListener(
			new java.awt.event.ActionListener()
			{
				public void actionPerformed(java.awt.event.ActionEvent event)
				{
					System.out.println("Connected clicked");
					BuildLoginWindow();
				}
			});
	}
	
	public static void SendMessage()
	{
		if (!messageField.getText().equals(""))
		{		
			
			System.out.println("clientgui_ActionBsend: "+ messageField.getText());
			chatClient.Send(messageField.getText());
			messageField.requestFocus();
		}
	}
	
	public static void Disconnect()
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
	}
}
