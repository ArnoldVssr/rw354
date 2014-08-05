import java.net.*;
import java.awt.Color;
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

public class MyClient extends Thread
{
	private static Socket socket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
	private static boolean isConnected = false;
	public static boolean notUnique = false;
	public static User user = new User();
	public static boolean readBusy = true;
	public static String username = "";
	public static String hostname = "";
	public static String portnum = "";
	public static String musername = "";
	public static String message = "";
	public static final class Lock { }
	public final static Object lock = new Lock();

	 public MyClient()
	 {
		 super();
	 }
	
	public void run() 
	{
		//System.out.println(this);
		String inputName = "";
		String choice = "";
		InetAddress address;
		int port = -1;

		//while (!isConnected)
		//{
			try 
			{
				socket = new Socket(ClientGUI.hostName, Integer.parseInt(ClientGUI.portNumber));
				//isConnected = true;
				
				sendbuf = new byte[socket.getSendBufferSize()];
	        	recbuf = new byte[socket.getReceiveBufferSize()];
				
				address = socket.getInetAddress();
				port = socket.getPort();
				user = new User(ClientGUI.userName, address,port);
				
				//build gui before
				//MyThread io = new MyThread("io_" + user.getPort(),socket);
				//io.start();
				
				sendbuf = ClientHandler.toByteArray(user);
				socket.getOutputStream().write(Message.USER);
				socket.getOutputStream().write(sendbuf);
				socket.getOutputStream().flush();
				
				socket.getInputStream().read(recbuf);
				try
				{
					notUnique = (Boolean) ClientHandler.toObject(recbuf);
				}
				catch (Exception e)
				{
					System.out.println("MC notUnique recv");
				}

				while(notUnique)
				{
					//System.out.println("Client says username was: " + username);
					/*synchronized(lock)
					{
						while (readBusy)
						{
							lock.wait();
						}
					}*/
					
					ClientGUI.mainWindow.setVisible(false);
					ClientGUI.loginWindow.setVisible(true);
					ClientGUI.userNameBoxField.setText("");
					
					while (ClientGUI.enterButton.getAction() == null)
					{
						MyClient.sleep(250);
					}
					
					username = ClientGUI.userNameBoxField.getText().trim();
					
					User nuser = new User(username, address,port);
					
					System.out.println("Notifying server of next send");
					//User nuser = new User(username, address,port);
					sendbuf = ClientHandler.toByteArray(nuser);
					socket.getOutputStream().write(Message.USER);
					socket.getOutputStream().write(sendbuf);
					System.out.println("Client says username is: " + user.getName());
					socket.getInputStream().read(recbuf);
					notUnique = (Boolean) ClientHandler.toObject(recbuf);
					//readBusy = true;
				}
				
				ClientGUI.mainWindow.setVisible(true);
				ClientGUI.loginWindow.setVisible(false);
				
				while(!choice.equalsIgnoreCase("bye"))
				{
					Message rec = new Message();
					try
					{
						socket.getInputStream().read(recbuf);
						rec = (Message) ClientHandler.toObject(recbuf);
						
						System.out.println("MC recv: " + rec.getMessage());
					}
					finally
					{
						if (rec.getRecipient() != "" || rec.getOrigin().equalsIgnoreCase("server"))
						{
							
							synchronized (lock) {
								while (ClientGUI.writeBusy)
								{
									lock.wait();
								}
								ClientGUI.chatArea.append("[" + rec.getOrigin() +"]: " + rec.getMessage() + "\n");
								ClientGUI.writeBusy = false;
								MyClient.lock.notifyAll();
							}
							
						}
						/*if (rec.getOrigin().equalsIgnoreCase("server"))
						{
							socket.wait(200);
							socket.close();
							break;
						}*/
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
			/*catch (InterruptedException e)
			{
				e.printStackTrace();
			}*/
		//}
 catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static void Send(Message message)
	{
		try
		{
			sendbuf = ClientHandler.toByteArray(message);
			if (message.getRecipient().equalsIgnoreCase(""))
			{
				socket.getOutputStream().write(Message.LOBBY);
			}
			else
			{
				socket.getOutputStream().write(Message.WHISPER);
			}
			socket.getOutputStream().write(sendbuf);
			socket.getOutputStream().flush();
		}
		catch (Exception e)
		{
			System.out.println("error in send MyClient");
		}
	}
	
	
	
	
	public static void main(String[] args) 
	{
		//MyClient client = new MyClient();
		//client.Communicate();
	}
}