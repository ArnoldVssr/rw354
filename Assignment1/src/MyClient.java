import java.net.*;
import java.io.*;

public class MyClient 
{
	private Socket socket = null;
	private ObjectInputStream inFromServer = null;
	private ObjectOutputStream outToServer = null;
	private boolean isConnected = false;
	public static boolean notUnique = true;
	public static User user = new User();
	public static Message tm = new Message();
	public static boolean readBusy = true;
	public static final class Lock { }
	public final static Object lock = new Lock();

	public void communicate() 
	{
		System.out.println(this);
		String inputName = "";
		String choice = "";
		String body = "";
		String toUser = "";
		InetAddress address;
		int port = -1;

		while (!isConnected)
		{
			try 
			{
				socket = new Socket("localHost", 6066);
				isConnected = true;
				address = socket.getInetAddress();
				port = socket.getPort();
				user = new User(inputName, address,port);
				MyThread io = new MyThread("io_" + user.getPort());
				io.start();
				outToServer = new ObjectOutputStream(socket.getOutputStream());
				inFromServer = new ObjectInputStream(socket.getInputStream());

				synchronized (lock) {
					while (readBusy)
					{
						lock.wait();	
					}
					System.out.println("Notifying server of next send");
					outToServer.writeObject(0);
					outToServer.writeObject(user);
					notUnique = (Boolean) inFromServer.readObject();
					readBusy = true;
				}

				while(notUnique)
				{
					System.out.println("Client says username was: " + user.getName());
					synchronized(lock)
					{
						while (readBusy)
						{
							lock.wait();
						}
					}
					outToServer.writeObject(0);
					outToServer.writeObject(user);
					System.out.println("Client says username is: " + user.getName());
					notUnique = (Boolean) inFromServer.readObject();
					readBusy = true;
				}
				Message temp = new Message();
				while(!choice.equalsIgnoreCase("bye"))
				{
					temp = (Message) inFromServer.readObject();
					if (temp.getRecipient() != "")
					{
						System.out.println(temp.getRecipient() + ": " + temp.getMessage());
					}
					synchronized(lock)
					{
						while(readBusy)
						{
							lock.wait();
							
						}
						outToServer.writeObject(1);
						outToServer.writeObject(tm);
						readBusy = true;
					}
				}
				outToServer.writeObject(9);
				inFromServer.close();
				outToServer.close();
				socket.close();
				System.out.println(user.getName() + " disconnected");
			}
			catch (SocketException socketError) 
			{
				System.err.println(socketError.getMessage());
			} 
			catch (IOException e)
			{
				System.err.println(e.getMessage());
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