import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
 
public class Server
{
	public static Map<Socket,User> mapTest = new HashMap<Socket,User>();
	
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
				//connectionList.add(clientSocket);
				
				String userName = "";
				boolean unique = false;
				while (unique == false)
				{
					Scanner userInput = new Scanner(clientSocket.getInputStream());
					PrintWriter userOutput = new PrintWriter(clientSocket.getOutputStream());
					userName = userInput.nextLine();
					
					User user = new User(userName,clientSocket.getInetAddress(),clientSocket.getPort());
					
					if (mapTest.containsKey(clientSocket))
					{
						userOutput = new PrintWriter(clientSocket.getOutputStream());
						userOutput.println("%^&false");
						userOutput.flush();
					}
					else
					{
						mapTest.put(clientSocket,user);
						unique = true;
					}
				}				
				
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
		ClientGUI.userName = userName;
		ClientGUI.userNameLabel.setText("Username: " + userName);
		ClientGUI.mainWindow.setTitle("Cr@p Talk: "+ userName);
		
		StringBuilder users = new StringBuilder();
		users.append("#?!");
		for(User a: mapTest.values())
		{
			users.append(a.getName());
			users.append(',');
		}
		users.deleteCharAt(users.length()-1);
		System.out.println(users);
		//fixed
		for(Map.Entry<Socket,User> entry: mapTest.entrySet())
		{
			Socket newOnlineUser = (Socket)entry.getKey();
			System.out.println("Socket: " + newOnlineUser);
			PrintWriter userOutput = new PrintWriter(newOnlineUser.getOutputStream());
			System.out.println(users.toString());
			userOutput.println(users.toString());
			System.out.println("Passed user output write");
			userOutput.flush();
		}
	}
	
	public static byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }
}