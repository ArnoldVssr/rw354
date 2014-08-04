import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

class InputHandler
{
	public static void main (String [] args)
	{
	}
}
class MyThread extends Thread
{
	//private MyClient test;
	//private static ThreadLocal tl = new ThreadLocal ();
	private static Socket socket = null;
	private static boolean connected = true;
	MyThread (String name, Socket socket)
	{
		super (name);
		this.socket = socket;
		//this.test = a;
	}
	public void run ()
	{
		//System.out.println(MyClitest);
		String inputName = "";
		String choice = "";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		while(connected)
		{
			try 
			{
				connected = false;
				synchronized(MyClient.lock)
				{
					System.out.println("User Connected.");
					System.out.print("Username: ");
					inputName = reader.readLine();
					MyClient.username = inputName;
					MyClient.readBusy = false;	
					MyClient.lock.notify();
				}
				while(!MyClient.readBusy)
				{
					MyThread.sleep(200);
				}
				
				while (MyClient.notUnique)
				{
					synchronized(MyClient.lock)
					{
						System.out.println("TLD2 says username was: " + MyClient.user.getName());
						System.out.println("Name already taken.");
						System.out.print("Username: ");
						inputName = reader.readLine();
						MyClient.username = inputName;
						System.out.println("TLD2 says username is: " + MyClient.user.getName());
						MyClient.readBusy = false;
						MyClient.lock.notifyAll();
					}
					while(!MyClient.readBusy)
					{
						MyThread.sleep(200);
					}
				}
				
				while(!choice.equalsIgnoreCase("bye"))
				{
					System.out.println("What you want?");
					//choice = reader.readLine();
					char ent = (char)reader.read();
					if (ent == '@')
					{
						StringBuilder user = new StringBuilder();
						while ((ent = (char)reader.read()) != ' '){
							user.append(ent);
						}
						String message = reader.readLine();
						Message whisp = new Message(MyClient.username,user.toString(), message);
						System.out.println("Sending message");
                		socket.getOutputStream().write(1);
                		byte[] bm = new byte[socket.getSendBufferSize()];
                		bm = ClientHandler.toByteArray(whisp);
                		socket.getOutputStream().write(bm);
					}
					else
					{
						String message = reader.readLine();
						message = ent + message;
						Message whisp = new Message(MyClient.username,"".toString(), message);
						System.out.println("Sending message");
                		socket.getOutputStream().write(2);
                		byte[] bm = new byte[socket.getSendBufferSize()];
                		bm = ClientHandler.toByteArray(whisp);
                		socket.getOutputStream().write(bm);
					}
					if (choice.equalsIgnoreCase("bye"))
					{
						System.out.println("Closing...");
						socket.getOutputStream().write(9);
					}
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}