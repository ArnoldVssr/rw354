import java.io.*;
import java.net.*;
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
			for (int i = 0; i < Server.connectionList.size(); i++)
			{
				if (Server.connectionList.get(i) == socket)
				{
					Server.connectionList.remove(i);
				}
			}
			
			//fixed
			for (int i = 0; i < Server.connectionList.size(); i++)
			{
				Socket temp = (Socket) Server.connectionList.get(i);
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
						
						System.out.println("isUser there: " + Server.userList.contains(recipient));
						
						System.out.println("users are: ");
						//moet fix
						for (int i = 0; i < Server.userList.size(); i++)
						{
							String user = (String) Server.userList.get(i);
							if (user.equalsIgnoreCase(recipient))
							{
								Socket tmpSocket = Server.connectionList.get(i);
								PrintWriter tempWriter = new PrintWriter(tmpSocket.getOutputStream());
								tempWriter.println("[" + sender + "]: " + temp);
								tempWriter.flush();
							}
							if (user.equalsIgnoreCase(sender))
							{
								Socket tmpSocket = Server.connectionList.get(i);
								PrintWriter tempWriter = new PrintWriter(tmpSocket.getOutputStream());
								tempWriter.println("[" + sender + "]: " + temp);
								tempWriter.flush();
							}
							
						}
						System.out.println("whisper sent");
						
					}
					else
					{
						//fixed
						for (int i = 0; i < Server.connectionList.size(); i++)
						{
							Socket echoSocket = Server.connectionList.get(i);
							PrintWriter tempWriter = new PrintWriter(echoSocket.getOutputStream());
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
