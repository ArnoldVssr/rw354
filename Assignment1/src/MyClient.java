import java.net.*;
import java.io.*;

public class MyClient 
{
	private Socket socket = null;
    private ObjectInputStream inFromServer = null;
    private ObjectOutputStream outToServer = null;
    private boolean isConnected = false;
    private boolean notUnique = true;

    
    public void communicate() 
    {
    	String inputName = "";
    	String choice = "";
    	String body = "";
    	String toUser = "";
    	InetAddress address;
    	int port = -1;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	
    	while (!isConnected)
    	{
    		try 
    		{
    			socket = new Socket("localHost", 6066);
    			System.out.println("User Connected.");
    			isConnected = true;
    			address = socket.getInetAddress();
    			port = socket.getPort();
    			outToServer = new ObjectOutputStream(socket.getOutputStream());
                inFromServer = new ObjectInputStream(socket.getInputStream());
    			System.out.print("Username: ");
    			inputName = reader.readLine();
    			User user = new User(inputName, address,port);
    			outToServer.writeObject(0);
                outToServer.writeObject(user);

                while (notUnique)
                {
                	notUnique = (Boolean) inFromServer.readObject();
                	
                	if (notUnique == true)
                	{
                		System.out.println("Name already taken");
                		System.out.print("Username: ");
            			inputName = reader.readLine();
            			user = new User(inputName, address,port);
            			outToServer.writeObject(0);
                        outToServer.writeObject(user);
                	}
                	else
                	{
                		System.out.println("Welcome, " + user.getName());
                	}
                }
                
                while (!choice.toLowerCase().equals("bye"))
                {
                	System.out.println("What you want?");
                	choice = reader.readLine();
                	if (choice.equalsIgnoreCase("whisper"))
                	{
                		System.out.print("User: ");
                		String whipsUser = reader.readLine();
                		System.out.println("Message: ");
                		String whispMessage = reader.readLine();
                		Message tm = new Message(whipsUser, whispMessage);
                		//Message tm = new Message("Arnold", "Hi Arnold, this is a whipser test");
                		outToServer.writeObject(1);
                		outToServer.writeObject(tm);
                	}
                	
                }
                outToServer.writeObject(9);
                outToServer.writeObject("bye");
                System.out.println(inFromServer.toString());
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
        }
    }
    
    public static void main(String[] args) 
    {
        MyClient client = new MyClient();
        client.communicate();
    }
}