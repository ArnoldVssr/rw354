import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;
 
public class Server
{
	public static ArrayList<Socket> connectionList = new ArrayList<Socket>();
	public static ArrayList<String> userList = new ArrayList<String>();
	public static ArrayList<String> uniqueUser = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException
	{
		try
		{
			final int port = 6066;
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Waiting for users...");
			
			while (true)
			{
				Socket clientSocket = serverSocket.accept();
				connectionList.add(clientSocket);
				System.out.println("before add iuserClient connected from: " + clientSocket.getLocalAddress().getHostName());
				
				//////////////////////////////////////////////////////////////////////////////////////////////////
				String userName = "";
				boolean unique = false;
				while (unique == false)
				{
					Scanner userInput = new Scanner(clientSocket.getInputStream());
					PrintWriter userOutput = new PrintWriter(clientSocket.getOutputStream());
					userName = userInput.nextLine();
					
					System.out.println("from client got: " + userName);
				
					System.out.println("Does user exist: " + uniqueUser.contains(userName.toLowerCase()));
					if (uniqueUser.contains(userName.toLowerCase()))
					{
						System.out.println("sent false");
						userOutput = new PrintWriter(clientSocket.getOutputStream());
						userOutput.println("%^&false");
						userOutput.flush();
					}
					else
					{
						System.out.println("sent true");
						unique = true;
					}
				}
				
				System.out.println("out of while with unique: " + unique);
				System.out.println("username is: " + userName);
				
				for (int i = 0; i < userList.size(); i++) {
					System.out.println((String) userList.get(i));
					
				}
				
				///////////////////////////////////////////////////////////////////////////////////////////////////
				AddUserName(userName);
				
				ServerSend chat = new ServerSend(clientSocket);
				Thread chatThread = new Thread(chat);
				chatThread.start();
			}
			
		}
		catch (Exception e)
		{
			System.out.println("User could not connect to server (MAIN).");
			System.out.println(e.getMessage());
		}
	}
	
	public static void AddUserName(String userName) throws Exception
	{	
		userList.add(userName);
		uniqueUser.add(userName.toLowerCase());
		ClientGUI.userName = userName;
		
		ClientGUI.userNameLabel.setText("Username: " + userName);
		
		for (int i = 0; i < Server.connectionList.size(); i++)
		{
			Socket newOnlineUser = (Socket) Server.connectionList.get(i);
			PrintWriter userOutput = new PrintWriter(newOnlineUser.getOutputStream());
			userOutput.println("#?!" + userList);
			//userOutput.println(userList);
			userOutput.flush();
		}
	}
}