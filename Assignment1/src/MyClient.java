import java.net.*;
import java.io.*;

public class MyClient 
{
	private Socket socket = null;
	private static byte[] sendbuf = null;
	private static byte[] recbuf = null;
	private boolean isConnected = false;
	public static boolean notUnique = true;
	public static User user = new User();
	public static boolean readBusy = true;
	public static String username = "";
	public static String musername = "";
	public static String message = "";
	public static final class Lock { }
	public final static Object lock = new Lock();

	public void communicate() 
	{
		System.out.println(this);
		String inputName = "";
		String choice = "";
		InetAddress address;
		int port = -1;

		while (!isConnected)
		{
			try 
			{
				socket = new Socket("localHost", 6066);
				isConnected = true;
				
				sendbuf = new byte[socket.getSendBufferSize()];
	        	recbuf = new byte[socket.getReceiveBufferSize()];
				
				address = socket.getInetAddress();
				port = socket.getPort();
				user = new User(inputName, address,port);
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
					User nuser = new User(username,address,port);
					sendbuf = ClientHandler.toByteArray(nuser);
					socket.getOutputStream().write(0);
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
		MyClient client = new MyClient();
		client.communicate();
	}
}