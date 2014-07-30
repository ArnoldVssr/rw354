import java.net.*;
import java.util.HashSet;
import java.io.*;
 
public class ClientHandler extends Thread 
{
    private Socket socket = null;
    private ObjectInputStream recieved = null;
    private ObjectOutputStream sent = null;
    private static HashSet<User> users = new HashSet<User>();
    private static boolean unique = false;
    
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
	
	public static boolean containsUser(User user)
	{
		if (users.contains(user))
		{
			return true;
		}
		else
		{
			users.add(user);
			return false;
		}
	}
    
    public void run() {
 
        try
        {
        	System.out.println("remote Socket Address "
                    + socket.getRemoteSocketAddress());
        	
        	// Get unique user
        	while (!unique)
        	{
        		recieved = new ObjectInputStream(socket.getInputStream());
            	sent = new ObjectOutputStream(socket.getOutputStream());
            	User cur_user = (User) recieved.readObject();
            	
            	if (containsUser(cur_user))
            	{
            		System.out.println("Name taken, notifying client");
            		sent.writeObject(true);
            	}
            	else
            	{
            		System.out.println("Unique name, notiying client");
            		sent.writeObject(false);
            		unique = true;
            	}
        	}
        	
        	// Wait user input
        	while(true)
        	{
        		break;
        	}
        	/*while(true)
        	{
        		System.out.println("test");
        		
        	}*/
        	socket.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } 
        catch (ClassNotFoundException e) 
        {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
