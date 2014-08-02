import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Scanner;

public class ServerSend implements Runnable 
{
	Socket socket;
	private Scanner input;
	private PrintWriter output;
	String message = "";
	
	public ServerSend(Socket socket)
	{
		this.socket = socket;
	}
	
	public void checkConnection() throws Exception
	{
		if (!socket.isConnected())
		{
			//fixed
			if(Server.mapTest.containsKey(socket))
			{
				Server.mapTest.remove(socket);
			}
			
			//fixed
			for (Map.Entry<Socket,User> entry: Server.mapTest.entrySet())
			{
				Socket temp = entry.getKey();
				PrintWriter tempWriter = new PrintWriter(temp.getOutputStream());
				tempWriter.println("User from "+ temp.getLocalAddress().getHostName() +" disconnected");
				tempWriter.flush();
				
				System.out.println("User from "+ temp.getLocalAddress().getHostName() +" disconnected");
			}
		}
	}
	
	public void run()
	{
		try
		{
			try
			{
				output = new PrintWriter(socket.getOutputStream());
				input = new Scanner(socket.getInputStream());
				
				while (true)
				{
					checkConnection();
					
					if(!input.hasNext())
					{
						return;
					}
					
					message = input.nextLine();
					String option = message.substring(0, 3);
					System.out.println("server got this message: " + message);
					
					if (option.equals("@*&"))
					{
						System.out.println("got a whisper");
						String temp = message.substring(3);				
						int endUser = temp.indexOf("(!}");
						String sender = temp.substring(0, endUser);
						
						
						temp = temp.substring(endUser+3, temp.length());
						endUser = temp.indexOf("<$%");
						String recipient = temp.substring(0, endUser);
						
						temp = temp.substring(endUser+3, temp.length());
						
						System.out.println("isUser there: " + Server.mapTest.containsValue(recipient));
						
						System.out.println("users are: ");
						
						//moet fix
						for(Map.Entry<Socket, User> entry: Server.mapTest.entrySet())
						{
							if (Server.mapTest.containsValue(recipient) || Server.mapTest.containsValue(sender))
							{
							
								Socket tmpSocket = entry.getKey();
								PrintWriter tempWriter = new PrintWriter(tmpSocket.getOutputStream());
								tempWriter.println("[" + entry.getValue() + "]: " + temp);
								tempWriter.flush();
							}
						}
						System.out.println("whisper sent");
						
					}
					else
					{
						//fixed
						for(Map.Entry<Socket, User> entry: Server.mapTest.entrySet())
						{
							Socket echoSocket = entry.getKey();
							PrintWriter tempWriter = new PrintWriter(echoSocket.getOutputStream());
							System.out.println("OUT_serverReturn_run: " + message);
							tempWriter.println(message);
							tempWriter.flush();
							
							System.out.println("Message sent to "+ echoSocket.getLocalAddress().getHostName());
						}
					}
				}
			}
			finally
			{
				socket.close();
			}
		}
		catch (Exception e)
		{
			System.out.println("error in run of ServerReturn");
			System.out.println(e);
		}
	}
}
