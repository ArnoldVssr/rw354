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
			for (int i = 0; i < Server.connectionList.size(); i++)
			{
				if (Server.connectionList.get(i) == socket)
				{
					Server.connectionList.remove(i);
				}
			}
			
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
					System.out.println("OUT_SERVERRETURN_RUN: " + message);
					
					for (int i = 0; i < Server.connectionList.size(); i++)
					{
						Socket echoSocket = Server.connectionList.get(i);
						PrintWriter tempWriter = new PrintWriter(echoSocket.getOutputStream());
						System.out.println("OUT_serverReturn_run: " + message);
						tempWriter.println(message);
						tempWriter.flush();
						
						System.out.println("Message sent to "+ echoSocket.getLocalAddress().getHostName());
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
