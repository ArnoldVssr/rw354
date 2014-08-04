import java.awt.*;

import javax.swing.*;
 
public class GUI
{
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
	public static JTextField portNumber = new JTextField(20);
	public static JTextField hostName = new JTextField(20);
	private static JButton enterButton = new JButton("Enter");
	private static JLabel enterUserNameLabel = new JLabel("Username: ");
	private static JLabel enterPortLabel = new JLabel("Port number: ");
	private static JLabel enterHostLabel = new JLabel("Host name: ");
	private static JPanel loginPane = new JPanel();
	
    public static void main(String[] args)
    {
    	Font font = new Font("Arial", Font.BOLD, 12);
    	
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
		mainWindow.setVisible(true);
    	
    }
}
