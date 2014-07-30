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
    	SocketAddress userAddress = null;
    	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    	
    	while (!isConnected)
    	{
    		try 
    		{
    			socket = new Socket("localHost", 6066);
    			System.out.println("User Connected.");
    			isConnected = true;
    			userAddress = socket.getRemoteSocketAddress();
    			outToServer = new ObjectOutputStream(socket.getOutputStream());
                inFromServer = new ObjectInputStream(socket.getInputStream());
    			System.out.print("Username: ");
    			inputName = reader.readLine();
    			User user = new User(inputName, userAddress);
    			Message message = new Message();
                outToServer.writeObject(user);
                

                while (notUnique)
                {
                	notUnique = (Boolean) inFromServer.readObject();
                	
                	if (notUnique == true)
                	{
                		System.out.println("Name already taken");
                		System.out.print("Username: ");
            			inputName = reader.readLine();
            			user = new User(inputName, userAddress);
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
                	System.out.println("1)Send message to user");
                	System.out.println("2)Connected users");
                	choice = reader.readLine();
                	
                	if (choice.equals("1"))
                	{
                		System.out.print("Message: ");
                		body = reader.readLine();
                		System.out.print("To: ");
                		toUser = reader.readLine();
                	}
                	
                	message = new Message(toUser, body);
                }
                outToServer.writeObject("bye");
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